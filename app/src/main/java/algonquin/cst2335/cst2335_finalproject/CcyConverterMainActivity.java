package algonquin.cst2335.cst2335_finalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.cst2335_finalproject.databinding.CurrencyconverterMainBinding;
import algonquin.cst2335.cst2335_finalproject.databinding.*;


/**
 * Currency converter created for CST2335 Final Project.
 * @author Huixin Xu
 * @version 1.0
 */
public class CcyConverterMainActivity extends AppCompatActivity {
    CurrencyconverterMainBinding ccyBinding;
    RecyclerView ccyRecyclerView;
    private RecyclerView.Adapter myAdapter;
    ArrayList<CcyListItem> ccyItemList = new ArrayList<>();
    CcyViewModel ccyViewModel;
    CcyListItemDAO myDAO;

    private Button ccyConvertButton;
    private EditText ccyAmtTxt;
    private TextView ccyResultAmt;
    private Spinner spinnerFromUnit;
    private Spinner spinnerToUnit;
    private Toolbar ccyToolbar;
    private ImageView ccyAddlist;

//    private String fromCcyUnit;
//    private String toCcyUnit;
//    private String fromCcyAmt;
//    private String ccyConvertedAmt;
    private String ccyConvertedTime="";

    private RequestQueue requestQueue;

    private static final String APIkey = "b29a0a8b0217704ebec9a1c47daf29639550ce8a";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ccyBinding = CurrencyconverterMainBinding.inflate(getLayoutInflater());
        setContentView(ccyBinding.getRoot());

        // Initialize components using data binding
        ccyRecyclerView = ccyBinding.ccyRecyclerView;
        ccyAmtTxt = ccyBinding.ccyAmtTxt;
        ccyConvertButton = ccyBinding.ccyConvertButton;
        ccyResultAmt = ccyBinding.ccyResultAmt;
        spinnerFromUnit = ccyBinding.spinnerFromUnit;
        spinnerToUnit = ccyBinding.spinnerToUnit;
        ccyToolbar = ccyBinding.ccyToolbar;
        ccyAddlist = ccyBinding.ccyAddlist;

        ccyViewModel = new ViewModelProvider(this).get(CcyViewModel.class);
        ccyItemList = ccyViewModel.ccyItemList.getValue();


        // Add tool bar by calling onCreateOptionsMenu()
        setSupportActionBar(ccyToolbar);

        // Populate dropdown list
        ArrayAdapter<CharSequence> ccyAdapter = ArrayAdapter.createFromResource(this, R.array.ccyList, android.R.layout.simple_spinner_item);
        ccyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerFromUnit.setAdapter(ccyAdapter);
        spinnerToUnit.setAdapter(ccyAdapter);

        // Access the database:
        CcyDatabase db = Room.databaseBuilder(getApplicationContext(), CcyDatabase.class, "CurrencyConvertorDatabase").build();
        myDAO = db.getDAO();

        if (ccyItemList == null) {
            ccyViewModel.ccyItemList.setValue(ccyItemList = new ArrayList<>());

            // get all list item from database
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(()->{
                List<CcyListItem> fromDatabase = myDAO.getAllCCYListItem();
                ccyItemList.addAll(fromDatabase);

                runOnUiThread(()->{
                    ccyRecyclerView.setAdapter(myAdapter);
                });
            });
        }

        // Connect to Server
        requestQueue = Volley.newRequestQueue(this);

        myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            // Creates a ViewHolder object representing a single row in the list
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CcylistItemBinding listItemBinding = CcylistItemBinding.inflate(getLayoutInflater(),parent,false);
                return new MyRowHolder(listItemBinding.getRoot());
            }


            @Override
            // Initializes a ViewHolder object
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                CcyListItem obj = ccyItemList.get(position);
                // Set the input currency unit and amount to the list item
                holder.ccy_from_unit.setText(obj.getFromCcyUnit());
                holder.ccy_to_unit.setText(obj.getConvertedCcyUnit());
                holder.ccy_list_fromAmt.setText("$"+ obj.getFromCcyAmt());
                holder.ccy_list_toAmt.setText("$"+ obj.getConvertedCcyAmt());
                holder.convertTime.setText(obj.getTimeConverted());
            }

            @Override
            // Returns an int specifying how many items to draw
            public int getItemCount() {
                return ccyItemList.size();
            }
        };

    // Re-load input data from previous time running the application
    SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
    int fromUnitIndex = prefs.getInt("FromCurrencyUnitIndex",0);
    int toUnitIndex = prefs.getInt("ToCurrencyUnitIndex",0);
    String preAmt = prefs.getString("AmountToConvert","");
        spinnerFromUnit.setSelection(fromUnitIndex);
        spinnerToUnit.setSelection(toUnitIndex);
        ccyAmtTxt.setText(preAmt);

    String userFromCCYUnit = spinnerFromUnit.getSelectedItem().toString();
    String userToCCYUnit = spinnerToUnit.getSelectedItem().toString();
    int spinnerFromPos = ccyAdapter.getPosition(userFromCCYUnit);
    int spinnerToPos = ccyAdapter.getPosition(userToCCYUnit);

    // Convert button to convert the currency
        ccyConvertButton.setOnClickListener(click->{
        String fromUnit = spinnerFromUnit.getSelectedItem().toString().substring(0,3);
        String toUnit = spinnerToUnit.getSelectedItem().toString().substring(0,3);
        String fromAmt = ccyAmtTxt.getText().toString();

        // Save input data for next time run the application
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("FromCurrencyUnitIndex",spinnerFromPos);
        editor.putInt("ToCurrencyUnitIndex", spinnerToPos);
        editor.putString("AmountToConvert",fromAmt);
        editor.apply();

        String url = "https://api.getgeoapi.com/v2/currency/convert?format=json&from="+ fromUnit + "&to=" + toUnit +"&amount="+ fromAmt +"&api_key="+ APIkey + "&format=json";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("rates");
                    JSONObject rateObject = jsonObject.getJSONObject(toUnit);
                    String rateForAmt = rateObject.getString("rate_for_amount");
                    String updateDate = response.getString("updated_date");
                    String convAmt = formatAmount(rateForAmt);
                    ccyResultAmt.setText(convAmt);
                    ccyConvertedTime = updateDate;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
    });

    // Button to create a new list item and add into list
        ccyAddlist.setOnClickListener(clk->{
        String fromUnit = spinnerFromUnit.getSelectedItem().toString().substring(0,3);
        String toUnit = spinnerToUnit.getSelectedItem().toString().substring(0,3);
        String fromAmt = ccyAmtTxt.getText().toString();
        String convertAmt = ccyResultAmt.getText().toString();
        double fromAmtNum = Double.parseDouble(fromAmt);
        double convertAmtNum = Double.parseDouble(convertAmt);

        CcyListItem newListItem = new CcyListItem(fromUnit,fromAmtNum,toUnit,convertAmtNum,ccyConvertedTime);
        ccyItemList.add(newListItem);

        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(()->{
            newListItem.id = myDAO.insertCCYListItem(newListItem);
            // Run on UI thread
            runOnUiThread(()->myAdapter.notifyItemInserted(ccyItemList.size()-1));
        });

        makeToast("Added to Favourite List");
    });


        ccyRecyclerView.setAdapter(myAdapter);
        ccyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Observe method for fragments
        ccyViewModel.selectedCcyItem.observe(this,(newValue)->{
            CcyDetailsFragment ccyItemFragment = new CcyDetailsFragment(newValue);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.ccy_fragmentSection,ccyItemFragment)
                    .addToBackStack("Go Back")
                    .commit();
        });

    }



    CcyListItem selectedItem;
    int rowClicked;
    public class MyRowHolder extends RecyclerView.ViewHolder{
        TextView ccy_from_unit;
        TextView ccy_to_unit;
        TextView ccy_list_fromAmt;
        TextView ccy_list_toAmt;
        TextView convertTime;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            ccy_from_unit = itemView.findViewById(R.id.ccy_list_from);
            ccy_to_unit = itemView.findViewById(R.id.ccy_list_to);
            ccy_list_fromAmt = itemView.findViewById(R.id.ccy_list_fromAmt);
            ccy_list_toAmt = itemView.findViewById(R.id.ccy_list_toAmt);
            convertTime = itemView.findViewById(R.id.time);

            // Show detail in fragment when click the row
            itemView.setOnClickListener(click->{
                //rowClicked = getAbsoluteAdapterPosition();
                rowClicked = getAbsoluteAdapterPosition();
                selectedItem = ccyItemList.get(rowClicked);
                ccyViewModel.selectedCcyItem.postValue(selectedItem);
            });

        }
    }

    public void makeToast(String str) {
        Toast toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
        toast.show();
    }


    // Create tool bar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.ccy_menu,menu);
        return true;
    }

    // Behaviour for selecting menu options
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String aboutMsg = "Currency Converter created by Huixin Xu (041062218)";

        if (item.getItemId()==R.id.menu_del){
            AlertDialog.Builder builder = new AlertDialog.Builder( CcyConverterMainActivity.this );
            builder.setMessage("Are you sure to delete the Record? ")
                    .setTitle("Question")
                    //Show "Yes / No" button, if click "No" nothing happened, while click "Yes" delete the message row and from database
                    .setNegativeButton("No", ((dialog, clk) ->{} ))
                    .setPositiveButton("Yes",((dialog, clk) ->{

                        // Delete selected list item from database
                        Executor thread = Executors.newSingleThreadExecutor();
                        thread.execute(()->{
                            myDAO.deleteCCYListItem(selectedItem);
                            ccyItemList.remove(rowClicked);

                            runOnUiThread(()->myAdapter.notifyItemRemoved(rowClicked));
                        });

                        Snackbar.make(ccyRecyclerView,"Record deleted successfully", Snackbar.LENGTH_LONG )
                                .setAction("Undo", clk2->{
                                    //reinsert the message:
                                    Executor thrd = Executors.newSingleThreadExecutor();
                                    thrd.execute(()->{ myDAO.insertCCYListItem(selectedItem); });
                                    ccyItemList.add(rowClicked,selectedItem);
                                    runOnUiThread(()->myAdapter.notifyDataSetChanged());
                                })
                                .show();
                    } ))

                    //show the alert dialog window:
                    .create().show();

        } else if (item.getItemId()== R.id.menu_about){
            makeToast(aboutMsg);

        } else if (item.getItemId()== R.id.menu_help){
            AlertDialog.Builder helpBuilder = new AlertDialog.Builder( CcyConverterMainActivity.this );
            helpBuilder.setMessage("1. Select Original Currency Unit.\n2.Select Currency Unit to convert.\n" +
                            "3.Input the Amount.\n4.Press Convert button to convert.\n5. Press Dollar Sign icon to swap Currency Unit." +
                            "\n6. Click Add icon to bookmark current query.\n7. Click Update button to update bookmark list.\n" +
                            "8. Click on list item to see the detail.")
                    .setTitle("Instruction")
                    .setNeutralButton("Back",((dialog, which) -> {}))
                    .show();
        }
        return true;
    }

    private String formatAmount(String value){
        double amt = Double.parseDouble(value);
        DecimalFormat format = new DecimalFormat("#.##");
        return format.format(amt);
    }


}

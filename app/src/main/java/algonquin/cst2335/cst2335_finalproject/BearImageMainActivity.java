package algonquin.cst2335.cst2335_finalproject;


import static algonquin.cst2335.cst2335_finalproject.R.*;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.cst2335_finalproject.databinding.BearImageViewBinding;
import algonquin.cst2335.cst2335_finalproject.databinding.BearimagegeneratorMainBinding;


public class BearImageMainActivity extends AppCompatActivity {

    BearimagegeneratorMainBinding binding;
    BearImageViewBinding imageBinding;

    Toolbar toolbar;
    EditText width;
    EditText height;
    Button generateButton;


    private RecyclerView recyclerView;
    private ArrayList<String> stringArrayList;
    private BearRecyclerViewAdapter recyclerViewAdapter;


    /**
     * OnCreate for Options menu
     * @param menu The options menu in which you place your items.
     *
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.bear_menu, menu);
        return true;
    }

    /**
     * Finds out when an item is selected
     * @param item The menu item that was selected.
     *
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.menu_help){
            showHelpDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * AlertDialog for when help menu option is selected
     */
    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Instructions");
        builder.setMessage("Enter Width on left, Height on right, then press the generate button");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /**
     * OnCreate method for main activity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.bearimagegenerator_main);

        toolbar = findViewById(id.toolbar);
        width = findViewById(id.width);
        height = findViewById(id.height);
        generateButton = findViewById(id.generateButton);
        recyclerView = findViewById(id.recycleView);


        stringArrayList = new ArrayList<>();

        recyclerViewAdapter = new BearRecyclerViewAdapter(this, stringArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);

        generateButton.setOnClickListener(clk -> {

            String widthInput = width.getText().toString();
            String heightInput = height.getText().toString();


            if (widthInput.isEmpty() && heightInput.isEmpty()) {
                Toast.makeText(BearImageMainActivity.this, "Both width and height cannot be null", Toast.LENGTH_SHORT).show();
            } else {
                stringArrayList.clear();
                int widthValue = Integer.parseInt(widthInput);
                int heightValue = Integer.parseInt(heightInput);
                String url = "https://placebear.com/" + widthValue + "/" + heightValue + ".jpg";
                stringArrayList.add(url);
                recyclerViewAdapter.notifyDataSetChanged();

            }


        });


    }


}








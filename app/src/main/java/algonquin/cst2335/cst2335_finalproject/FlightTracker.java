package algonquin.cst2335.cst2335_finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.cst2335_finalproject.databinding.FlightTrackerBinding;

public class FlightTracker extends AppCompatActivity implements FlightRecyclerViewInterface {
    ImageButton search;
    ImageButton saved;
    FlightTrackerBinding binding;
    Button saveFlight;

    EditText enterFlight;
    RequestQueue queue = null;
    ArrayList<FlightModel> flightModels = new ArrayList<>();
    ArrayList<FlightModel> savedFlights = new ArrayList<>();
    FlightModelDAO fmDAO;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.flight_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.help){
            AlertDialog.Builder builder = new AlertDialog.Builder(FlightTracker.this);
            builder.setMessage("Enter a 3 letter airport code to get all the flights from that airport." +
                            "Click a flight from the screen to view details about the flight, and to save " +
                            "the flight. Click the saved flights icon in the menu bar in order to view your saved flights." +
                            " You can click on a flight to bring up the option to delete the flight from your saved flights.")
                    .setTitle("Flight Tracker Help")
                    .setNeutralButton("Ok", (dialog, cl)->{})
                    .create().show();
        }
        else if(item.getItemId() == R.id.savedFlights) {
            Intent toSavedFlights = new Intent(this, SavedFlights.class);
            startActivity(toSavedFlights);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlightDatabase db = Room.databaseBuilder(getApplicationContext(), FlightDatabase.class, "SavedFlights").build();
        fmDAO = db.fmDAO();
        binding = FlightTrackerBinding.inflate(getLayoutInflater());

        queue = Volley.newRequestQueue(this);
        setContentView(R.layout.flight_tracker);
        SharedPreferences prefs  = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        search = findViewById(R.id.search);
        enterFlight = findViewById(R.id.enterFlight);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Flight Tracker");
        toolbar.showOverflowMenu();

        String userAirportCode = prefs.getString("AirportCode", "");
        enterFlight.setText(userAirportCode);

        //database??
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() ->{
            savedFlights.addAll(fmDAO.getAllFlights());
        });


        search.setOnClickListener(click ->{
            flightModels.clear();
            String airportCode = enterFlight.getText().toString();
            String url = "http://api.aviationstack.com/v1/flights?access_key=a0a7276cce38b003a8f87610bfb18012&dep_iata=" + URLEncoder.encode(airportCode);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("AirportCode", enterFlight.getText().toString());
            editor.apply();
            //Pull the data from the API with this JSON request
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (response) -> {
                        try {
                            JSONArray data = response.getJSONArray("data");

                            for (int i = 0; i < data.length(); i++){
                                JSONObject thisObj = data.getJSONObject(i);
                                String status = thisObj.getString("flight_status");

                                JSONObject dept = thisObj.getJSONObject("departure");
                                String deptTime = dept.getString("estimated");
                                String gate = dept.getString("gate");
                                String delayString = dept.getString("delay");

                                String terminal = dept.getString("terminal");
                                JSONObject arrival = thisObj.getJSONObject("arrival");
                                String destAirport = arrival.getString("airport");
                                String destCode = arrival.getString("iata");

                                if (delayString != "null") {
                                    int delay = Integer.parseInt(delayString);
                                    flightModels.add(new FlightModel(airportCode, status, deptTime, gate, terminal, destAirport, destCode, delay));
                                }
                                else {
                                    flightModels.add(new FlightModel(airportCode, status, deptTime, gate, terminal, destAirport, destCode, 0));
                                }

                            }
                            FlightRecyclerViewAdapter adapter = new FlightRecyclerViewAdapter(this, flightModels, this);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(this));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    },
                    (error) ->{
                            int i = 0;
                    });


            queue.add(request);


        });

    }

    private void setUpFlightModels() {
        //write some code
    }

    @Override
    public void onItemClick(int position) {
        FlightDetailsFragment frag = new FlightDetailsFragment(flightModels.get(position));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocation, frag)
                .addToBackStack("")
                .commit();
    }
}

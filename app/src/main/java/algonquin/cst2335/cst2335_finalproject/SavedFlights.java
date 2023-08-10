package algonquin.cst2335.cst2335_finalproject;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.cst2335_finalproject.databinding.FlightTrackerBinding;
import algonquin.cst2335.cst2335_finalproject.databinding.SavedFlightsBinding;

public class SavedFlights extends AppCompatActivity implements FlightRecyclerViewInterface {

    ArrayList<FlightModel> savedFlights = new ArrayList<>();
    FlightModelDAO fmDAO;
    SavedFlightsBinding binding;

    FlightRecyclerViewAdapter adapter;

    @Override
    public void onItemClick(int position) {

        SavedFlightDetailsFragment frag = new SavedFlightDetailsFragment(savedFlights.get(position));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocation2, frag)
                .addToBackStack("")
                .commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.flight_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.help){
            AlertDialog.Builder builder = new AlertDialog.Builder(SavedFlights.this);
            builder.setMessage("This is the help page!! I gotta edit this soon!")
                    .setTitle("Flight Tracker Help")
                    .setNeutralButton("Ok", (dialog, cl)->{})
                    .create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle SavedInstance) {
        super.onCreate(SavedInstance);

        FlightDatabase db = Room.databaseBuilder(getApplicationContext(), FlightDatabase.class, "SavedFlights").build();
        fmDAO = db.fmDAO();
        binding = SavedFlightsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        RecyclerView recyclerView = binding.recyclerView2;


        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Saved Flights");
//        toolbar.showOverflowMenu();

        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() ->{
            savedFlights.addAll(fmDAO.getAllFlights());

        });

        adapter = new FlightRecyclerViewAdapter(this, savedFlights, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


}


package algonquin.cst2335.cst2335_finalproject;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.cst2335_finalproject.databinding.FlightInfoBinding;
import algonquin.cst2335.cst2335_finalproject.databinding.SavedFlightInfoBinding;

public class SavedFlightDetailsFragment extends Fragment{

    FlightModel flight;
    FlightModelDAO fmDAO;

    public SavedFlightDetailsFragment(FlightModel flight){
        this.flight = flight;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        SavedFlightInfoBinding binding = SavedFlightInfoBinding.inflate(inflater);

        FlightDatabase db = Room.databaseBuilder(getActivity(), FlightDatabase.class, "SavedFlights").build();
        fmDAO = db.fmDAO();

        binding.airportCode.setText(flight.getAirportCode());
        binding.destCode.setText(flight.getDestAirportCode());
        binding.delayFr.setText(String.format("%d min", flight.getDelay()));
        binding.departureFr.setText(flight.getDeparture());
        binding.terminalFr.setText("Terminal: " + flight.getTerminal());
        binding.gateFr.setText("Gate: "+ flight.getGate());
        binding.statusFr.setText(flight.getStatus());
        binding.destinationCity.setText(flight.getDestination());

        binding.deleteFlightButton.setOnClickListener(click -> {
            Executor thread = Executors.newSingleThreadExecutor();


            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Are you sure you want to delete this flight?")
                    .setTitle("Question:")
                    .setNegativeButton("No", (dialog, cl) -> {})
                    .setPositiveButton("Yes", (dialog, cl) -> {
                        thread.execute(() -> {
                            fmDAO.deleteFlight(flight);//add to database;
                            /*this runs in another thread*/
                        });
                        Snackbar.make(container, "Flight deleted...", Snackbar.LENGTH_LONG).setAction("Undo", clk -> {
                            thread.execute(() -> {
                                fmDAO.insertFlight(flight);//add to database;
                                /*this runs in another thread*/
                            });

                        }).show();
                    }).create().show();

        });

        return binding.getRoot();
    }
}

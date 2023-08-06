package algonquin.cst2335.cst2335_finalproject;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import androidx.fragment.app.Fragment;

import algonquin.cst2335.cst2335_finalproject.databinding.FlightInfoBinding;

public class FlightDetailsFragment extends Fragment {

    FlightModel flight;
    Button saveFlight;

    public FlightDetailsFragment(FlightModel flight){
        this.flight = flight;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        FlightInfoBinding binding = FlightInfoBinding.inflate(inflater);

        binding.airportCode.setText(flight.getAirportCode());
        binding.destCode.setText(flight.getDestAirportCode());
        binding.delayFr.setText(String.format("%d", flight.getDelay()));
        binding.departureFr.setText(flight.getDeparture());
        binding.terminalFr.setText(flight.getTerminal());
        binding.gateFr.setText(flight.getGate());
        binding.statusFr.setText(flight.getStatus());
        binding.destinationCity.setText(flight.getDestination());

        binding.saveFlightButton.setOnClickListener(click -> {

            Toast toast = Toast.makeText(getActivity(), "Saved flight!", Toast.LENGTH_LONG);
            toast.show();

        });

        return binding.getRoot();
    }
}

package algonquin.cst2335.cst2335_finalproject;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

/**
 * A fragment class representing the details view for a bear image.
 * This fragment displays UI elements and handles user interactions.
 * @author Daniel Stewart
 * @version 1.0
 */
public class BearImageDetailsFragment extends Fragment {

    /**
     * Called to create the UI for the fragment. Inflates the layout from the specified XML resource
     * and handles button click events.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The inflated view for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bear_fragment, container, false);

        // Handle button click
        Button enterButton = view.findViewById(R.id.enterButton);
        enterButton.setOnClickListener(v -> {
            // Remove the current fragment from the fragment manager
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .remove(BearImageDetailsFragment.this)
                    .commit();
        });

        return view;
    }
}









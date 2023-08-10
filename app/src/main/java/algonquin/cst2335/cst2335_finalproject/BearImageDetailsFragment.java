package algonquin.cst2335.cst2335_finalproject;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class BearImageDetailsFragment extends Fragment {

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








package algonquin.cst2335.cst2335_finalproject;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.cst2335_finalproject.databinding.CcyitemDetailBinding;


/**
 * CcyDetailsFragment is a Fragment subclass that displays the details of a selected currency conversion item.
 * This fragment is used to show the details of a specific currency conversion from the favorite list.
 * @author Huixin Xu
 * @version 1.0
 */
public class CcyDetailsFragment extends Fragment {

    CcyitemDetailBinding fragmentBinding;
    CcyListItem selectedItem;

    public CcyDetailsFragment(CcyListItem selecteditem){
        selectedItem = selecteditem;
    }


    /**
     * Called when the fragment's view is created. Inflates the fragment's layout and populates it
     * with the details of the selected currency conversion item.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The fragment's root view, containing the layout with the details of the selected currency conversion item.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        fragmentBinding = CcyitemDetailBinding.inflate(inflater);

        fragmentBinding.fromUnitInput.setText(selectedItem.getFromCcyUnit());
        fragmentBinding.fromAmount.setText("$ " + selectedItem.getFromCcyAmt());
        fragmentBinding.toUnitInput.setText(selectedItem.getConvertedCcyUnit());
        fragmentBinding.toAmount.setText("$ " + selectedItem.getConvertedCcyAmt());
        fragmentBinding.convertTime.setText(selectedItem.getTimeConverted());

        return fragmentBinding.getRoot();
    }
}

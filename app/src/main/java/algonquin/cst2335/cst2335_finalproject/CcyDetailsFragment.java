package algonquin.cst2335.cst2335_finalproject;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.cst2335_finalproject.databinding.CcyitemDetailBinding;


public class CcyDetailsFragment extends Fragment {

    CcyitemDetailBinding fragmentBinding;
    CcyListItem selectedItem;

    public CcyDetailsFragment(CcyListItem selecteditem){
        selectedItem = selecteditem;
    }


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

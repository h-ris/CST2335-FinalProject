package algonquin.cst2335.cst2335_finalproject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class CcyViewModel extends ViewModel {
    public MutableLiveData<ArrayList<CcyListItem>> ccyItemList = new MutableLiveData<>();

    public MutableLiveData<CcyListItem> selectedCcyItem = new MutableLiveData<>();
}

package algonquin.cst2335.cst2335_finalproject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class TriviaRoomViewModel extends ViewModel {

    public MutableLiveData<List<TriviaScore>> playerScores = new MutableLiveData<>();
    public MutableLiveData<TriviaScore> selectedScore = new MutableLiveData<>();
}

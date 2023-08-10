package algonquin.cst2335.cst2335_finalproject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

/**
 * ViewModel class for managing data related to trivia scores in a room.
 * This ViewModel provides MutableLiveData instances to observe a list of player scores
 * and a selected trivia score.
 */
public class TriviaRoomViewModel extends ViewModel {

    /**
     * LiveData representing a list of trivia scores for players.
     * Observers can use this LiveData to monitor changes to the list of scores.
     */
    public MutableLiveData<List<TriviaScore>> playerScores = new MutableLiveData<>();

    /**
     * LiveData representing a selected trivia score.
     * Observers can use this LiveData to monitor changes to the selected score.
     */
    public MutableLiveData<TriviaScore> selectedScore = new MutableLiveData<>();
}

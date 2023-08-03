package algonquin.cst2335.trivia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TriviaScoreDAO {

    @Insert
    long insertScore(TriviaScore ts);

    @Query("SELECT * FROM triviascore")
    List<TriviaScore> getAllScores();

    @Delete
    void deleteScore(TriviaScore ts);
}

package algonquin.cst2335.trivia;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Comparator;

@Entity(tableName = "triviascore")
public class TriviaScore implements Comparator<TriviaScore> {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "username")
    protected String userName;
    @ColumnInfo(name = "score")
    protected int score;

    public TriviaScore(){}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int score) {
        this.score = this.score + score;
    }

    public String getScoreString() {
        return score + "";
    }

//    @Override
    public int compare(TriviaScore t1, TriviaScore t2) {
        // Grater score comes first (return < 0).
        return t2.getScore() - t1.getScore();
    }
}

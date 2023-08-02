package algonquin.cst2335.trivia;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TriviaScore.class}, version = 1)
public abstract class TriviaScoreDatabase extends RoomDatabase {
    public abstract TriviaScoreDAO tsDAO();

    private static volatile TriviaScoreDatabase instance;

    public static synchronized TriviaScoreDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TriviaScoreDatabase.class, "trivia_score_database").build();
        }
        return instance;
    }
}

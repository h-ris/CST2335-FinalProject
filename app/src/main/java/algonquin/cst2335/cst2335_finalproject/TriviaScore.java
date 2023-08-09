package algonquin.cst2335.cst2335_finalproject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class TriviaScore implements Comparator<TriviaScore> {

    private int id;
    private String playerName;
    private int score;
    private LocalDate gameDate;

    public TriviaScore() {}

    public TriviaScore(int id, String playerName, int score, String gameDate) {
        this.id = id;
        this.playerName = playerName;
        this.score = score;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            this.gameDate = LocalDate.parse(gameDate, formatter);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LocalDate getGameDate() {
        return gameDate;
    }

    public void setGameDate(LocalDate gameDate) {
        this.gameDate = gameDate;
    }

    public String getScoreString() {
        return score + "";
    }

    public void addScore(int score) {
        this.score = this.score + score;
    }

    public String getGameDateString() {
        String dateString = gameDate.toString();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            dateString = gameDate.format(formatter);
        }
        return dateString;
    }

    @Override
    public int compare(TriviaScore t1, TriviaScore t2) {
        return t2.getScore() - t1.getScore();
    }
}

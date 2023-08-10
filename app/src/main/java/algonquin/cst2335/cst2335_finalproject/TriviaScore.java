package algonquin.cst2335.cst2335_finalproject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

/**
 * A class representing a trivia score, which includes the player's name, the score value,
 * and the date when the score was achieved.
 * This class also implements the Comparator interface to allow for score-based comparison.
 */
public class TriviaScore implements Comparator<TriviaScore> {

    private int id;
    private String playerName;
    private int score;
    private LocalDate gameDate;

    /**
     * Default constructor for TriviaScore.
     * Creates an empty TriviaScore object with default values.
     */
    public TriviaScore() {}

    /**
     * Constructor for TriviaScore with specified values.
     *
     * @param id         The unique identifier for the score.
     * @param playerName The name of the player.
     * @param score      The player's score.
     * @param gameDate   The date when the score was achieved as a string (in "yyyy-MM-dd" format).
     */
    public TriviaScore(int id, String playerName, int score, String gameDate) {
        this.id = id;
        this.playerName = playerName;
        this.score = score;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            this.gameDate = LocalDate.parse(gameDate, formatter);
        }
    }

    // Getters and setters for class properties...
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

    /**
     * Add a score to the current score value.
     *
     * @param score The score to be added.
     */
    public void addScore(int score) {
        this.score = this.score + score;
    }

    /**
     * Get the formatted game date string (in "MMM dd, yyyy" format).
     *
     * @return The formatted game date string.
     */
    public String getGameDateString() {
        String dateString = gameDate.toString();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            dateString = gameDate.format(formatter);
        }
        return dateString;
    }

    /**
     * Compare two TriviaScore objects based on their scores.
     *
     * @param t1 The first TriviaScore object.
     * @param t2 The second TriviaScore object.
     * @return A negative integer, zero, or a positive integer as the first score is less than,
     *         equal to, or greater than the second score.
     */
    @Override
    public int compare(TriviaScore t1, TriviaScore t2) {
        return t2.getScore() - t1.getScore();
    }
}

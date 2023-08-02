package algonquin.cst2335.trivia;

public class TriviaScore {
    private String userName;
    private int score;

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
}

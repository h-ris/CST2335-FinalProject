package algonquin.cst2335.trivia;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;

public class TriviaQuestion {

    private String category;
    private String type;
    private String difficulty;
    private String question;
    private String correctAnswer;
    private List<String> incorrectAnswers;

    public TriviaQuestion(JSONObject questionObject) throws JSONException, UnsupportedEncodingException {
        this.category = decode(questionObject.getString("category"));
        this.type = decode(questionObject.getString("type"));
        this.difficulty = decode(questionObject.getString("difficulty"));
        this.question = decode(questionObject.getString("question"));
        this.correctAnswer = decode(questionObject.getString("correct_answer"));
        this.incorrectAnswers = Arrays.asList(decode(questionObject.getString("incorrect_answers"))
                .replace("[\"", "").replace("\"]", "").split("\",\""));
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(List<String> incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    private String decode(String apiString) throws UnsupportedEncodingException {
        return URLDecoder.decode(apiString, "UTF-8");
    }
}

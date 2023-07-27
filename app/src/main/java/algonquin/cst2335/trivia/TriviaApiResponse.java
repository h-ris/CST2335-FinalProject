package algonquin.cst2335.trivia;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TriviaApiResponse {

    @SerializedName("response_code")
    private Integer responseCode;

    @SerializedName("results")
    private List<TriviaQuestion> questions;

//    public TriviaApiResponse() {
//        responseCode = -1;
//        questions = new ArrayList<>();
//    }

    public List<TriviaQuestion> getQuestions() {
        return questions;
    }

}

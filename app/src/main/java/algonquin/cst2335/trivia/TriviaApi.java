package algonquin.cst2335.trivia;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TriviaApi {

    @GET("https://opentdb.com/api.php?amount=10&type=multiple&encode=url3986")
    Call<TriviaApiResponse> getTriviaQuestions();
}

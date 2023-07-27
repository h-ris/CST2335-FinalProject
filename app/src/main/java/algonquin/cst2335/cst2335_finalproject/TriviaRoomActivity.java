package algonquin.cst2335.cst2335_finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import algonquin.cst2335.cst2335_finalproject.databinding.ActivityTriviaRoomBinding;
import algonquin.cst2335.trivia.RetrofitTrivia;
import algonquin.cst2335.trivia.TriviaApi;
import algonquin.cst2335.trivia.TriviaApiResponse;
import algonquin.cst2335.trivia.TriviaQuestion;
import algonquin.cst2335.trivia.TriviaScore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TriviaRoomActivity extends AppCompatActivity {

    ActivityTriviaRoomBinding binding;
    private TriviaApi triviaApi;
    private AlertDialog gameDialog;
    private RadioGroup rgAnswers;
    private TextView tvQuestion;
    private RadioButton rbOptionA;
    private RadioButton rbOptionB;
    private RadioButton rbOptionC;
    private RadioButton rbOptionD;
    private List<TriviaQuestion> apiQuestions;
    private TriviaQuestion currentQuestion;

    private int questionIndex;
    private TriviaScore currentScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTriviaRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadQuestions();
        buildGameFragment();

        binding.top10List.setAdapter(new RecyclerView.Adapter<ScoreHolder>() {
            @NonNull
            @Override
            public ScoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull ScoreHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        });

        Button btnTriviaStart = findViewById(R.id.btnTriviaStart);
        btnTriviaStart.setOnClickListener(v -> {
            questionIndex = 0;
            currentScore = new TriviaScore();
            try {
                loadTriviaQuestion();
            } catch (UnsupportedEncodingException e) {
                Toast.makeText(this, "Unable to decode trivia question.", Toast.LENGTH_SHORT).show();
            }
            gameDialog.show();
        });

    }

    private void buildGameFragment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View gameView = getLayoutInflater().inflate(R.layout.fragment_trivia_game, null);
        builder.setView(gameView);

        rgAnswers = gameView.findViewById(R.id.rgAnswers);
        tvQuestion = gameView.findViewById(R.id.tvQuestion);
        rbOptionA = gameView.findViewById(R.id.rbOptionA);
        rbOptionB = gameView.findViewById(R.id.rbOptionB);
        rbOptionC = gameView.findViewById(R.id.rbOptionC);
        rbOptionD = gameView.findViewById(R.id.rbOptionD);

        gameDialog = builder.create();

        Button next = gameView.findViewById(R.id.btnTriviaNext);
        next.setOnClickListener(view -> {
            RadioButton userAnswer = (RadioButton) rgAnswers.getChildAt(rgAnswers.getCheckedRadioButtonId());

            //TODO: Fix selected radio button.
            if (userAnswer.getText().equals(currentQuestion.getCorrectAnswer())) {
                currentScore.addScore(10);
            }
            if (questionIndex < apiQuestions.size()) {
                questionIndex++;
                try {
                    loadTriviaQuestion();
                } catch (UnsupportedEncodingException e) {
                    Toast.makeText(this, "Unable to decode trivia question.", Toast.LENGTH_SHORT).show();
                }
            } else {
                //TODO: Create fragment to save score and username.
                Toast.makeText(this, "SCORE: " + currentScore.getScore(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTriviaQuestion() throws UnsupportedEncodingException {
        List<RadioButton> optionButtons = new ArrayList<>();
        optionButtons.add(rbOptionA);
        optionButtons.add(rbOptionB);
        optionButtons.add(rbOptionC);
        optionButtons.add(rbOptionD);
        // TODO: Extract encode method.
       currentQuestion = apiQuestions.get(questionIndex);
       tvQuestion.setText(java.net.URLDecoder.decode(currentQuestion.getQuestion(), "UTF-8"));

       Random random = new Random();

       for (int i = optionButtons.size() - 1; i > 0; i--) {
           int randomIndex = random.nextInt(optionButtons.size());
           optionButtons.get(randomIndex)
                   .setText(java.net.URLDecoder.decode(currentQuestion.getIncorrectAnswers().get(i - 1), "UTF-8"));
           optionButtons.remove(randomIndex);
       }
       optionButtons.get(0).setText(java.net.URLDecoder.decode(currentQuestion.getCorrectAnswer(), "UTF-8"));
    }

    private void loadQuestions() {
        triviaApi = RetrofitTrivia.getRetrofitInstance().create(TriviaApi.class);

        Call<TriviaApiResponse> call = triviaApi.getTriviaQuestions();
        call.enqueue(new Callback<TriviaApiResponse>() {
            @Override
            public void onResponse(Call<TriviaApiResponse> call, Response<TriviaApiResponse> response) {
                if (response.isSuccessful()) {
                    TriviaApiResponse triviaResponse = response.body();
                    if (triviaResponse != null) {
                        apiQuestions = triviaResponse.getQuestions();
                    }
                } else {
                    Toast.makeText(TriviaRoomActivity.this, "The force is not with you! Please check your Jedi internet connection.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TriviaApiResponse> call, Throwable t) {
                Toast.makeText(TriviaRoomActivity.this, "The Dark side is too strong, you loose!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class ScoreHolder extends RecyclerView.ViewHolder {
        public ScoreHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
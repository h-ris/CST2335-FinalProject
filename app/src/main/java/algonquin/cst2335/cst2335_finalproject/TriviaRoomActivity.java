package algonquin.cst2335.cst2335_finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import algonquin.cst2335.cst2335_finalproject.databinding.ActivityTriviaRoomBinding;
import algonquin.cst2335.trivia.ScoreAdapter;
import algonquin.cst2335.trivia.TriviaQuestion;
import algonquin.cst2335.trivia.TriviaScore;

public class TriviaRoomActivity extends AppCompatActivity {

    private static final String API_URL = "https://opentdb.com/api.php?amount=10&type=multiple&encode=url3986";
    ActivityTriviaRoomBinding binding;
    private AlertDialog gameDialog;
    private AlertDialog scoreDialog;
    private RadioGroup rgAnswers;
    private TextView tvQuestion;
    private RadioButton rbOptionA;
    private RadioButton rbOptionB;
    private RadioButton rbOptionC;
    private RadioButton rbOptionD;
    private RadioButton userAnswer;
    private List<TriviaQuestion> apiQuestions;
    private TriviaQuestion currentQuestion;
    private int questionIndex;
    private TriviaScore currentScore;
    private TextView tvUserScore;
    private EditText etUsername;
    private TextView tvNoScore;
    private Button btnDone;
    private List<TriviaScore> top10;
    private ScoreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTriviaRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // TODO load top 10 from DB
        loadTop10list();

        loadApiQuestions();
        buildGameDialog();
        buildScoreDialog();

        adapter = new ScoreAdapter(top10);
        binding.top10List.setHasFixedSize(true);
        binding.top10List.setLayoutManager(new LinearLayoutManager(this));
        binding.top10List.setAdapter(adapter);

        binding.btnTriviaStart.setOnClickListener(v -> {
            questionIndex = 0;
            currentScore = new TriviaScore();
            loadTriviaQuestion();
            gameDialog.show();
        });
    }

    private void loadTop10list() {
        // load from DB then one by one
        top10 = new ArrayList<>();
    }

    /**
     * Method to load the Trivia API questions from the internet using JasonObjectRequest.
     */
    private void loadApiQuestions() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, API_URL, null,
                response -> {
                    apiQuestions = new ArrayList<>();

                    try {
                        JSONArray results = response.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject questionObject = results.getJSONObject(i);
                            apiQuestions.add(new TriviaQuestion(questionObject));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                        Toast.makeText(TriviaRoomActivity.this, "The force is not with you! Please check your Jedi internet connection.", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    /**
     * Method that builds the User Score entry dialog that shows the user latest score and
     * has an EditText component for the username input.
     */
    private void buildScoreDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View scoreView = getLayoutInflater().inflate(R.layout.new_score, null);
        builder.setView(scoreView);

        tvUserScore = scoreView.findViewById(R.id.userScore);
        etUsername = scoreView.findViewById(R.id.userName);
        tvNoScore = scoreView.findViewById(R.id.sorryScore);
        btnDone = scoreView.findViewById(R.id.btnDone);

        btnDone.setOnClickListener(view -> {
            if (!etUsername.getText().toString().trim().equals("")) {
                currentScore.setUserName(etUsername.getText().toString());
                top10.add(currentScore);
                // TODO add comparable to score and order top10 list
                adapter.notifyDataSetChanged();
            }
            etUsername.setText("");
            scoreDialog.dismiss();
            loadApiQuestions();
        });

        scoreDialog = builder.create();
    }

    /**
     * Method that builds the Trivia Game dialog with a question TextView, 4 answers as
     * Radio Buttons, and a Next button to load the next question.
     */
    private void buildGameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View gameView = getLayoutInflater().inflate(R.layout.trivia_game, null);
        builder.setView(gameView);

        rgAnswers = gameView.findViewById(R.id.rgAnswers);
        tvQuestion = gameView.findViewById(R.id.tvQuestion);
        rbOptionA = gameView.findViewById(R.id.rbOptionA);
        rbOptionB = gameView.findViewById(R.id.rbOptionB);
        rbOptionC = gameView.findViewById(R.id.rbOptionC);
        rbOptionD = gameView.findViewById(R.id.rbOptionD);

        rgAnswers.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            userAnswer = gameView.findViewById(checkedId);
        });

        // TODO test with no options selected
        Button next = gameView.findViewById(R.id.btnTriviaNext);
        next.setOnClickListener(view -> {
            int rawId = 0;
            if (userAnswer.getText().equals(currentQuestion.getCorrectAnswer())) {
                currentScore.addScore(10);
                rawId = R.raw.right;
            } else {
                rawId = R.raw.wrong;
            }

            final MediaPlayer mp = MediaPlayer.create(this, rawId);
            mp.start();

            if (questionIndex < 2){//apiQuestions.size()) {
                questionIndex++;
                loadTriviaQuestion();
            } else {
                if (currentScore.getScore() == 0) {
                    etUsername.setVisibility(View.GONE);
                    tvNoScore.setVisibility(View.VISIBLE);
                } else {
                    etUsername.setVisibility(View.VISIBLE);
                    tvNoScore.setVisibility(View.GONE);
                }
                gameDialog.hide();
                tvUserScore.setText(currentScore.getScoreString());
                scoreDialog.show();
            }
        });
        gameDialog = builder.create();
    }

    /**
     * Method that loads the next Trivia Question on the Game Dialog, with the answers randomly
     * placed on the Radio Buttons.
     */
    private void loadTriviaQuestion() {
        rgAnswers.clearCheck();
        List<RadioButton> optionButtons = new ArrayList<>();
        optionButtons.add(rbOptionA);
        optionButtons.add(rbOptionB);
        optionButtons.add(rbOptionC);
        optionButtons.add(rbOptionD);

        currentQuestion = apiQuestions.get(questionIndex);
        tvQuestion.setText(currentQuestion.getQuestion());

        Random random = new Random();

        for (int i = optionButtons.size() - 1; i > 0; i--) {
           int randomIndex = random.nextInt(optionButtons.size());
           optionButtons.get(randomIndex).setText(currentQuestion.getIncorrectAnswers().get(i - 1));
           optionButtons.remove(randomIndex);
        }
        optionButtons.get(0).setText(currentQuestion.getCorrectAnswer());
    }
}
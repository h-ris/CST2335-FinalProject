package algonquin.cst2335.cst2335_finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import algonquin.cst2335.cst2335_finalproject.databinding.ActivityTriviaRoomBinding;

/**
 * Trivia Room Main Activity class that loads a Trivia Game with questions
 * from an API and shows top 10 scores list.
 *
 * @author Andre Azevedo
 */
public class TriviaRoomActivity extends AppCompatActivity {

    private static final String API_URL = "https://opentdb.com/api.php?amount=10&difficulty=easy&type=multiple&encode=url3986";
    public static final int TOP_10_SIZE = 10;
    private ActivityTriviaRoomBinding binding;
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
    private EditText etPlayerName;
    private TextView tvNoScore;
    private List<TriviaScore> top10;
    private ScoreAdapter adapter;
    private TriviaDatabase triviaDatabase;
    private SharedPreferences sp;
    private String savedPlayerName;
    private TriviaRoomViewModel tvModel;

    /**
     * The onCreate method for the TriviaRoomActivity will create the view and also run the settings
     * for the trivia game. In here we initialize the database and the ViewModel, creating the connection
     * with the score adaptor, will load the trivia questions from the API on load, and also after each
     * round for new questions, and the game and score dialogs will be created and ready to run.
     *
     * @param savedInstanceState The saved instance state for the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTriviaRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbInit();
        loadPreferences();
        loadApiQuestions();
        buildGameDialog();
        buildScoreDialog();

        adapter = new ScoreAdapter(tvModel);
        binding.top10List.setHasFixedSize(true);
        binding.top10List.setLayoutManager(new LinearLayoutManager(this));
        binding.top10List.setAdapter(adapter);

        binding.btnTriviaStart.setOnClickListener(v -> {
            questionIndex = 0;
            currentScore = new TriviaScore();
            loadTriviaQuestion();
            gameDialog.show();
        });

        binding.btnTriviaInfo.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(TriviaRoomActivity.this);
            builder.setTitle(getString(R.string.trivia_instruc));
            builder.setMessage(getString(R.string.trivia_instruc_info));
            builder.setPositiveButton(getString(R.string.ok), (dialog, cl) -> {});
            builder.create().show();
        });

        tvModel.selectedScore.observe(this, (scoreValue) -> {
            if (scoreValue != null) {
                TriviaScoreDetails scoreFrag = TriviaScoreDetails.newInstance(tvModel, adapter, triviaDatabase);

                FragmentManager fMgr = getSupportFragmentManager();
                FragmentTransaction tx = fMgr.beginTransaction();

                tx.add(R.id.triviaFrame, scoreFrag);
                tx.addToBackStack("Score Detail");
                tx.commit();
            }
        });
    }

    /**
     * Method to initialize the database connection and load the Top10 list.
     */
    private void dbInit() {
        triviaDatabase = new TriviaDatabase( this);
        top10 = triviaDatabase.getAllTriviaScores();

        tvModel = new ViewModelProvider(this).get(TriviaRoomViewModel.class);
        tvModel.playerScores.setValue(top10);

//        addFakeData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            top10.sort(new TriviaScore());
        }
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
        etPlayerName = scoreView.findViewById(R.id.playerName);
        tvNoScore = scoreView.findViewById(R.id.sorryScore);

        if (!savedPlayerName.equals("")) {
            etPlayerName.setText(savedPlayerName);
            etPlayerName.setSelectAllOnFocus(true);
        }

        Button btnDone = scoreView.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(view -> {
            if (!etPlayerName.getText().toString().trim().equals("")) {
                currentScore.setPlayerName(etPlayerName.getText().toString());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    currentScore.setGameDate(LocalDate.now());
                }
                savedPlayerName = currentScore.getPlayerName();
                savePreference("playername", currentScore.getPlayerName());
                addNewScore();
            }
            etPlayerName.setText("");
            scoreDialog.dismiss();
            loadApiQuestions();
            betterLuckSnack();
            Toast.makeText(TriviaRoomActivity.this,
                    getString(R.string.new_questions), Toast.LENGTH_SHORT).show();
        });

        scoreDialog = builder.create();
    }

    /**
     * Method that analyses the current user score and if applicable adds it to the Top 10 list.
     */
    private void addNewScore() {
        if (top10.size() == TOP_10_SIZE) {
            TriviaScore lastPlace = top10.get(top10.size() - 1);

            if (currentScore.getScore() > lastPlace.getScore()) {
                triviaDatabase.deleteTriviaScore(top10.get(top10.size() - 1));
                top10.remove(top10.size() - 1);
                top10.add(currentScore);
                triviaDatabase.insertTriviaScore(currentScore);
            }
        } else {
            top10.add(currentScore);
            triviaDatabase.insertTriviaScore(currentScore);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            top10.sort(new TriviaScore());
        }
        adapter.notifyDataSetChanged();
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

        Button next = gameView.findViewById(R.id.btnTriviaNext);
        next.setOnClickListener(view -> {
            int rawId = 0;
            if (userAnswer != null && userAnswer.getText().equals(currentQuestion.getCorrectAnswer())) {
                currentScore.addScore(10);
                rawId = R.raw.right;
            } else {
                rawId = R.raw.wrong;
            }

            final MediaPlayer mp = MediaPlayer.create(this, rawId);
            mp.start();

            if (questionIndex < apiQuestions.size() - 1) {
                questionIndex++;
                loadTriviaQuestion();
            } else {
                if (!scoreIsTop10()) {
                    etPlayerName.setVisibility(View.GONE);
                    tvNoScore.setVisibility(View.VISIBLE);
                } else {
                    etPlayerName.setVisibility(View.VISIBLE);
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

        // Toast for correct answer for testing purposes
//        Toast.makeText(this, "" + currentQuestion.getCorrectAnswer(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Method that shows a Snackbar message at the top of the screen if the player didn't get on
     * the Top 10 list.
     */
    private void betterLuckSnack() {
        if (!top10.contains(currentScore)) {
            Snackbar snack = Snackbar.make(binding.gameLayout, getString(R.string.better_luck), Snackbar.LENGTH_LONG);
            View snackView = snack.getView();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackView.getLayoutParams();
            params.gravity = Gravity.TOP;
            snackView.setLayoutParams(params);
            snack.show();
        }
    }

    /**
     * Method that loads user Shared Preferences and loads it to the Trivia Game.
     */
    private void loadPreferences() {
        sp = getSharedPreferences("TriviaData", Context.MODE_PRIVATE);
        savedPlayerName = sp.getString("playername","");
    }

    /**
     * Method that saves the specified user Shared Preference.
     *
     * @param prefName The Shared Preference name.
     * @param prefValue The Shared Preference value to be saved.
     */
    private void savePreference(String prefName, String prefValue) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(prefName, prefValue);
        editor.apply();
    }

    /**
     * Method that checks the current user score to see if it is a Top 10 score or not.
     *
     * @return True if the current score is a Top 10 score, False otherwise.
     */
    private boolean scoreIsTop10() {
        boolean top10score = false;

        if (currentScore.getScore() != 0) {
            if (top10.size() == TOP_10_SIZE) {
                TriviaScore lastPlace = top10.get(top10.size() - 1);

                if (currentScore.getScore() > lastPlace.getScore()) {
                    top10score = true;
                }
            } else {
                top10score = true;
            }
        }
        return top10score;
    }

    /**
     * Loading placeholder data for testing purposes.
     */
    private void addFakeData() {
        TriviaScore ts;
        for (int i = 0; i < 10; i++) {
            ts = new TriviaScore();
            ts.setPlayerName("Myname" + (i + 1));
            ts.setScore(10);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ts.setGameDate(LocalDate.now());
            }
            top10.add(ts);
            triviaDatabase.insertTriviaScore(ts);
        }
    }

    /**
     * OnDestroy will also close the database.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        triviaDatabase.close();
    }
}
package algonquin.cst2335.cst2335_finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import algonquin.cst2335.cst2335_finalproject.databinding.ActivityMainBinding;

/**
 * Main Activity for the Final Project of the Course CST2335 Mobile Application Programming.
 * The Project includes an Aviation Flight Tracker session, a Bear Image Generator session,
 * a Currency Converter session and a Trivia Game session.
 *
 * @author Andre Azevedo
 * @author Daniel Stewart
 * @author Stefan Stivicic
 * @author Huixin Xu
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonTrivia.setOnClickListener(click -> {
            Intent toTriviaRoom = new Intent(this, TriviaRoomActivity.class);
            startActivity(toTriviaRoom);
        });

        //button for Bear Image Generator
        binding.buttonBear.setOnClickListener(click -> {
            Intent toBearImageGen  = new Intent(this, BearImageMainActivity.class);
            startActivity(toBearImageGen);

        });

        //button for Aviation Stack Flight Tracker
        binding.buttonFlight.setOnClickListener(click -> {
            Intent toFlightTracker = new Intent(this, FlightTracker.class);
            startActivity(toFlightTracker);
        });

        //button for Currency Converter
        binding.buttonCurrency.setOnClickListener(click -> {
            Intent toCcyConverter = new Intent(this, CcyConverterMainActivity.class);
            startActivity(toCcyConverter);
        });

        binding.btnCredits.setOnClickListener(click -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getString(R.string.credits));
            builder.setMessage(getString(R.string.credits_info));
            builder.setPositiveButton(getString(R.string.ok), (dialog, cl) -> {});
            builder.create().show();
        });

        binding.btnLanguage.setOnClickListener(click -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getString(R.string.languages));
            builder.setMessage(getString(R.string.lang_info));
            builder.setPositiveButton(getString(R.string.ok), (dialog, cl) -> {});
            builder.create().show();
        });
    }
}
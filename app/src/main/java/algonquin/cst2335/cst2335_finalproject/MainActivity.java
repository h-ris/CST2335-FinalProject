package algonquin.cst2335.cst2335_finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import algonquin.cst2335.cst2335_finalproject.databinding.ActivityMainBinding;

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
//        binding.button3.setOnClickListener(click -> {
//
//        });

        //button for Currency Converter
        binding.buttonCurrency.setOnClickListener(click -> {
            Intent toCcyConverter = new Intent(this, CcyConverterMainActivity.class);
            startActivity(toCcyConverter);
        });

    }
}
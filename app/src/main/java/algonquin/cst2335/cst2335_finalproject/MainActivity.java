package algonquin.cst2335.cst2335_finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

//import algonquin.cst2335.cst2335_finalproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    Button button;
    Button button2;
    Button button3;
    Button button4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);


        //button for Trivia Question Database
//        binding.button.setOnClickListener(click -> {
//
//        });
//
        //button for Bear Image Generator
        button2.setOnClickListener(click -> {
            Intent toBearGenerator = new Intent(this, BearImageMainActivity.class);
            startActivity(toBearGenerator);
        });

        //button for Aviation Stack Flight Tracker
//        binding.button3.setOnClickListener(click -> {
//
//        });

        //button for Currency Converter
        button4.setOnClickListener(click -> {
            Intent toCcyConverter = new Intent(this, CcyConverterMainActivity.class);
            startActivity(toCcyConverter);
        });
    }
}
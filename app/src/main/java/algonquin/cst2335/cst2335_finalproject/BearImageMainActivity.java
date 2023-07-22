package algonquin.cst2335.cst2335_finalproject;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class BearImageMainActivity extends AppCompatActivity {

    TextView bearText;
    EditText width;
    EditText height;
    Button generateButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bearimagegenerator_main);

        bearText = findViewById(R.id.BearText);
        width = findViewById(R.id.width);
        height = findViewById(R.id.height);
        generateButton = findViewById(R.id.generateButton);

        generateButton.setOnClickListener(clk -> {
//            if (width.Integer.parseInt)


        });


    }

}

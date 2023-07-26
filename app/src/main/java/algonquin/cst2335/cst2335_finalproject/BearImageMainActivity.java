package algonquin.cst2335.cst2335_finalproject;


import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;




public class BearImageMainActivity extends AppCompatActivity {

//    BearImageGeneratorBinding binding;
    TextView bearText;
    EditText width;
    EditText height;
    Button generateButton;

    Spinner helpMenu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bearimagegenerator_main);


        helpMenu = findViewById(R.id.helpMenu);
        bearText = findViewById(R.id.BearText);
        width = findViewById(R.id.width);
        height = findViewById(R.id.height);
        generateButton = findViewById(R.id.generateButton);

        String widthInput = width.getText().toString();
        String heightInput = height.getText().toString();


        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.helparray, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        helpMenu.setAdapter(arrayAdapter);




        generateButton.setOnClickListener(clk -> {
            if(!widthInput.isEmpty() || !heightInput.isEmpty()){
                int widthValue = Integer.parseInt(widthInput);
                int heightValue = Integer.parseInt(heightInput);
                String url = "https://placebear.com/" + widthValue + "/" + heightValue;
            }
            else {
                Toast.makeText(BearImageMainActivity.this, "Both width and height cannot be null", Toast.LENGTH_SHORT).show();
            }


        });


    }








}

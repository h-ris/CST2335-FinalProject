package algonquin.cst2335.cst2335_finalproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class CcyConverterMainActivity extends AppCompatActivity {
    private Button convertBtn;
    private EditText amtTxt;
    private TextView resultAmt;
    private EditText sourceTxt;
    private EditText destinationTxt;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currencyconverter_main);

        amtTxt = findViewById(R.id.amtTxt);
        sourceTxt = findViewById(R.id.sourceTxt);
        destinationTxt = findViewById(R.id.destinationTxt);
        convertBtn = findViewById(R.id.convertBtn);
        resultAmt = findViewById(R.id.resultAmt);

        requestQueue = Volley.newRequestQueue(this);

        convertBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String amount = amtTxt.getText().toString().trim();
                String sourceCcy = sourceTxt.getText().toString().trim().toUpperCase();
                String destinationCcy = destinationTxt.getText().toString().trim().toUpperCase();

                if (!amount.isEmpty() && !sourceCcy.isEmpty() && !destinationCcy.isEmpty()) {
                    String url = "https://currency-converter5.p.rapidapi.com/currency/convert?format=json&from="
                            + sourceCcy + "&to=" + destinationCcy + "&amount=" + amount;

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            response -> {
                                try {
                                    double convertedAmount = response.getDouble("amount");
                                    resultAmt.setText(String.valueOf(convertedAmount));
                                } catch (JSONException e) {
                                    Toast.makeText(CcyConverterMainActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                                }
                            },
                            error -> {
                                Toast.makeText(CcyConverterMainActivity.this, "Failed to convert currency", Toast.LENGTH_SHORT).show();
                                Log.e("VolleyError", "Error: " + error.getMessage());
                            });

                    requestQueue.add(jsonObjectRequest);

                } else {
                    Toast.makeText(CcyConverterMainActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}

package algonquin.cst2335.cst2335_finalproject;


import static algonquin.cst2335.cst2335_finalproject.R.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.cst2335_finalproject.databinding.BearImageViewBinding;
import algonquin.cst2335.cst2335_finalproject.databinding.BearimagegeneratorMainBinding;


public class BearImageMainActivity extends AppCompatActivity {

    BearimagegeneratorMainBinding binding;
    BearImageViewBinding imageBinding;

    Toolbar toolbar;
    EditText width;
    EditText height;
    Button generateButton;


    private RecyclerView recyclerView;
    private ArrayList<String> stringArrayList;
    private BearRecyclerViewAdapter recyclerViewAdapter;

    private static final int REQUEST_CODE_DISPLAY_IMAGE = 101;

    private static final String PREF_WELCOME_SHOWN = "welcomeShown";
    private static final String PREFS_NAME = "MyPrefs";
    private SharedPreferences sharedPreferences;


    /**
     * OnCreate for Options menu
     * @param menu The options menu in which you place your items.
     *
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.bear_menu, menu);
        return true;
    }

    /**
     * Finds out when an item is selected
     * @param item The menu item that was selected.
     *
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.menu_help){
            showHelpDialog();
            return true;
        }else if (id == R.id.menu_saved_images) {
            openSavedImagesActivity();
            return true;
        }else if (id == R.id.menu_show_welcome) {
            openWelcomeFragment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * AlertDialog for when help menu option is selected
     */
    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Instructions");
        builder.setMessage("Enter Width on left, Height on right, then press the generate button");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private BearImageDatabaseHelper databaseHelper;
    private void loadSavedImagesFromDatabase() {
        ArrayList<String> savedImageUrls = databaseHelper.getAllImageUrls();
        stringArrayList.clear();
        if (!savedImageUrls.isEmpty()) {
            String lastSavedImageUrl = savedImageUrls.get(savedImageUrls.size() - 1);
            stringArrayList.add(lastSavedImageUrl);
        }
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void insertImageUrlToDatabase(String imageUrl) {
        databaseHelper.insertImageUrl(imageUrl);
    }



    /**
     * OnCreate method for main activity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bearimagegenerator_main);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isFirstLaunch = sharedPreferences.getBoolean(PREF_WELCOME_SHOWN, true);

        if (isFirstLaunch) {
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, new BearImageDetailsFragment())
                    .commit();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(PREF_WELCOME_SHOWN, false);
            editor.apply();
        }

        toolbar = findViewById(id.toolbar);
        width = findViewById(id.width);
        height = findViewById(id.height);
        generateButton = findViewById(id.generateButton);
        recyclerView = findViewById(id.recycleView);


        stringArrayList = new ArrayList<>();

        recyclerViewAdapter = new BearRecyclerViewAdapter(this, stringArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);

        databaseHelper = new BearImageDatabaseHelper(this);
        databaseHelper.getWritableDatabase();
        loadSavedImagesFromDatabase();

        generateButton.setOnClickListener(clk -> {

            String widthInput = width.getText().toString();
            String heightInput = height.getText().toString();


            if (widthInput.isEmpty() && heightInput.isEmpty()) {
                Toast.makeText(BearImageMainActivity.this, "Both width and height cannot be null", Toast.LENGTH_SHORT).show();
            } else {
                stringArrayList.clear();
                int widthValue = Integer.parseInt(widthInput);
                int heightValue = Integer.parseInt(heightInput);
                String url = "https://placebear.com/" + widthValue + "/" + heightValue + ".jpg";
                stringArrayList.add(url);
                recyclerViewAdapter.notifyDataSetChanged();


                // Insert the image URL to the database
                insertImageUrlToDatabase(url);

                // Load the last saved image URL from the database after inserting
                loadSavedImagesFromDatabase();

            }


        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("MainActivity", "onActivityResult - RequestCode: " + requestCode + ", ResultCode: " + resultCode);

        if (requestCode == REQUEST_CODE_DISPLAY_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            // Get the selected image URL from the SavedImagesActivity
            String selectedImageUrl = data.getStringExtra("selectedImageUrl");

            // Add the selected image URL to the RecyclerView
            stringArrayList.clear();
            stringArrayList.add(selectedImageUrl);
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database when the activity is destroyed
        databaseHelper.close();
    }

    private void openSavedImagesActivity() {
        Intent intent = new Intent(this, SavedImagesActivity.class);
        startActivityForResult(intent,REQUEST_CODE_DISPLAY_IMAGE);
    }

    private void openWelcomeFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new BearImageDetailsFragment())
                .commit();

        // Reset the flag in shared preferences to show the welcome fragment again
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_WELCOME_SHOWN, true);
        editor.apply();
    }


}








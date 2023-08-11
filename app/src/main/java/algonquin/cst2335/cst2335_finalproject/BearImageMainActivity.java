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

/**
 * The main activity class for the Bear Image Generator application. This class handles user interactions
 * and manages the UI components for generating and displaying bear images.
 * @author Daniel Stewart
 * @version 1.0
 */
public class BearImageMainActivity extends AppCompatActivity {

    // Variables for UI components
    Toolbar toolbar;
    EditText width;
    EditText height;
    Button generateButton;


    private RecyclerView recyclerView;
    private ArrayList<String> stringArrayList;
    private BearRecyclerViewAdapter recyclerViewAdapter;

    // Request code for startActivityForResult
    private static final int REQUEST_CODE_DISPLAY_IMAGE = 101;

    // Shared preferences keys
    private static final String PREF_WELCOME_SHOWN = "welcomeShown";
    private static final String PREFS_NAME = "MyPrefs";
    private SharedPreferences sharedPreferences;


    /**
     * Called to create the options menu in the activity's action bar.
     *
     * @param menu The options menu in which you place your items.
     * @return Returns true for the menu to be displayed, false otherwise.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.bear_menu, menu);
        return true;
    }

    /**
     * Called when a menu item is selected.
     *
     * @param item The menu item that was selected.
     * @return Returns true to consume the event here or false to allow normal menu processing to proceed.
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
     * Called to create and show an AlertDialog for displaying help instructions.
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
     * Called when the activity is being created. Initializes UI components and handles user interactions.
     *
     * @param savedInstanceState If the activity is being re-initialized after being shut down then this Bundle contains the data it most recently supplied
     *                           in {@link #onSaveInstanceState}. Otherwise, it is null.
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

        // Initialize UI components
        toolbar = findViewById(id.toolbar);
        width = findViewById(id.width);
        height = findViewById(id.height);
        generateButton = findViewById(id.generateButton);
        recyclerView = findViewById(id.recycleView);


        stringArrayList = new ArrayList<>();

        recyclerViewAdapter = new BearRecyclerViewAdapter(this, stringArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);

        // Initialize database helper and load saved images
        databaseHelper = new BearImageDatabaseHelper(this);
        databaseHelper.getWritableDatabase();
        loadSavedImagesFromDatabase();

        // Set click listener for the generate button
        generateButton.setOnClickListener(clk -> {

            String widthInput = width.getText().toString();
            String heightInput = height.getText().toString();


            if (widthInput.isEmpty() || heightInput.isEmpty()) {
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


    /**
     * Called when the activity returns a result from another activity started by startActivityForResult.
     *
     * @param requestCode The request code passed to startActivityForResult.
     * @param resultCode The result code returned by the child activity.
     * @param data The intent that carries the result data.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("MainActivity", "onActivityResult - RequestCode: " + requestCode + ", ResultCode: " + resultCode);

        // Handle the result data
        if (requestCode == REQUEST_CODE_DISPLAY_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            // Get the selected image URL from the SavedImagesActivity
            String selectedImageUrl = data.getStringExtra("selectedImageUrl");

            // Add the selected image URL to the RecyclerView
            stringArrayList.clear();
            stringArrayList.add(selectedImageUrl);
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Called when the activity is being destroyed. Closes the database connection.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database when the activity is destroyed
        databaseHelper.close();
    }

    /**
     * Opens the SavedImagesActivity to display a list of saved bear image URLs.
     */
    private void openSavedImagesActivity() {
        // Create an intent to start the SavedImagesActivity
        Intent intent = new Intent(this, SavedImagesActivity.class);
        // Start the activity and expect a result from it
        startActivityForResult(intent, REQUEST_CODE_DISPLAY_IMAGE);
    }

    /**
     * Opens the BearImageDetailsFragment to display a welcome message.
     * Resets the "welcome shown" flag in shared preferences after displaying the fragment.
     */
    private void openWelcomeFragment() {
        // Replace the content view with the BearImageDetailsFragment
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new BearImageDetailsFragment())
                .commit();

        // Reset the flag in shared preferences to show the welcome fragment again
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_WELCOME_SHOWN, true);
        editor.apply();
    }



}








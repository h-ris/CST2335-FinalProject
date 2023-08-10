package algonquin.cst2335.cst2335_finalproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageRequest;

import java.util.ArrayList;

/**
 * The activity class responsible for displaying saved bear image URLs. This class handles displaying
 * the list of saved images, item click events, and options for deleting or displaying images.
 * @author Daniel Stewart
 * @version 1.0
 */
public class SavedImagesActivity extends AppCompatActivity implements BearRecyclerViewAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private ArrayList<String> savedImageUrls;
    private BearRecyclerViewAdapter recyclerViewAdapter;
    private BearImageDatabaseHelper databaseHelper;

    /**
     * Called when the activity is being created. Initializes UI components and handles loading saved images from the database.
     *
     * @param savedInstanceState If the activity is being re-initialized after being shut down then this Bundle contains the data it most recently supplied in {@link #onSaveInstanceState}. Otherwise, it is null.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_images);

        recyclerView = findViewById(R.id.savedImagesRecyclerView);
        savedImageUrls = new ArrayList<>();
        recyclerViewAdapter = new BearRecyclerViewAdapter(this, savedImageUrls);

        // Use a vertical LinearLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setItemClickListener(this);

        databaseHelper = new BearImageDatabaseHelper(this);

        loadSavedImagesFromDatabase();
    }

    /**
     * Loads saved bear image URLs from the database and updates the RecyclerView adapter's data.
     */
    private void loadSavedImagesFromDatabase() {
        ArrayList<String> savedImageUrls = databaseHelper.getAllImageUrls();
        recyclerViewAdapter.updateData(savedImageUrls);
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
     * Handles item click events in the RecyclerView. Displays options for deleting or displaying images.
     *
     * @param position The position of the clicked item in the RecyclerView.
     */
    @Override
    public void onItemClick(int position) {
        Log.d("SavedImagesActivity", "Item clicked at position: " + position);
        String imageUrl = savedImageUrls.get(position);

        BearImageVolleySingleton volleySingleton = BearImageVolleySingleton.getInstance(this);

        ImageRequest imageRequest = new ImageRequest(imageUrl,
                response -> {
                    Bitmap imageBitmap = response;
                    int width = imageBitmap.getWidth();
                    int height = imageBitmap.getHeight();

                    // Create the title with dimensions
                    String title = "Image Dimensions: " + width + "x" + height;

                    // Create the AlertDialog with options
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(title);
                    builder.setItems(new CharSequence[]{"Delete", "Display Image"}, (dialog, which) -> {
                        if (which == 0) {
                            // Delete option selected
                            deleteImage(position);
                        } else if (which == 1) {
                            // Display Image option selected
                            displayImage(imageUrl);
                        }
                    });

                    builder.setPositiveButton("Cancel", (dialog, which) -> dialog.dismiss());
                    builder.show();
                }, 0, 0, ImageView.ScaleType.CENTER_INSIDE, null,
                error -> {
                    // Handle error loading image
                    Log.e("SavedImagesActivity", "Error loading image: " + error.getMessage());
                });

        // Add the image request to the Volley request queue
        volleySingleton.addToRequestQueue(imageRequest);
    }

    /**
     * Deletes a saved image at the specified position from the database and the RecyclerView.
     *
     * @param position The position of the image to be deleted.
     */
    private void deleteImage(int position) {
        String imageUrl = savedImageUrls.get(position);
        databaseHelper.deleteImageUrl(imageUrl);
        savedImageUrls.remove(position);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    /**
     * Displays the selected image and returns to the MainActivity with the selected image URL.
     *
     * @param imageUrl The URL of the selected image.
     */
    private void displayImage(String imageUrl) {
        // Navigate back to MainActivity and display the selected image
        Intent intent = new Intent();
        intent.putExtra("selectedImageUrl", imageUrl);
        setResult(Activity.RESULT_OK, intent);
        finish();
        Log.d("SavedImagesActivity", "Selected image URL: " + imageUrl);
    }

}

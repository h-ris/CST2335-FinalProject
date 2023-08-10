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

public class SavedImagesActivity extends AppCompatActivity implements BearRecyclerViewAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private ArrayList<String> savedImageUrls;
    private BearRecyclerViewAdapter recyclerViewAdapter;
    private BearImageDatabaseHelper databaseHelper;

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


    private void loadSavedImagesFromDatabase() {
        ArrayList<String> savedImageUrls = databaseHelper.getAllImageUrls();
        recyclerViewAdapter.updateData(savedImageUrls);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database when the activity is destroyed
        databaseHelper.close();
    }

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

    private void deleteImage(int position) {
        String imageUrl = savedImageUrls.get(position);
        databaseHelper.deleteImageUrl(imageUrl);
        savedImageUrls.remove(position);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void displayImage(String imageUrl) {
        // Navigate back to MainActivity and display the selected image
        Intent intent = new Intent();
        intent.putExtra("selectedImageUrl", imageUrl);
        setResult(Activity.RESULT_OK, intent);
        finish();
        Log.d("SavedImagesActivity", "Selected image URL: " + imageUrl);
    }

}

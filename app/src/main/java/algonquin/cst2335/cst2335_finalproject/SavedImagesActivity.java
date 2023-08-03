package algonquin.cst2335.cst2335_finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options");
        builder.setItems(new CharSequence[]{"Delete", "Display Image"}, (dialog, which) -> {
            if (which == 0) {
                // Delete option selected
                deleteImage(position);
            } else if (which == 1) {
                // Display Image option selected
                displayImage(imageUrl);
            }
        });
        builder.show();
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

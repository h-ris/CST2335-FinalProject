package algonquin.cst2335.cst2335_finalproject;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;


/**
 * An adapter class for populating a RecyclerView with a list of image URLs using Volley's ImageLoader.
 * @author Daniel Stewart
 * @version Daniel Stewart
 */
public class BearRecyclerViewAdapter extends RecyclerView.Adapter<BearRecyclerViewAdapter.ViewHolder> {

    // The application context used for initializing the ImageLoader instance.
    private final Context context;

    // The list of image URLs to be displayed in the RecyclerView.
    private final ArrayList<String> urlList;

    private BearImageDatabaseHelper databaseHelper;

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    private ItemClickListener itemClickListener;

    /**
     * Constructor for the BearRecyclerViewAdapter class.
     *
     * @param context  The application context.
     * @param urlList  The list of image URLs to be displayed in the RecyclerView.
     */
    public BearRecyclerViewAdapter(Context context, ArrayList<String> urlList) {
        this.context = context;
        this.urlList = urlList;
//        databaseHelper = new BearImageDatabaseHelper(context);
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent  The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType  The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public BearRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for the individual item view and create a new ViewHolder.
        View view = LayoutInflater.from(context).inflate(R.layout.bear_image_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder  The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Retrieve the image URL for the current position.
        String imageUrl = urlList.get(position);

        // Load the image into the NetworkImageView using the shared ImageLoader instance from BearImageVolleySingleton.
        holder.bearImage.setImageUrl(imageUrl, BearImageVolleySingleton.getInstance(context).getImageLoader());
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in the data set.
     */
    @Override
    public int getItemCount() {
        return urlList.size();
    }

    /**
     * ViewHolder class to hold references to the views for each item in the RecyclerView.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        NetworkImageView bearImage;

        /**
         * Constructor for the ViewHolder class.
         *
         * @param itemView  The item view containing the NetworkImageView to display the image.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the NetworkImageView from the item view.
            bearImage = itemView.findViewById(R.id.bearImage);
        }
    }

    /**
     * Updates the data of the RecyclerView adapter with the provided list of image URLs.
     *
     * @param urlList The ArrayList of image URLs to update the adapter's data with.
     */
    public void updateData(ArrayList<String> urlList) {
        this.urlList.clear();
        this.urlList.addAll(urlList);
        notifyDataSetChanged();
    }

    /**
     * Deletes an item from the adapter's data and the associated database entry.
     *
     * @param position The position of the item to be deleted.
     */
    public void deleteItem(int position) {
        String imageUrl = urlList.get(position);
        urlList.remove(position);
        notifyDataSetChanged();
        databaseHelper.deleteImageUrl(imageUrl);
    }

    /**
     * Sets a listener for item click events in the RecyclerView items.
     *
     * @param itemClickListener The ItemClickListener to be set for handling item click events.
     */
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


}
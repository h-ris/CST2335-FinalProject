package algonquin.cst2335.cst2335_finalproject;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * A singleton class that provides a centralized instance of Volley's RequestQueue
 * and an ImageLoader with caching functionality for handling image requests.
 * @author Daniel Stewart
 * @version 1.0
 */
public class BearImageVolleySingleton {

    // Singleton instance of the BearImageVolleySingleton class.
    private static BearImageVolleySingleton instance;

    // Volley's RequestQueue for handling network requests.
    private RequestQueue requestQueue;

    // ImageLoader with caching functionality for loading and caching images.
    private ImageLoader imageLoader;

    // Application context used for initializing Volley's RequestQueue.
    private static Context ctx;

    /**
     * Private constructor to prevent direct instantiation of the class.
     * Initializes the RequestQueue and ImageLoader with caching functionality.
     *
     * @param context The application context used to initialize the Volley components.
     */
    private BearImageVolleySingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();

        // Initialize the ImageLoader with caching functionality using an anonymous class.
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    /**
     * Returns the singleton instance of BearImageVolleySingleton, creating it if necessary.
     *
     * @param context The application context used to initialize the singleton.
     * @return The singleton instance of BearImageVolleySingleton.
     */
    public static synchronized BearImageVolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new BearImageVolleySingleton(context);
        }
        return instance;
    }

    /**
     * Returns the RequestQueue instance or creates a new one if it's not initialized.
     *
     * @return The RequestQueue instance.
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * Adds a request to the RequestQueue.
     *
     * @param req The request to be added to the queue.
     * @param <T> The type of the request.
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    /**
     * Returns the ImageLoader instance with caching functionality.
     *
     * @return The ImageLoader instance.
     */
    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}

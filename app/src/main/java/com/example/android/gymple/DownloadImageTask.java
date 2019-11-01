package com.example.android.gymple;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * The DownloadImageTask class will perform images download from URL from background operation
 * @author  Desmond Yeo
 * @version 1.0, 23 Oct 2019
 *
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView bmImage;

    /**
     * Default constructor for instantiating DownloadImageTask task.
     * @param bmImage Android image view user interface
     */
    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    /**
     * Perform background operation of downloading image from url and convert to a Bitmap
     * @param urls The url of the image
     * @return Bitmap Digital image composed of a matrix of dots
     */
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap bmp = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            bmp = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", ""+e.getMessage());
            e.printStackTrace();
        }
        return bmp;
    }
    /**
     * After the background operation of downloading image is complete if will update the android ImageView
     * @param result
     */
    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
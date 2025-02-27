package com.example.androidlabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private ImageView catViewImage;
    private ProgressBar progressBar;
    private Handler handler = new Handler();
    private int currentProgress = 0;
    private final int SLIDESHOW_DURATION = 5000; // 5 seconds per image
    private final int UPDATE_INTERVAL = 50; // Progress updates every 50ms

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        catViewImage = findViewById(R.id.catImageView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);

        startSlideshow();
    }

    private void startSlideshow() {
        new LoadCatImage().execute();
        currentProgress = 0;
        updateProgressBar();
    }

    private void updateProgressBar() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentProgress += UPDATE_INTERVAL;
                progressBar.setProgress((currentProgress * 100) / SLIDESHOW_DURATION);

                if (currentProgress < SLIDESHOW_DURATION) {
                    handler.postDelayed(this, UPDATE_INTERVAL);
                } else {
                    startSlideshow(); // Load a new image when time runs out
                }
            }
        }, UPDATE_INTERVAL);
    }

    private class LoadCatImage extends AsyncTask<Void, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                URL jsonUrl = new URL("https://cataas.com/cat?json=true");
                HttpURLConnection connection = (HttpURLConnection) jsonUrl.openConnection();
                connection.setRequestMethod("GET");

                InputStream inputStream = connection.getInputStream();
                Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
                String jsonString = scanner.hasNext() ? scanner.next() : "";
                scanner.close();

                JSONObject jsonObject = new JSONObject(jsonString);
                String catId = jsonObject.getString("id");
                String imageURL = "https://cataas.com" + jsonObject.getString("url");

                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                if (storageDir == null) return null;

                File catFile = new File(storageDir, catId + ".jpg");

                if (catFile.exists()) {
                    return BitmapFactory.decodeFile(catFile.getAbsolutePath());
                } else {
                    URL imgUrl = new URL(imageURL);
                    HttpURLConnection imgConnection = (HttpURLConnection) imgUrl.openConnection();
                    InputStream imgStream = imgConnection.getInputStream();
                    Bitmap catImage = BitmapFactory.decodeStream(imgStream);
                    imgStream.close();

                    // Save the image
                    FileOutputStream outputStream = new FileOutputStream(catFile);
                    catImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();

                    return catImage;
                }
            } catch (Exception e) {
                Log.e("LoadCatImage", "Error loading image", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                catViewImage.setImageBitmap(result);
            }
        }
    }
}

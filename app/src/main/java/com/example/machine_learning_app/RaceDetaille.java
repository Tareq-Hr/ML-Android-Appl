package com.example.machine_learning_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.visual_recognition.v3.model.ClassifyOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class RaceDetaille extends AppCompatActivity {

    public String getPrediction() {/*
        Authenticator authenticator = new IamAuthenticator("GEqKoCqVHa5qYuVyFMFyJXQuwOe9tISWcENJtRWQjiVF");
        VisualRecognition service = new VisualRecognition("2018-03-19", authenticator);
            AddImagesOptions image = new AddImagesOptions.Builder()
                    .addImageUrl("https://nas-national-prod.s3.amazonaws.com/styles/hero_mobile/s3/h_a1_7443_5_painted-bunting_julie_torkomian_adult-male.jpg?itok=dMVj7z0b") // replace with path to file
                    .collectionId("DefaultCustomModel_1395740173")
                    .build();
        // analyze the image!
        AnalyzeOptions options = new AnalyzeOptions.Builder()
                .addImageUrl("
     https://nas-national-prod.s3.amazonaws.com/styles/hero_mobile/s3/h_a1_7443_5_painted-bunting_julie_torkomian_adult-male.jpg?itok=dMVj7z0b")
                .addFeatures(AnalyzeOptions.Features.OBJECTS)
                .build();
        AnalyzeResponse result = service.analyze(options).execute().getResult();


        return String.valueOf(result);

        *///2

        IamAuthenticator authenticator = new IamAuthenticator("GEqKoCqVHa5qYuVyFMFyJXQuwOe9tISWcENJtRWQjiVF");

        VisualRecognition service = new VisualRecognition("2018-03-19", authenticator);

        InputStream imagesStream = null;

        if((Bitmap)getIntent().getParcelableExtra("bp") != null) {
            try {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
                OutputStream out = null;
                File file = new File(path, "new_bird.jpg");
                out = new FileOutputStream(file);
                Bitmap bp = (Bitmap) getIntent().getParcelableExtra("bp");
                bp.compress(Bitmap.CompressFormat.JPEG, 100, out);
                imageview.setImageBitmap(bp);
                System.out.println(file);
                imagesStream = new FileInputStream(file);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else if(!getIntent().getStringExtra("path").isEmpty()) {
            try {
                Uri uri = Uri.parse(getIntent().getStringExtra("path"));
                imageview.setImageURI(uri);
                System.out.println(uri.getLastPathSegment());
                imagesStream = new FileInputStream(uri.getLastPathSegment());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                .imagesFile(imagesStream)
                .imagesFilename("Bird")
                .threshold((float) 0.6)
                .classifierIds(Arrays.asList("DefaultCustomModel_746826046"))
                .build();
        ClassifiedImages result = service.classify(classifyOptions).execute().getResult();
        System.out.println(result.getImages().get(0).getClassifiers().get(0).getClasses().get(0).getXClass());
        ////
        String BirdClass = result.getImages().get(0).getClassifiers().get(0).getClasses().get(0).getXClass();
        return BirdClass;
    }

    private TextView race_type;
    private String BirdClass;
    private ImageView imageview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_detaille);

        race_type = findViewById(R.id.textView_race_type);
        imageview  =findViewById(R.id.imageView);




        Thread thread =new Thread(new Runnable() {
            public void run() {
                try {
                    BirdClass = getPrediction();
                    System.out.println(BirdClass);
                    race_type.setText(BirdClass);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}

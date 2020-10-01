package com.example.machine_learning_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.visual_recognition.v3.model.ClassifyOptions;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;

public class RaceDetaille extends AppCompatActivity {

    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();

    private long fileSize = 0;
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

        IamAuthenticator authenticator = new IamAuthenticator("YFqAdIkR1M7JfdWEyi777Uuc8SQefTzTsGVVqUcprVtW");

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
               // Uri uri = Uri.fromFile(new File("path/to/images/rivers.jpg"));
                imageview.setImageURI(uri);
                System.out.println(uri.getLastPathSegment());
                imagesStream = new FileInputStream(uri.getLastPathSegment());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else if(!getIntent().getStringExtra("raceName").isEmpty()){
            race_type.setText(getIntent().getStringExtra("raceName"));
            imageview.setImageResource(getIntent().getIntExtra("image",0));
        }
        ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                .imagesFile(imagesStream)
                .imagesFilename("Bird")
                .threshold((float) 0.6)
                .classifierIds(Arrays.asList("DefaultCustomModel_823799762"))
                .build();
        ClassifiedImages result = service.classify(classifyOptions).execute().getResult();
        System.out.println(result.getImages().get(0).getClassifiers().get(0).getClasses().get(0).getXClass());
        ////
            BirdClass = result.getImages().get(0).getClassifiers().get(0).getClasses().get(0).getXClass();

        return BirdClass;
    }

    private TextView race_type, family, habitat, feeding, name;
    private String BirdClass;
    private ImageView imageview, image1, image2, image3, image4;
    ProgressDialog progressDialog;

    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_detaille);

        race_type = findViewById(R.id.textView_race_type);
        name = findViewById(R.id.textView3);
        family = findViewById(R.id.textView6);
        feeding = findViewById(R.id.textView9);
        habitat = findViewById(R.id.textView7);
        imageview  =findViewById(R.id.imageView);
        image1  =findViewById(R.id.imageView7);
        image2  =findViewById(R.id.imageView8);
        image3  =findViewById(R.id.imageView9);
        image4  =findViewById(R.id.imageView10);

        progressDialog = new ProgressDialog(RaceDetaille.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.show();
        progressDialog.setContentView(R.layout.prog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);



        Thread thread =new Thread(new Runnable() {
            public void run() {
                try {
                    BirdClass = getIntent().getStringExtra("raceName");
                    if(BirdClass == null){
                        BirdClass = getPrediction();
                        race_type.setText(BirdClass);
                    }else{
                        BirdClass = getIntent().getStringExtra("raceName");
                        imageview.setImageResource(getIntent().getIntExtra("image",0));
                        race_type.setText(BirdClass);
                        progressDialog.findViewById(R.id.prog).setVisibility(View.INVISIBLE);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            progressDialog.findViewById(R.id.prog).setVisibility(View.VISIBLE);
                            progressDialog.findViewById(R.id.prog).setVisibility(View.INVISIBLE);
                            progressDialog.dismiss();
                        }
                    });
                    System.out.println(BirdClass);
                    race_type.setText(BirdClass);


                    //Firebase
                    // Create database instance
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference();

                    mStorageRef = FirebaseStorage.getInstance().getReference();
                    Query query = myRef.child(BirdClass.replace(' ','_'));

                    final StorageReference birdRef = mStorageRef.child("/Training/ALBATROSS/3.jpg");

                    System.out.println(birdRef);



                    // Read from the database
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            if(dataSnapshot.exists()) {
                                for(DataSnapshot issue : dataSnapshot.getChildren()) {
                                    HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                                    name.setText(value.get("name").toString().replace('_',' '));
                                    family.setText(value.get("family").toString());
                                    habitat.setText(value.get("habitat").toString());
                                    feeding.setText(value.get("feeding").toString());
                                    Log.d("Firebase", "Value is: " + value);

                                    final StorageReference birdRef1 = mStorageRef.child("/Training/"+BirdClass.replace(' ','_')+"/1.jpg");
                                    birdRef1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.d("URI : ", uri.toString());
                                            Picasso.get().load(uri).into(image1);
                                        }
                                    });

                                    final StorageReference birdRef2 = mStorageRef.child("/Training/"+BirdClass.replace(' ','_')+"/2.jpg");
                                    birdRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.d("URI : ", uri.toString());
                                            Picasso.get().load(uri).into(image2);
                                        }
                                    });

                                    final StorageReference birdRef3 = mStorageRef.child("/Training/"+BirdClass.replace(' ','_')+"/3.jpg");
                                    birdRef3.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.d("URI : ", uri.toString());
                                            Picasso.get().load(uri).into(image3);
                                        }
                                    });

                                    final StorageReference birdRef4 = mStorageRef.child("/Training/"+BirdClass.replace(' ','_')+"/4.jpg");
                                    birdRef4.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.d("URI : ", uri.toString());
                                            Picasso.get().load(uri).into(image4);
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w("Firebase", "Failed to read value.", error.toException());
                        }
                    });
                    //

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }


}
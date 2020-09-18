package com.example.machine_learning_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.developer_cloud.service.security.IamOptions;

import com.ibm.watson.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.visual_recognition.v3.model.ClassifyOptions;
import com.ibm.watson.visual_recognition.v4.model.AddImageTrainingDataOptions;
import com.ibm.watson.visual_recognition.v4.model.AddImagesOptions;
import com.ibm.watson.visual_recognition.v4.model.AnalyzeOptions;
import com.ibm.watson.visual_recognition.v4.model.AnalyzeResponse;
import com.ibm.watson.visual_recognition.v4.model.Collection;
import com.ibm.watson.visual_recognition.v4.model.CreateCollectionOptions;
import com.ibm.watson.visual_recognition.v4.model.ImageDetailsList;
import com.ibm.watson.visual_recognition.v4.model.Location;
import com.ibm.watson.visual_recognition.v4.model.TrainOptions;
import com.ibm.watson.visual_recognition.v4.model.TrainingDataObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ImageButton btn_import;
    private ImageButton btn_capture;

    private ImageView imageview;

    final String[] CAPTURE_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    final String[] IMPORT_IMAGE_PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE};

    final int REQUEST_IMAGE_ID = 20;
    final int REQUEST_IMPORT_IMAGE_ID = 40;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_import = findViewById(R.id.imageButton);
        btn_capture = findViewById(R.id.imageButton2);

        imageview = findViewById(R.id.imageView);


    }

    public void ImportImage(View view) {
        if (hasPermissions(MainActivity.this, IMPORT_IMAGE_PERMISSIONS)){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_IMPORT_IMAGE_ID);
        }else{
            ActivityCompat.requestPermissions(MainActivity.this, CAPTURE_PERMISSIONS, REQUEST_IMPORT_IMAGE_ID);
        }
    }

    public void CaptureImage(View view) {
        if (hasPermissions(MainActivity.this, CAPTURE_PERMISSIONS)){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_IMAGE_ID);
        }else{
            ActivityCompat.requestPermissions(MainActivity.this, CAPTURE_PERMISSIONS, REQUEST_IMAGE_ID);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_ID) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(MainActivity.this, RaceDetaille.class);
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                intent.putExtra("bp",bp);
                startActivity(intent);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action annulée", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action échouée", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_IMPORT_IMAGE_ID){
            if (resultCode == RESULT_OK){
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePath, null, null, null);
                cursor.moveToFirst();

                Intent intent = new Intent(MainActivity.this, RaceDetaille.class);
                intent.putExtra("path",selectedImage.toString());
                startActivity(intent);

            }

        }
    }

    public boolean hasPermissions(Context context, String[] permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
;
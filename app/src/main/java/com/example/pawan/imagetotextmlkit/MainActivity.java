package com.example.pawan.imagetotextmlkit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.api.services.translate.Translate;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private android.widget.Button snapButton;
    private android.widget.TextView englishText;
    private android.widget.TextView hindiText;
    private Bitmap imageBitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Task<FirebaseVisionText> result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        snapButton = findViewById(R.id.snap);
        englishText = findViewById(R.id.englishText);
        snapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

//        translateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                detectText();
//            }
//        });

    }

    private void detectText() {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextDetector detector = FirebaseVision.getInstance().getVisionTextDetector();

                detector.detectInImage(image)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                processText(firebaseVisionText);
                                // ...
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });



    }

    private void processText(FirebaseVisionText firebaseVisionText) {

        List<FirebaseVisionText.Block> blocks = firebaseVisionText.getBlocks();

        if(blocks == null || blocks.size() == 0) {
            Toast.makeText(this, "No Text Found", Toast.LENGTH_LONG).show();
            return;
        }

        ///////////////////////////////

        //////////////////////////////////

        for(FirebaseVisionText.Block block : blocks) {
            Log.d("blocks",block.getText());
            for (FirebaseVisionText.Line line: block.getLines()) {
//                for (FirebaseVisionText.Element element: line.getElements()) {
                    Log.d("lines",line.getText());
                    String text = line.getText();
                    englishText.setTextSize(24);
                    englishText.setText(englishText.getText().toString()+"\n"+text);
//                }
            }

        }


    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");

            detectText();
        }
    }
}

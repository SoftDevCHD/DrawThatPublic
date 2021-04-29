package com.pictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class CreationActivity extends AppCompatActivity {

    public static final String TAG = "CreationActivity";

    private Button btnTakePicture;
    private Button btnSubmit;
    private ImageView ivCreatePicture;
    private EditText etDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);

        btnTakePicture = findViewById(R.id.btnTakePicture);
        btnSubmit = findViewById(R.id.btnSubmit);
        ivCreatePicture = findViewById(R.id.ivCreatePicture);
        etDescription = findViewById(R.id.etDescription);

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open camera, set ivCreatePicture to picture taken
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create post, put it in recycler view
            }
        });
    }
}
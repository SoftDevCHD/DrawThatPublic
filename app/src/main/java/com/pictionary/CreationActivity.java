package com.pictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.io.File;

public class CreationActivity extends AppCompatActivity {

    public static final String TAG = "CreationActivity";

    private Button btnTakePicture;
    private TextView tvPhrase;
    private Button btnSubmit;
    private ImageView ivCreatePicture;
    private EditText etDescription;
    private Phrase currentPhrase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);

        currentPhrase = Parcels.unwrap(getIntent().getParcelableExtra("currentPhrase"));

        btnTakePicture = findViewById(R.id.btnTakePicture);
        tvPhrase = findViewById(R.id.tvPhrase);
        btnSubmit = findViewById(R.id.btnSubmit);
        ivCreatePicture = findViewById(R.id.ivCreatePicture);
        etDescription = findViewById(R.id.etDescription);

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open camera, set ivCreatePicture to picture taken
            }
        });

        String currPhrase = "Current Phrase: " + currentPhrase.getName();
        tvPhrase.setText(currPhrase);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create post, put it in recycler view
                String description = etDescription.getText().toString();
                File image = null;      // TODO: Use actual image
                ParseUser user = ParseUser.getCurrentUser();
                Phrase phrase = currentPhrase;

                savePost(description, image, user, phrase);
            }
        });
    }

    // Saves a new post to Parse
    private void savePost(String description, File image, ParseUser user, Phrase phrase) {
        ParseObject post = new ParseObject("Post");

        // Set up post to be saved
        post.put("description", description);
        //post.put("image", image);     Uncomment this after implementing camera
        post.put("user", user);
        post.put("phrase", phrase);

        // Save post
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                // If e exists, error has occurred
                if(e != null) {
                    Log.e(TAG, "Could not save post.", e);
                    Toast.makeText(CreationActivity.this, "Post failed!", Toast.LENGTH_SHORT).show();
                }
                etDescription.setText("");
                ivCreatePicture.setImageResource(0);
                Toast.makeText(CreationActivity.this, "Posted to DrawThat!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
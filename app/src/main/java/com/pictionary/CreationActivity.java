package com.pictionary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 20;

    private Button btnTakePicture;
    private TextView tvPhrase;
    private Button btnSubmit;
    private ImageView ivCreatePicture;
    private EditText etDescription;
    private Phrase currentPhrase;
    private File photoFile;
    private String photoFileName;

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

        photoFileName = "image.jpg";

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open camera, set ivCreatePicture to picture taken
                launchCamera();
            }
        });

        String currPhrase = "Current Phrase: " + currentPhrase.getName();
        tvPhrase.setText(currPhrase);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create post, put it in recycler view
                String description = etDescription.getText().toString();
                ParseUser user = ParseUser.getCurrentUser();
                Phrase phrase = currentPhrase;

                savePost(description, photoFile, user, phrase);
            }
        });
    }

    // Saves a new post to Parse
    private void savePost(String description, File image, ParseUser user, Phrase phrase) {
        ParseObject post = new ParseObject("Post");

        // Set up post to be saved
        post.put("description", description);
        post.put("image", new ParseFile(image));
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

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(CreationActivity.this, "com.codepath.fileprovider.Pictionary", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        if(!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "Failed to create directory");
        }

        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                ivCreatePicture.setImageBitmap(takenImage);
            } else {
                Toast.makeText(this, "Picture was taken", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
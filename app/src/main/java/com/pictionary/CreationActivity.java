package com.pictionary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
        Post post = new Post();

        // Set up post to be saved
        post.setDescription(description);
        post.setImage(new ParseFile(image));
        post.setUser(user);
        post.setPhrase(phrase);

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

                // Go back to feed
                Intent i = new Intent();
                i.putExtra("post", Parcels.wrap(post));
                setResult(RESULT_OK, i);
                finish();
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

                // Rotate the image accordingly
                Bitmap takenImage = rotateBitmapOrientation(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                Bitmap resizedImage;
                try {
                    resizedImage = resizeImage(takenImage);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                // Load the taken image into a preview
                ivCreatePicture.setImageBitmap(resizedImage);
            } else {
                Toast.makeText(this, "Picture was taken", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Bitmap resizeImage(Bitmap image) throws IOException {
        Bitmap resizedImage;
        if (image.getWidth() > image.getHeight()) {
            resizedImage = scaleToFitWidth(image, 1024);
        } else {
            resizedImage = scaleToFitHeight(image, 1024);
        }
        // Configure byte output stream
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        // Compress the image further
        resizedImage.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        // Create a new file for the resized bitmap (`getPhotoFileUri` defined above)
        File resizedFile = getPhotoFileUri("resized_" + photoFileName);
        resizedFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(resizedFile);
        // Write the bytes of the bitmap to file
        fos.write(bytes.toByteArray());
        fos.close();
        photoFile = resizedFile;
        return resizedImage;
    }

    public Bitmap rotateBitmapOrientation(String photoFilePath) {
        // Create and configure BitmapFactory
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath);

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        // Rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        // Return result
        return rotatedBitmap;
    }

    // Scale and maintain aspect ratio given a desired height
    // scaleToFitWidth(bitmap, 100);
    public static Bitmap scaleToFitWidth(Bitmap b, int width)
    {
        float factor = width / (float) b.getWidth();
        int height = (int) (b.getHeight() * factor);
        return Bitmap.createScaledBitmap(b, width, height, true);
    }


    // Scale and maintain aspect ratio given a desired height
    // scaleToFitHeight(bitmap, 100);
    public static Bitmap scaleToFitHeight(Bitmap b, int height)
    {
        float factor = height / (float) b.getHeight();
        int width = (int) (b.getWidth() * factor);
        return Bitmap.createScaledBitmap(b, width, height, true);
    }

}
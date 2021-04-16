package com.pictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CreateGame extends AppCompatActivity {

    public static final String TAG = "CreateGame";

    private EditText etTeamOneName;
    private EditText etTeamTwoName;
    private Button btnCreateTeams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        etTeamOneName = (EditText) findViewById(R.id.etTeamOneName);
        etTeamTwoName = (EditText) findViewById(R.id.etTeamTwoName);
        btnCreateTeams = (Button) findViewById(R.id.btnCreateTeams);

        btnCreateTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onCLick createTeams");
                saveGame();
            }
        });
    }
    private void saveGame() {
        Game game = new Game();
        game.setTeamOneName(etTeamOneName.getText().toString());
        game.setTeamTwoName(etTeamTwoName.getText().toString());
        game.setCreatedBy(ParseUser.getCurrentUser());

        game.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error saving game", e);
                    Toast.makeText(CreateGame.this, "Create Teams failed.", Toast.LENGTH_LONG).show();
                    return;
                }

                loadMainActivity();
            }
        });
    }

    private void loadMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
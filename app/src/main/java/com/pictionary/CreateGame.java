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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class CreateGame extends AppCompatActivity {

    public static final String TAG = "CreateGame";

    private EditText etTeamOneName;
    private EditText etTeamTwoName;
    private Button btnCreateTeams;
    private Button btnResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        etTeamOneName = (EditText) findViewById(R.id.etTeamOneName);
        etTeamTwoName = (EditText) findViewById(R.id.etTeamTwoName);
        btnCreateTeams = (Button) findViewById(R.id.btnCreateTeams);
        btnResume = (Button) findViewById(R.id.btnResume);

        findPreviousGames();

        btnCreateTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onCLick createTeams");
                saveGame();
            }
        });

        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onCLick resume");
                resumeGame();
            }
        });
    }

    private void findPreviousGames() {
        int resume = (int) getIntent().getSerializableExtra("resume");
        if (resume == 1) {
            btnResume.setEnabled(false);
        }
        else {
            btnResume.setEnabled(true);
        }
    }

    private void resumeGame() {
        ParseQuery<Game> query = ParseQuery.getQuery(Game.class);
        query.whereEqualTo(Game.KEY_CREATED_BY, ParseUser.getCurrentUser());
        query.addDescendingOrder(Game.KEY_CREATED_AT);
        query.setLimit(1);
        try {
            List<Game> games = query.find();
            Game game = games.get(0);
            loadMainActivity(game.getTeamOneName(), game.getTeamTwoName(), game.getTeamOneScore(), game.getTeamTwoScore(), game.getObjectId());

        } catch (Exception e) {
            Log.e(TAG, "Error querying previous games");
            e.printStackTrace();
            return;
        }
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

                loadMainActivity(game.getTeamOneName(), game.getTeamTwoName(), 0, 0, game.getObjectId());
            }
        });
    }

    private void loadMainActivity(String teamOne, String teamTwo, int scoreOne, int scoreTwo, String objectId) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("teamOneName", teamOne);
        intent.putExtra("teamTwoName", teamTwo);
        intent.putExtra("teamOneScore", scoreOne);
        intent.putExtra("teamTwoScore", scoreTwo);
        intent.putExtra("objectId", objectId);
        startActivity(intent);
        finish();
    }
}
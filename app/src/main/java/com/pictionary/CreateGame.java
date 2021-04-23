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
        ParseQuery<Game> query = ParseQuery.getQuery(Game.class);
        query.whereEqualTo(Game.KEY_CREATED_BY, ParseUser.getCurrentUser());
        query.setLimit(1);
        query.findInBackground(new FindCallback<Game>() {
            @Override
            public void done(List<Game> games, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "No previous games found", e);
                    return;
                }
                btnResume.setEnabled(true);
            }
        });
    }

    private void resumeGame() {
        ParseQuery<Game> query = ParseQuery.getQuery(Game.class);
        query.whereEqualTo(Game.KEY_CREATED_BY, ParseUser.getCurrentUser());
        query.include(Game.KEY_TEAM_ONE_NAME);
        query.include(Game.KEY_TEAM_ONE_SCORE);
        query.include(Game.KEY_TEAM_TWO_NAME);
        query.include(Game.KEY_TEAM_TWO_SCORE);
        query.include(Game.KEY_PHRASE);
        query.addDescendingOrder(Game.KEY_CREATED_AT);
        query.setLimit(1);
        query.findInBackground(new FindCallback<Game>() {
            @Override
            public void done(List<Game> games, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting games", e);
                    return;
                }
                Game game = games.get(0);
                loadMainActivity(game.getTeamOneName(), game.getTeamTwoName(), game.getTeamOneScore(), game.getTeamTwoScore(), game.getPhrase());
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

    private void loadMainActivity(String teamOne, String teamTwo, int scoreOne, int scoreTwo, String phrase) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("teamOneName", teamOne);
        intent.putExtra("teamTwoName", teamTwo);
        intent.putExtra("teamOneScore", scoreOne);
        intent.putExtra("teamTwoScore", scoreTwo);
        intent.putExtra("phrase", phrase);
        startActivity(intent);
        finish();
    }

    private void loadMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
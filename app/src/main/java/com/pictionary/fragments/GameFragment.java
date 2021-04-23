package com.pictionary.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.pictionary.Phrase;
import com.pictionary.R;

import java.util.List;


public class GameFragment extends Fragment {

    public static final String TAG = "GameFragment";
    private TextView tvDifficulty;
    private TextView tvName;
    private TextView teamOneScore;
    private TextView teamTwoScore;
    private Button btnStartTimer;
    private Button btnNextPhrase;
    private ProgressBar pgTimer;
    private List<Phrase> phrases;

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for the fragment
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvDifficulty = (TextView) view.findViewById(R.id.tvDifficulty);
        tvName = (TextView) view.findViewById(R.id.tvName);
        btnStartTimer = (Button) view.findViewById(R.id.btnStartTimer);
        btnNextPhrase = (Button) view.findViewById(R.id.btnNextPhrase);
        pgTimer = (ProgressBar) view.findViewById(R.id.pgTimer);
        teamOneScore = (TextView) view.findViewById(R.id.tvTeamOne);
        teamTwoScore = (TextView) view.findViewById(R.id.tvTeamTwo);
    }

    private void getRandomPhrase() {
        Phrase phrase;
        ParseQuery<Phrase> query = ParseQuery.getQuery(Phrase.class);
        query.setLimit(1);
        query.findInBackground(new FindCallback<Phrase>() {
            @Override
            public void done(List<Phrase> phrases, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue getting phrase", e);
                    return;
                }
                Phrase phrase = phrases.get(0);
                tvDifficulty.setText(phrase.getDifficulty());
                tvName.setText(phrase.getName());
            }
        });
    }
}
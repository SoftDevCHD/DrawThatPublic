package com.pictionary.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.pictionary.Phrase;
import com.pictionary.R;

import java.util.List;
import java.util.Random;


public class GameFragment extends Fragment {

    public static final String TAG = "GameFragment";
    private TextView tvDifficulty;
    private TextView tvName;
    private TextView tvTeamOneName;
    private TextView tvTeamTwoName;
    private TextView tvTeamOneScore;
    private TextView tvTeamTwoScore;
    private Button btnStartTimer;
    private Button btnNextPhrase;
    private Button btnRevealPhrase;
    private ProgressBar pgTimer;
    private Drawable pgDrawable;
    private ObjectAnimator animProgress;
    private ObjectAnimator animColorFirst;
    private ObjectAnimator animColorLast;
    private AnimatorSet animSet;
    private boolean isAnimating;
    private boolean isCancelled;
    private int timerStartColor;
    private int timerMidColor;
    private int timerEndColor;
    private int timerIdleColor;
    private int timerDuration;
    Phrase phrase;


    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for the fragment
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvDifficulty = (TextView) view.findViewById(R.id.tvDifficulty);
        tvName = (TextView) view.findViewById(R.id.tvName);
        btnRevealPhrase = (Button) view.findViewById(R.id.btnRevealPhrase);
        btnStartTimer = (Button) view.findViewById(R.id.btnStartTimer);
        btnNextPhrase = (Button) view.findViewById(R.id.btnNextPhrase);
        pgTimer = (ProgressBar) view.findViewById(R.id.pgTimer);
        tvTeamOneName = (TextView) view.findViewById(R.id.tvTeamOneName);
        tvTeamTwoName = (TextView) view.findViewById(R.id.tvTeamTwoName);
        tvTeamOneScore = view.findViewById(R.id.tvTeamOneScore);
        tvTeamTwoScore = view.findViewById(R.id.tvTeamTwoScore);

        if (getArguments() != null) {
            Bundle args = getArguments();
            tvTeamOneName.setText(args.getString("teamOneName") != null ? args.getString("teamOneName") : "Team One");
            tvTeamTwoName.setText(args.getString("teamTwoName") != null ? args.getString("teamTwoName") : "Team Two");
            tvTeamOneScore.setText(String.valueOf(args.getInt("teamOneScore")));
            tvTeamTwoScore.setText(String.valueOf(args.getInt("teamTwoScore")));
        }

        phrase = getPhrase();
        tvDifficulty.setText(phrase.getDifficulty());
        tvName.setText(phrase.getName());

        setupNewPhrase();

        btnNextPhrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupNewPhrase();
            }
        });


        btnRevealPhrase.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        tvName.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    default:
                        tvName.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });


        // Set up timer animation
        timerDuration = 60;
        pgDrawable = pgTimer.getProgressDrawable();
        timerStartColor = 0xFF34F100;
        timerMidColor = 0xFFFFF200;
        timerEndColor = 0xFFFF0000;
        timerIdleColor = 0xFF9BCA93;
        pgDrawable.setTint(timerIdleColor);
        animProgress = ObjectAnimator.ofInt(pgTimer, "progress", 1000, 0);
        animColorFirst = ObjectAnimator.ofArgb(pgDrawable, "tint",
                timerStartColor, timerMidColor);
        animColorLast = ObjectAnimator.ofArgb(pgDrawable, "tint",
                timerMidColor, timerEndColor);
        animSet = new AnimatorSet();
        animProgress.setDuration(timerDuration * 1000); // Duration converted to milliseconds
        animColorFirst.setDuration((timerDuration * 1000) / 2);
        animColorLast.setDuration((timerDuration * 1000) / 2);
        animSet.play(animProgress);
        animSet.playSequentially(animColorFirst, animColorLast);
        animSet.setInterpolator(new LinearInterpolator());
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimating = true;
                btnStartTimer.setText("Stop Timer");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // If animation was not cancelled manually, take the following actions
                if(!isCancelled) {
                    Toast.makeText(getActivity(), "Time is up!", Toast.LENGTH_SHORT).show();
                    resetTimer();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                resetTimer();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        isAnimating = false;
        isCancelled = false;

        btnStartTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isAnimating) {
                    // Start animating timer
                    animSet.start();
                }
                else {
                    // Cancel the animation
                    isCancelled = true; // Timer was cancelled manually, not from running out of time
                    animSet.cancel();
                    isCancelled = false;
                }
            }
        });
    }

    private void resetTimer(){
        isAnimating=false;
        btnStartTimer.setText("Start Timer");
        pgTimer.setProgress(pgTimer.getMax());
        pgDrawable.setTint(timerIdleColor);
    }

    private void setupNewPhrase() {
        phrase = getPhrase();
        tvDifficulty.setText(phrase.getDifficulty());
        tvName.setText(phrase.getName());
    }

    private Phrase getPhrase() {
        ParseQuery<Phrase> query = ParseQuery.getQuery(Phrase.class);
        try {
            List<Phrase> phrases = query.find();
            Random random = new Random();
            int randomIndex = random.nextInt(phrases.size());
            return phrases.get(randomIndex);
        } catch (ParseException e) {
            Toast.makeText(this.getContext(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Unable to retrieve phrases...");
            return null;
        }
    }
}
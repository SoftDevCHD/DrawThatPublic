package com.pictionary.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private Button btnStartTimer;
    private Button btnNextPhrase;
    private ProgressBar pgTimer;
    private List<Phrase> phrases;
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

        // Set up timer animation
        timerDuration = 10;
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

    private void resetTimer() {
        isAnimating = false;
        btnStartTimer.setText("Start Timer");
        pgTimer.setProgress(pgTimer.getMax());
        pgDrawable.setTint(timerIdleColor);
    }
}
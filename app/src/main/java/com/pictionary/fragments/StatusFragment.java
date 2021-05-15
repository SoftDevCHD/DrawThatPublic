package com.pictionary.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pictionary.Game;
import com.pictionary.Post;
import com.pictionary.R;

public class StatusFragment extends Fragment {

    public static final String TAG = "StatusFragment";
    private TextView tvNumGamesCount;
    private TextView tvNumPostCount;

    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_status, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvNumGamesCount = view.findViewById(R.id.tvNumGamesCount);
        tvNumPostCount = view.findViewById(R.id.tvNumPostCount);
        countQuery();

    }

    private void countQuery() {
        ParseQuery<Game> query = ParseQuery.getQuery(Game.class);
        query.whereEqualTo("createdBy", ParseUser.getCurrentUser());
        query.countInBackground((count, e) -> {
            if (e == null) {
                tvNumGamesCount.setText(Integer.toString(count));
            } else {
                Toast.makeText(this.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ParseQuery<Post> query2 = ParseQuery.getQuery(Post.class);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.countInBackground((count, e) -> {
            if (e == null) {
                tvNumPostCount.setText(Integer.toString(count));
            } else {
                Toast.makeText(this.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
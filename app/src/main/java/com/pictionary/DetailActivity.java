package com.pictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pictionary.adapters.PostsAdapter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String TAG = "DetailActivity";

    private Button btnCreatePost;
    private RecyclerView rvPosts;
    private PostsAdapter postsAdapter;
    private List<String> posts;
    private Phrase currentPhrase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        currentPhrase = Parcels.unwrap(getIntent().getParcelableExtra("currentPhrase"));

        btnCreatePost = findViewById(R.id.btnCreatePost);
        rvPosts = findViewById(R.id.rvPosts);

        posts = new ArrayList<>();
        postsAdapter = new PostsAdapter(this, posts);

        rvPosts.setAdapter(postsAdapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));

        // Temporary data to test recycler view
        posts.add("Group 1");
        posts.add("Group 2");
        posts.add("Group 3");
        postsAdapter.notifyDataSetChanged();

        // Go to creation screen
        btnCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postIntent = new Intent(DetailActivity.this, CreationActivity.class);
                postIntent.putExtra("currentPhrase", Parcels.wrap(currentPhrase));
                startActivity(postIntent);
            }
        });
    }
}
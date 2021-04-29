package com.pictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pictionary.adapters.PostsAdapter;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private Button btnCreatePost;
    private RecyclerView rvPosts;
    private PostsAdapter postsAdapter;
    private List<String> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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

        // Set action for create post
        btnCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }
}
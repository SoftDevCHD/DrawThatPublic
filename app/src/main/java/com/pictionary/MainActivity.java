package com.pictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.LogInCallback;
import com.parse.ParseUser;
import com.pictionary.fragments.GameFragment;
import com.pictionary.fragments.ProfileFragment;
import com.pictionary.fragments.StatusFragment;

public class MainActivity extends AppCompatActivity {

    private static Bundle bundle;

    public static final String TAG = "MainActivity";
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Intent intent = getIntent();
            bundle = new Bundle();
            bundle.putString("teamOneName", intent.getStringExtra("teamOneName"));
            bundle.putString("teamTwoName", intent.getStringExtra("teamTwoName"));
            bundle.putInt("teamOneScore", intent.getIntExtra("teamOneScore", 0));
            bundle.putInt("teamTwoScore", intent.getIntExtra("teamTwoScore", 0));

        } catch (Exception e) {
            Log.e(TAG, "new game is created");
        } finally {

            bottomNavigationView = findViewById(R.id.bottomNavigationView);

            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment fragment;
                    switch (menuItem.getItemId()) {
                        case R.id.action_game:
                            fragment = new GameFragment();
                            if (bundle != null) {
                                fragment.setArguments(bundle);
                            }
                            break;
                        case R.id.action_status:
                            fragment = new StatusFragment();
                            break;
                        case R.id.action_profile:
                            fragment = new ProfileFragment();
                            break;
                        case R.id.action_logout:
                            ParseUser.logOut();
                            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        default:
                            fragment = new GameFragment();
                    }

                    fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                    return true;
                }
            });

            // Set default selection
            bottomNavigationView.setSelectedItemId(R.id.action_game);
        }
    }
}
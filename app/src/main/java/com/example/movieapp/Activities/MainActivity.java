package com.example.movieapp.Activities;

import android.content.Intent;
import android.os.Bundle;


import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.movieapp.Fragment.HomeFragment;
import com.example.movieapp.Fragment.ProfileFragment;
import com.example.movieapp.Fragment.SearchFragment;
import com.example.movieapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private String name, email;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // activity_main có container fragmentContainer
        EdgeToEdge.enable(this);
        bottomNavigationView = findViewById(R.id.bottomNav);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");

        // Mặc định load HomeFragment
        loadFragment(new HomeFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int id = item.getItemId();
            if (id == R.id.home) {
                selectedFragment = new HomeFragment();
            } else if (id == R.id.search) {
                selectedFragment = new SearchFragment();
            } else if (id == R.id.profile) {
                Bundle bundle= new Bundle();
                bundle.putString("name", name);
                bundle.putString("email", email);
                selectedFragment = new ProfileFragment();
                selectedFragment.setArguments(bundle);
            }


            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}

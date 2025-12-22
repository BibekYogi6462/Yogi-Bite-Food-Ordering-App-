package com.bca6th.bibekyogi.foodorderingsystemyogibyte;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        SharedPreferences sharedpreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        CardView exit = findViewById(R.id.cardExit);

        // Check if this is the FIRST TIME opening HomeActivity after login
        boolean firstTimeHome = sharedpreferences.getBoolean("first_time_home", true);

        if (firstTimeHome) {
            // This is first time after login, show toast
            Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();

            String username = sharedpreferences.getString("username", "");
            if (!username.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Welcome " + username, Toast.LENGTH_SHORT).show();
            }

            // Set flag to false so toast won't show again
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("first_time_home", false);
            editor.apply();
        }

        exit.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.putBoolean("first_time_home", true); // Reset for next login
            editor.apply();

            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        });


        CardView findRestaurant  = findViewById(R.id.findRestaurant);

        findRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, FindRestaurantActivity.class));
            }
        });



    }
}
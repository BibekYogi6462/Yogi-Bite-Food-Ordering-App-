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

public class HomeActivity extends AppCompatActivity {

    int userId = -1; // Store user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Get user ID from intent (passed from LoginActivity)
        Intent receivedIntent = getIntent(); // Renamed to avoid conflict
        if (receivedIntent != null && receivedIntent.hasExtra("USER_ID")) {
            userId = receivedIntent.getIntExtra("USER_ID", -1);
        }

        SharedPreferences sharedpreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);

        // If no user ID from intent, try to get from SharedPreferences
        if (userId == -1) {
            userId = sharedpreferences.getInt("user_id", -1);
        }

        // Save user ID to SharedPreferences if not already saved
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (!sharedpreferences.contains("user_id") && userId != -1) {
            editor.putInt("user_id", userId);
        }
        editor.apply();

        // Initialize all cards
        CardView exit = findViewById(R.id.cardExit);
        CardView findRestaurant = findViewById(R.id.findRestaurant);
        CardView orderFoodCard = findViewById(R.id.orderFood);
        CardView myOrdersCard = findViewById(R.id.orderDetails);
        CardView offersCard = findViewById(R.id.offer);
        CardView supportCard = findViewById(R.id.helpSupport);

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
            editor.putBoolean("first_time_home", false);
            editor.apply();
        }

        // ========== CARD CLICK LISTENERS ==========

        // Order Food Card
        orderFoodCard.setOnClickListener(v -> {
            if (userId == -1) {
                Toast.makeText(HomeActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            } else {
                Intent foodIntent = new Intent(HomeActivity.this, OrderFoodActivity.class);
                foodIntent.putExtra("USER_ID", userId);
                startActivity(foodIntent);
            }
        });

        // My Orders Card - THIS WAS MISSING!
        myOrdersCard.setOnClickListener(v -> {
            if (userId == -1) {
                Toast.makeText(HomeActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            } else {
                Intent ordersIntent = new Intent(HomeActivity.this, MyOrdersActivity.class);
                ordersIntent.putExtra("USER_ID", userId); // Pass user ID
                startActivity(ordersIntent);
            }
        });

        // Find Restaurant Card
        findRestaurant.setOnClickListener(v -> {
            if (userId == -1) {
                Toast.makeText(HomeActivity.this, "Please login again", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            } else {
                Intent restaurantIntent = new Intent(HomeActivity.this, FindRestaurantActivity.class);
                restaurantIntent.putExtra("USER_ID", userId);
                startActivity(restaurantIntent);
            }
        });

        // Offers Card - Fixed variable name conflict
        offersCard.setOnClickListener(v -> {
            Intent offersIntent = new Intent(HomeActivity.this, OffersActivity.class);
            startActivity(offersIntent);
// Use default animation
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Support Card
        // In HomeActivity.java, update the support card click listener:
        supportCard.setOnClickListener(v -> {
            Intent supportIntent = new Intent(HomeActivity.this, SupportActivity.class);
            startActivity(supportIntent);
        });

        // Exit Button
        exit.setOnClickListener(v -> {
            SharedPreferences.Editor logoutEditor = sharedpreferences.edit();
            logoutEditor.clear();
            logoutEditor.putBoolean("first_time_home", true); // Reset for next login
            logoutEditor.apply();

            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh user ID from SharedPreferences when returning to HomeActivity
        SharedPreferences sharedpreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        userId = sharedpreferences.getInt("user_id", -1);
    }
}
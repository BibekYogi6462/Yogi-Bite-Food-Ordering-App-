package com.bca6th.bibekyogi.foodorderingsystemyogibyte;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class RestaurantDetailsActivity extends AppCompatActivity {

    int userId = -1; // Add this variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        // FIRST: Get user ID from FindRestaurantActivity
        Intent intent = getIntent();
        userId = intent.getIntExtra("USER_ID", -1);

        // If still no user ID, check SharedPreferences
        if (userId == -1) {
            android.content.SharedPreferences prefs = getSharedPreferences("shared_prefs", MODE_PRIVATE);
            userId = prefs.getInt("user_id", -1);
        }

        // Get restaurant data from intent
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        double rating = intent.getDoubleExtra("rating", 4.0);
        String deliveryTime = intent.getStringExtra("deliveryTime");
        String price = intent.getStringExtra("price");
        int imageResId = intent.getIntExtra("imageResId", R.drawable.restaurant1);

        // Initialize views
        TextView restaurantTitle = findViewById(R.id.restaurantTitle);
        ImageView restaurantImage = findViewById(R.id.restaurantImage);
        TextView ratingText = findViewById(R.id.ratingText);
        TextView restaurantDescription = findViewById(R.id.restaurantDescription);
        TextView deliveryTimeText = findViewById(R.id.deliveryTimeText);
        TextView priceText = findViewById(R.id.priceText);
        ImageView backButton = findViewById(R.id.backButton);

        // Set data to views
        if (title != null) {
            restaurantTitle.setText(title);
        }

        if (imageResId != 0) {
            restaurantImage.setImageResource(imageResId);
        }

        ratingText.setText(String.valueOf(rating));

        if (description != null) {
            restaurantDescription.setText(description);
        }

        if (deliveryTime != null) {
            deliveryTimeText.setText(deliveryTime);
        }

        if (price != null) {
            priceText.setText(price);
        }

        // Back button click listener
        backButton.setOnClickListener(v -> {
            finish();
        });

        // View Menu button click listener
        findViewById(R.id.btnViewMenu).setOnClickListener(v -> {
            Toast.makeText(this, "Opening menu for " + title, Toast.LENGTH_SHORT).show();
            // You can start a new activity for the full menu here
            // Intent menuIntent = new Intent(this, MenuActivity.class);
            // menuIntent.putExtra("restaurantName", title);
            // startActivity(menuIntent);
        });

        // Menu item click listeners (example)
        findViewById(R.id.item1Name).setOnClickListener(v -> {
            Toast.makeText(this, "Added Chicken Momo to cart", Toast.LENGTH_SHORT).show();
        });

        CardView btnBookSeat = findViewById(R.id.btnBookSeat);

        btnBookSeat.setOnClickListener(v -> {
            // Check if user is logged in
            if (userId == -1) {
                Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent bookIntent = new Intent(RestaurantDetailsActivity.this, BookSeatActivity.class);
            bookIntent.putExtra("USER_ID", userId); // PASS USER ID HERE
            startActivity(bookIntent);
        });


        // Add this in onCreate() method after finding other buttons:
        CardView btnViewBookings = findViewById(R.id.btnViewBookings);
        btnViewBookings.setOnClickListener(v -> {
            // Check if user is logged in
            if (userId == -1) {
                Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
                return;
            }

            // Go to ViewBookingsActivity
            Intent bookingsIntent = new Intent(RestaurantDetailsActivity.this, ViewBookingsActivity.class);
            bookingsIntent.putExtra("USER_ID", userId);
            startActivity(bookingsIntent);
        });



    }
}
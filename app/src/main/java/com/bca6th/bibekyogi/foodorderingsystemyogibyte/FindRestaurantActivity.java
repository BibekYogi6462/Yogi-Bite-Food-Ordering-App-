package com.bca6th.bibekyogi.foodorderingsystemyogibyte;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class FindRestaurantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_find_restaurant);

        ImageView backButton = findViewById(R.id.backButton);

        // Using lambda expression (cleaner and more concise)
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(FindRestaurantActivity.this, HomeActivity.class));
            finish(); // Optional: close current activity
        });
    }
}
package com.bca6th.bibekyogi.foodorderingsystemyogibyte;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class FindRestaurantActivity extends AppCompatActivity {

    int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_find_restaurant);

        // Get user ID from HomeActivity
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("USER_ID")) {
            userId = intent.getIntExtra("USER_ID", -1);
        }

        // If still no user ID, check SharedPreferences
        if (userId == -1) {
            android.content.SharedPreferences prefs = getSharedPreferences("shared_prefs", MODE_PRIVATE);
            userId = prefs.getInt("user_id", -1);
        }

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Restaurant 1 - PASS USER ID
        findViewById(R.id.res1).setOnClickListener(v -> {
            if (userId == -1) {
                Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent it = new Intent(FindRestaurantActivity.this, RestaurantDetailsActivity.class);
            it.putExtra("title", "Momo Junction");
            it.putExtra("description", "A paradise for momo lovers...");
            it.putExtra("rating", 4.5);
            it.putExtra("USER_ID", userId); // PASS USER ID HERE
            startActivity(it);
        });

        // Restaurant 2 - PASS USER ID
        findViewById(R.id.res2).setOnClickListener(v -> {
            if (userId == -1) {
                Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent it = new Intent(FindRestaurantActivity.this, RestaurantDetailsActivity.class);
            it.putExtra("title", "Terai Spicy");
            it.putExtra("description", "Experience the bold and fiery flavors...");
            it.putExtra("rating", 4.7);
            it.putExtra("USER_ID", userId); // PASS USER ID HERE
            startActivity(it);
        });

        // Restaurant 3 - PASS USER ID
        findViewById(R.id.res3).setOnClickListener(v -> {
            if (userId == -1) {
                Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent it = new Intent(FindRestaurantActivity.this, RestaurantDetailsActivity.class);
            it.putExtra("title", "Himalayan Spice");
            it.putExtra("description", "Inspired by the flavors of the Himalayas...");
            it.putExtra("rating", 4.7);
            it.putExtra("USER_ID", userId); // PASS USER ID HERE
            startActivity(it);
        });

        // Restaurant 4 - PASS USER ID
        findViewById(R.id.res4).setOnClickListener(v -> {
            if (userId == -1) {
                Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent it = new Intent(FindRestaurantActivity.this, RestaurantDetailsActivity.class);
            it.putExtra("title", "Negha's Cafe");
            it.putExtra("description", "Negha's Cafe is a unique destination...");
            it.putExtra("rating", 4.7);
            it.putExtra("USER_ID", userId); // PASS USER ID HERE
            startActivity(it);
        });
    }
}


//package com.bca6th.bibekyogi.foodorderingsystemyogibyte;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.cardview.widget.CardView;
//
//public class FindRestaurantActivity extends AppCompatActivity {
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_find_restaurant);
//
//        ImageView backButton = findViewById(R.id.backButton);
//        backButton.setOnClickListener(v -> finish()); // Just finish(), no need for new Intent
//
//        // Restaurant 1
//        findViewById(R.id.res1).setOnClickListener(v -> {
//            Intent it = new Intent(FindRestaurantActivity.this, RestaurantDetailsActivity.class);
//            it.putExtra("title", "Momo Junction");
////            it.putExtra("description", "Best momos and Nepali street food");
//            it.putExtra(
//                    "description",
//                    "A paradise for momo lovers, Momo Junction serves freshly steamed and fried momos packed with rich flavors. "
//                            + "From classic chicken and veg to spicy buff and special sauces, this place brings the true taste of Nepali "
//                            + "street food in a cozy and vibrant atmosphere."
//            );
//
//            it.putExtra("rating", 4.5);
//            startActivity(it);
//        });
//
//        // Restaurant 2
//        findViewById(R.id.res2).setOnClickListener(v -> {
//            Intent it = new Intent(FindRestaurantActivity.this, RestaurantDetailsActivity.class);
//            it.putExtra("title", "Terai Spicy");
////            it.putExtra("description", "Authentic Italian cuisine");
//            it.putExtra(
//                    "description",
//                    "Experience the bold and fiery flavors of the Terai region. Terai Spicy is known for its rich spices, "
//                            + "aromatic curries, and perfectly balanced heat. Every dish is prepared using traditional recipes that "
//                            + "deliver an unforgettable taste of authentic regional cuisine."
//            );
//            it.putExtra("rating", 4.7);
//
//            startActivity(it);
//        });
//
//        // Restaurant 3
//        findViewById(R.id.res3).setOnClickListener(v -> {
//            Intent it = new Intent(FindRestaurantActivity.this, RestaurantDetailsActivity.class);
//            it.putExtra("title", "Himalayan Spice");
////            it.putExtra("description", "Authentic Nepali and Indian cuisine");
//            it.putExtra(
//                    "description",
//                    "Inspired by the flavors of the Himalayas, Himalayan Spice offers a delightful mix of Nepali and Indian dishes. "
//                            + "From warming curries to flavorful tandoori items, each meal reflects authentic spices, fresh ingredients, "
//                            + "and a homely dining experience."
//            );
//
//            it.putExtra("rating", 4.7);
//            startActivity(it);
//        });
//
//        // Restaurant 4
//        findViewById(R.id.res4).setOnClickListener(v -> {
//            Intent it = new Intent(FindRestaurantActivity.this, RestaurantDetailsActivity.class);
//            it.putExtra("title", "Negha's Cafe");
////            it.putExtra("description", "Authentic Russian cuisine");
//            it.putExtra(
//                    "description",
//                    "Negha's Cafe is a unique destination offering a fusion of authentic Russian cuisine and café-style comfort. "
//                            + "Enjoy rich flavors, freshly brewed beverages, and a peaceful ambiance that makes it perfect for relaxed "
//                            + "meetups, work sessions, or casual dining."
//            );
//
//            it.putExtra("rating", 4.7);
//            startActivity(it);
//        });
//    }
//
//    }

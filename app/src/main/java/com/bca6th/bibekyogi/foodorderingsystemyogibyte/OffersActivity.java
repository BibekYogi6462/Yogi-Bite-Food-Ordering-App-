package com.bca6th.bibekyogi.foodorderingsystemyogibyte;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class OffersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);

        // Back button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Setup RecyclerView
        RecyclerView offersRecyclerView = findViewById(R.id.offersRecyclerView);
        offersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create sample offers data - using existing drawables
        List<Offer> offersList = new ArrayList<>();
        offersList.add(new Offer("50% OFF", "On First Order", "Use Code: WELCOME50", R.drawable.ic_offers, "#FF6B8B"));
        offersList.add(new Offer("Free Delivery", "On orders above ₹299", "Valid till Dec 31", R.drawable.ic_food, "#4ECDC4"));
        offersList.add(new Offer("Buy 1 Get 1", "On All Pizzas", "Weekend Special", R.drawable.ic_orders, "#45B7D1"));
        offersList.add(new Offer("30% OFF", "On Chinese Food", "Use Code: CHINESE30", R.drawable.ic_restaurant, "#96CEB4"));
        offersList.add(new Offer("Extra 10%", "With Credit Card", "ICICI Bank Users", R.drawable.ic_support, "#FFEAA7"));

        OffersAdapter offersAdapter = new OffersAdapter(offersList);
        offersRecyclerView.setAdapter(offersAdapter);
    }
}
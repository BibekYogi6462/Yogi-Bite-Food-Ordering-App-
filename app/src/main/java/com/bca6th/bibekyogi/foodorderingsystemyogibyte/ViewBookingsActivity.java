package com.bca6th.bibekyogi.foodorderingsystemyogibyte;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ViewBookingsActivity extends AppCompatActivity {

    private Database dbHelper;
    private int userId = -1;
    private LinearLayout bookingsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bookings);

        // Get user ID
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("USER_ID")) {
            userId = intent.getIntExtra("USER_ID", -1);
        }

        if (userId == -1) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dbHelper = new Database(this);
        bookingsContainer = findViewById(R.id.bookingsContainer);

        // Back button
        CardView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        loadBookings();
    }

    private void loadBookings() {
        bookingsContainer.removeAllViews();

        Cursor cursor = dbHelper.getBookingsByUserId(userId);

        if (cursor == null || cursor.getCount() == 0) {
            TextView noBookings = new TextView(this);
            noBookings.setText("No bookings found. Book a seat first!");
            noBookings.setTextSize(16);
            noBookings.setPadding(32, 32, 32, 32);
            noBookings.setGravity(android.view.Gravity.CENTER);
            bookingsContainer.addView(noBookings);
            return;
        }

        while (cursor.moveToNext()) {
            // Create booking card
            CardView bookingCard = new CardView(this);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, 16);
            bookingCard.setLayoutParams(cardParams);
            bookingCard.setCardElevation(4);
            bookingCard.setRadius(16);
            bookingCard.setCardBackgroundColor(getResources().getColor(android.R.color.white));

            // Card content
            LinearLayout cardContent = new LinearLayout(this);
            cardContent.setOrientation(LinearLayout.VERTICAL);
            cardContent.setPadding(24, 24, 24, 24);

            // Restaurant name
            TextView nameText = new TextView(this);
            nameText.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            nameText.setTextSize(18);
            nameText.setTextColor(getResources().getColor(android.R.color.black));
            nameText.setTypeface(null, android.graphics.Typeface.BOLD);

            // Phone
            TextView phoneText = new TextView(this);
            phoneText.setText("Phone: " + cursor.getString(cursor.getColumnIndexOrThrow("phone")));
            phoneText.setTextSize(14);
            phoneText.setTextColor(getResources().getColor(android.R.color.darker_gray));
            phoneText.setPadding(0, 4, 0, 0);

            // Date and Time
            TextView dateTimeText = new TextView(this);
            String date = cursor.getString(cursor.getColumnIndexOrThrow("booking_date"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("booking_time"));
            dateTimeText.setText("Date: " + date + " | Time: " + time);
            dateTimeText.setTextSize(14);
            dateTimeText.setTextColor(getResources().getColor(android.R.color.darker_gray));
            dateTimeText.setPadding(0, 4, 0, 0);

            // Seats
            TextView seatsText = new TextView(this);
            int seats = cursor.getInt(cursor.getColumnIndexOrThrow("seats"));
            seatsText.setText("Seats: " + seats);
            seatsText.setTextSize(14);
            seatsText.setTextColor(getResources().getColor(android.R.color.darker_gray));
            seatsText.setPadding(0, 4, 0, 0);

            // Add views to card
            cardContent.addView(nameText);
            cardContent.addView(phoneText);
            cardContent.addView(dateTimeText);
            cardContent.addView(seatsText);

            bookingCard.addView(cardContent);
            bookingsContainer.addView(bookingCard);
        }

        cursor.close();
    }
}
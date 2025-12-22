package com.bca6th.bibekyogi.foodorderingsystemyogibyte;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.Calendar;
import java.util.Locale;

public class BookSeatActivity extends AppCompatActivity {

    EditText etName, etPhone, etDate, etTime, etSeats;
    CardView btnSubmit;
    Database dbHelper;
    int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_seat);

        // First try to get user ID from intent
        userId = getUserIdFromIntent();

        // If not found in intent, try SharedPreferences
        if (userId == -1) {
            userId = getUserIdFromPrefs();
        }

        // If still not found, try to get from LoginActivity's SharedPreferences
        if (userId == -1) {
            userId = getUserIdFromLoginPrefs();
        }

        // Check if user is logged in
        if (userId == -1) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        dbHelper = new Database(this);
        initializeViews();
        setupListeners();

        // Optional: Auto-fill user details if available
        autoFillUserDetails();

        // Debug: Show user ID (remove in production)
        // Toast.makeText(this, "User ID: " + userId, Toast.LENGTH_SHORT).show();
    }

    private int getUserIdFromIntent() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("USER_ID")) {
            return intent.getIntExtra("USER_ID", -1);
        }
        return -1;
    }

    private int getUserIdFromPrefs() {
        // Try the main SharedPreferences
        SharedPreferences prefs = getSharedPreferences("shared_prefs", MODE_PRIVATE);
        return prefs.getInt("user_id", -1);
    }

    private int getUserIdFromLoginPrefs() {
        // Try alternative SharedPreferences name if exists
        SharedPreferences prefs = getSharedPreferences("shared_prefs", MODE_PRIVATE);
        return prefs.getInt("USER_ID", -1); // Try uppercase too
    }

    private void initializeViews() {
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etSeats = findViewById(R.id.etSeats);
        btnSubmit = findViewById(R.id.btnSubmit);
    }

    private void setupListeners() {
        // Date Picker
        etDate.setOnClickListener(v -> showDatePicker());

        // Time Picker
        etTime.setOnClickListener(v -> showTimePicker());

        // Submit Button
        btnSubmit.setOnClickListener(v -> submitBooking());
    }

    private void autoFillUserDetails() {
        // Get user details from database and auto-fill name/phone if available
        try {
            Cursor cursor = dbHelper.getUserById(userId);
            if (cursor != null && cursor.moveToFirst()) {
                // Get username from database
                int usernameIndex = cursor.getColumnIndex("username");
                if (usernameIndex != -1) {
                    String username = cursor.getString(usernameIndex);
                    etName.setText(username);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void submitBooking() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String seatsStr = etSeats.getText().toString().trim();

        // Validation
        if (name.isEmpty() || phone.isEmpty() || date.isEmpty() || time.isEmpty() || seatsStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int seats;
        try {
            seats = Integer.parseInt(seatsStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (seats <= 0 || seats > 20) {
            Toast.makeText(this, "Please enter valid number of seats (1-20)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Phone validation (basic)
        if (phone.length() < 10) {
            Toast.makeText(this, "Please enter a valid phone number (10+ digits)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save booking to database
        boolean isSuccess = dbHelper.addBooking(userId, name, phone, date, time, seats);

        if (isSuccess) {
            Toast.makeText(this, "Booking confirmed successfully!", Toast.LENGTH_SHORT).show();

            // Clear form
            etPhone.setText("");
            etDate.setText("");
            etTime.setText("");
            etSeats.setText("");

            // Optionally go back or show confirmation
            // finish(); // Go back to previous activity

        } else {
            Toast.makeText(this, "Booking failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    etDate.setText(date);
                },
                year, month, day
        );

        // Set minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, selectedHour, selectedMinute) -> {
                    String time = String.format(Locale.getDefault(),
                            "%02d:%02d", selectedHour, selectedMinute);
                    etTime.setText(time);
                },
                hour, minute, false
        );
        timePickerDialog.show();
    }
}


//package com.bca6th.bibekyogi.foodorderingsystemyogibyte;
//
//import android.app.DatePickerDialog;
//import android.app.TimePickerDialog;
//import android.content.Intent;
//import android.database.Cursor;  // ADD THIS LINE
//import android.os.Bundle;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.cardview.widget.CardView;
//
//import java.util.Calendar;
//import java.util.Locale;
//
//public class BookSeatActivity extends AppCompatActivity {
//
//    EditText etName, etPhone, etDate, etTime, etSeats;
//    CardView btnSubmit;
//    Database dbHelper;
//    int userId = -1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_book_seat);
//
//        // Get user ID from intent (passed from login)
//        Intent intent = getIntent();
//        if (intent != null && intent.hasExtra("USER_ID")) {
//            userId = intent.getIntExtra("USER_ID", -1);
//        }
//
//        // If no user ID from intent, try to get from SharedPreferences
//        if (userId == -1) {
//            userId = getUserIdFromPrefs();
//        }
//
//        // Check if user is logged in
//        if (userId == -1) {
//            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        dbHelper = new Database(this);
//        initializeViews();
//        setupListeners();
//
//        // Optional: Auto-fill user details if available
//        autoFillUserDetails();
//    }
//
//    private void initializeViews() {
//        etName = findViewById(R.id.etName);
//        etPhone = findViewById(R.id.etPhone);
//        etDate = findViewById(R.id.etDate);
//        etTime = findViewById(R.id.etTime);
//        etSeats = findViewById(R.id.etSeats);
//        btnSubmit = findViewById(R.id.btnSubmit);
//    }
//
//    private void setupListeners() {
//        // Date Picker
//        etDate.setOnClickListener(v -> showDatePicker());
//
//        // Time Picker
//        etTime.setOnClickListener(v -> showTimePicker());
//
//        // Submit Button
//        btnSubmit.setOnClickListener(v -> submitBooking());
//    }
//
//    private void autoFillUserDetails() {
//        // Get user details from database and auto-fill name/phone if available
//        Cursor cursor = dbHelper.getUserById(userId);
//        if (cursor != null && cursor.moveToFirst()) {
//            // You might want to add a phone field to users table
//            // For now, we'll just show the username as name
//            String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
//            etName.setText(username);
//            cursor.close();
//        }
//    }
//
//    private void submitBooking() {
//        String name = etName.getText().toString().trim();
//        String phone = etPhone.getText().toString().trim();
//        String date = etDate.getText().toString().trim();
//        String time = etTime.getText().toString().trim();
//        String seatsStr = etSeats.getText().toString().trim();
//
//        // Validation
//        if (name.isEmpty() || phone.isEmpty() || date.isEmpty() || time.isEmpty() || seatsStr.isEmpty()) {
//            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        int seats;
//        try {
//            seats = Integer.parseInt(seatsStr);
//        } catch (NumberFormatException e) {
//            Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (seats <= 0 || seats > 20) {
//            Toast.makeText(this, "Please enter valid number of seats (1-20)", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Phone validation (basic)
//        if (phone.length() < 10) {
//            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Save booking to database
//        boolean isSuccess = dbHelper.addBooking(userId, name, phone, date, time, seats);
//
//        if (isSuccess) {
//            Toast.makeText(this, "Booking confirmed successfully!", Toast.LENGTH_SHORT).show();
//
//            // Clear form (optional)
//            etName.setText("");
//            etPhone.setText("");
//            etDate.setText("");
//            etTime.setText("");
//            etSeats.setText("");
//
//            // Navigate to confirmation or home
//            // Intent confirmIntent = new Intent(this, BookingConfirmationActivity.class);
//            // confirmIntent.putExtra("USER_ID", userId);
//            // startActivity(confirmIntent);
//        } else {
//            Toast.makeText(this, "Booking failed. Please try again.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private int getUserIdFromPrefs() {
//        // Create a simple SharedPreferences helper method
//        android.content.SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
//        return prefs.getInt("USER_ID", -1);
//    }
//
//    private void showDatePicker() {
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        DatePickerDialog datePickerDialog = new DatePickerDialog(
//                this,
//                (view, selectedYear, selectedMonth, selectedDay) -> {
//                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
//                    etDate.setText(date);
//                },
//                year, month, day
//        );
//
//        // Set minimum date to today
//        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//        datePickerDialog.show();
//    }
//
//    private void showTimePicker() {
//        Calendar calendar = Calendar.getInstance();
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//
//        TimePickerDialog timePickerDialog = new TimePickerDialog(
//                this,
//                (view, selectedHour, selectedMinute) -> {
//                    String time = String.format(Locale.getDefault(),
//                            "%02d:%02d", selectedHour, selectedMinute);
//                    etTime.setText(time);
//                },
//                hour, minute, false
//        );
//        timePickerDialog.show();
//    }
//}
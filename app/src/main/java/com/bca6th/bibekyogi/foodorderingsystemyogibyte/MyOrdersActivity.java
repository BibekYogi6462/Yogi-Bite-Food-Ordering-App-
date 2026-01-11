package com.bca6th.bibekyogi.foodorderingsystemyogibyte;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersActivity extends AppCompatActivity {

    private RecyclerView ordersRecyclerView;
    private LinearLayout emptyStateLayout;
    private Database db;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        // Initialize database
        db = new Database(this);

        // Get user ID from Intent or SharedPreferences
        currentUserId = getUserId();

        // If user is not logged in, redirect to login
        if (currentUserId == -1) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Initialize views
        ImageView backButton = findViewById(R.id.backButton);
        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);

        // Setup back button
        backButton.setOnClickListener(v -> finish());

        // Setup RecyclerView
        setupRecyclerView();

        // Load orders
        loadOrders();
    }

    private int getUserId() {
        // FIRST: Try to get from Intent (passed from HomeActivity)
        int userId = getIntent().getIntExtra("USER_ID", -1);

        // SECOND: If not in Intent, try SharedPreferences
        if (userId == -1) {
            SharedPreferences sharedpreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            userId = sharedpreferences.getInt("user_id", -1);
        }

        return userId; // Returns -1 if not found
    }

    private void setupRecyclerView() {
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ordersRecyclerView.setHasFixedSize(true);
    }

    private void loadOrders() {
        List<Order> orderList = new ArrayList<>();

        // Get orders from database
        Cursor cursor = db.getOrdersByUserId(currentUserId);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Order order = new Order();

                // Get order details
                int idIndex = cursor.getColumnIndex("id");
                if (idIndex != -1) {
                    long orderId = cursor.getLong(idIndex);
                    order.setId(orderId);

                    // Load food items for this order
                    loadFoodItemsForOrder(order, orderId);
                }

                int dateIndex = cursor.getColumnIndex("order_date");
                int timeIndex = cursor.getColumnIndex("order_time");
                int amountIndex = cursor.getColumnIndex("total_amount");
                int statusIndex = cursor.getColumnIndex("status");
                int addressIndex = cursor.getColumnIndex("delivery_address");
                int paymentIndex = cursor.getColumnIndex("payment_method");
                int phoneIndex = cursor.getColumnIndex("phone_number");

                if (dateIndex != -1) order.setOrderDate(cursor.getString(dateIndex));
                if (timeIndex != -1) order.setOrderTime(cursor.getString(timeIndex));
                if (amountIndex != -1) order.setTotalAmount(cursor.getDouble(amountIndex));
                if (statusIndex != -1) order.setStatus(cursor.getString(statusIndex));
                if (addressIndex != -1) order.setDeliveryAddress(cursor.getString(addressIndex));
                if (paymentIndex != -1) order.setPaymentMethod(cursor.getString(paymentIndex));
                if (phoneIndex != -1) order.setPhoneNumber(cursor.getString(phoneIndex));

                orderList.add(order);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Check if we have orders
        if (orderList.isEmpty()) {
            showEmptyState();
        } else {
            showOrdersList(orderList);
        }
    }

    private void loadFoodItemsForOrder(Order order, long orderId) {
        Cursor itemCursor = db.getOrderItems(orderId);

        if (itemCursor != null && itemCursor.moveToFirst()) {
            do {
                OrderItem orderItem = new OrderItem();

                int nameIndex = itemCursor.getColumnIndex("food_name");
                int quantityIndex = itemCursor.getColumnIndex("quantity");
                int priceIndex = itemCursor.getColumnIndex("price");

                if (nameIndex != -1) orderItem.setFoodName(itemCursor.getString(nameIndex));
                if (quantityIndex != -1) orderItem.setQuantity(itemCursor.getInt(quantityIndex));
                if (priceIndex != -1) orderItem.setPrice(itemCursor.getDouble(priceIndex));

                order.addFoodItem(orderItem);
            } while (itemCursor.moveToNext());
            itemCursor.close();
        }
    }

    private void showEmptyState() {
        ordersRecyclerView.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.VISIBLE);
    }

    private void showOrdersList(List<Order> orderList) {
        emptyStateLayout.setVisibility(View.GONE);
        ordersRecyclerView.setVisibility(View.VISIBLE);

        OrderAdapter adapter = new OrderAdapter(orderList);
        ordersRecyclerView.setAdapter(adapter);

        // Set click listener for order items
        adapter.setOnItemClickListener(order -> {
            // Open order details activity
            Intent intent = new Intent(MyOrdersActivity.this, OrderDetailsActivity.class);
            intent.putExtra("order_id", order.getId());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh orders when activity resumes
        loadOrders();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}
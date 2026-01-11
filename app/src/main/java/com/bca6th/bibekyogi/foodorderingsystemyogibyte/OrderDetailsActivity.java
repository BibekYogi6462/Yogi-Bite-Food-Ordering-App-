package com.bca6th.bibekyogi.foodorderingsystemyogibyte;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class OrderDetailsActivity extends AppCompatActivity {

    private Database db;
    private long orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        // Get order ID from intent
        orderId = getIntent().getLongExtra("order_id", -1);

        if (orderId == -1) {
            Toast.makeText(this, "Invalid order", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db = new Database(this);

        // Initialize views
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Load order details
        loadOrderDetails();
    }

    private void loadOrderDetails() {
        Cursor cursor = db.getOrderById(orderId);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                // Find TextViews
                TextView orderIdText = findViewById(R.id.orderIdText);
                TextView orderDateText = findViewById(R.id.orderDateText);
                TextView orderStatusText = findViewById(R.id.orderStatusText);
                TextView deliveryAddressText = findViewById(R.id.deliveryAddressText);
                TextView paymentMethodText = findViewById(R.id.paymentMethodText);
                TextView totalAmountText = findViewById(R.id.totalAmountText);

                // Set order details
                if (orderIdText != null) orderIdText.setText("Order #" + orderId);

                int dateIndex = cursor.getColumnIndex("order_date");
                int timeIndex = cursor.getColumnIndex("order_time");
                if (orderDateText != null && dateIndex != -1 && timeIndex != -1) {
                    orderDateText.setText("Date: " + cursor.getString(dateIndex) +
                            " at " + cursor.getString(timeIndex));
                }

                int statusIndex = cursor.getColumnIndex("status");
                if (orderStatusText != null && statusIndex != -1) {
                    orderStatusText.setText("Status: " + cursor.getString(statusIndex));
                }

                int addressIndex = cursor.getColumnIndex("delivery_address");
                if (deliveryAddressText != null && addressIndex != -1) {
                    deliveryAddressText.setText("Address: " + cursor.getString(addressIndex));
                }

                int paymentIndex = cursor.getColumnIndex("payment_method");
                if (paymentMethodText != null && paymentIndex != -1) {
                    paymentMethodText.setText("Payment: " + cursor.getString(paymentIndex));
                }

                int amountIndex = cursor.getColumnIndex("total_amount");
                if (totalAmountText != null && amountIndex != -1) {
                    totalAmountText.setText("Total: $" +
                            String.format("%.2f", cursor.getDouble(amountIndex)));
                }

            } catch (Exception e) {
                Toast.makeText(this, "Error loading order details", Toast.LENGTH_SHORT).show();
            } finally {
                cursor.close();
            }
        } else {
            Toast.makeText(this, "Order not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}
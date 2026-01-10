package com.bca6th.bibekyogi.foodorderingsystemyogibyte;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    private TextView totalAmountTextView, deliveryFeeTextView, grandTotalTextView;
    private EditText edtName, edtPhone, edtAddress, edtInstructions;
    private RadioGroup paymentMethodGroup;
    private Button btnPlaceOrder;
    private ImageView backButton;

    private double cartTotal = 0.0;
    private double deliveryFee = 2.99;
    private double grandTotal = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Get cart total from intent
        cartTotal = getIntent().getDoubleExtra("cart_total", 0.0);
        grandTotal = cartTotal + deliveryFee;

        // Initialize views
        initializeViews();
        setupListeners();
        updateUI();
    }

    private void initializeViews() {
        totalAmountTextView = findViewById(R.id.totalAmount);
        deliveryFeeTextView = findViewById(R.id.deliveryFee);
        grandTotalTextView = findViewById(R.id.grandTotal);

        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        edtInstructions = findViewById(R.id.edtInstructions);

        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        backButton = findViewById(R.id.backButton);
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> finish());

        btnPlaceOrder.setOnClickListener(v -> {
            if (validateInputs()) {
                placeOrder();
            }
        });

        // Update payment method selection
        paymentMethodGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // You can add logic for different payment methods
        });
    }

    private void updateUI() {
        totalAmountTextView.setText(String.format("$%.2f", cartTotal));
        deliveryFeeTextView.setText(String.format("$%.2f", deliveryFee));
        grandTotalTextView.setText(String.format("$%.2f", grandTotal));
    }

    private boolean validateInputs() {
        if (edtName.getText().toString().trim().isEmpty()) {
            edtName.setError("Name is required");
            edtName.requestFocus();
            return false;
        }

        if (edtPhone.getText().toString().trim().isEmpty()) {
            edtPhone.setError("Phone number is required");
            edtPhone.requestFocus();
            return false;
        }

        if (edtAddress.getText().toString().trim().isEmpty()) {
            edtAddress.setError("Delivery address is required");
            edtAddress.requestFocus();
            return false;
        }

        if (paymentMethodGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void placeOrder() {
        try {
            // Get user details
            String name = edtName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();
            String instructions = edtInstructions.getText().toString().trim();

            // Get selected payment method
            int selectedId = paymentMethodGroup.getCheckedRadioButtonId();
            RadioButton selectedPayment = findViewById(selectedId);
            String paymentMethod = selectedPayment.getText().toString();

            // Get current date and time
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

            // Here you would typically:
            // 1. Get current user ID from SharedPreferences or intent
            // 2. Save order to database
            // 3. Clear cart
            // 4. Navigate to order confirmation

            // For now, we'll simulate order placement
            Database db = new Database(this);

            // Get user ID (you should pass this from previous activity or get from SharedPreferences)
            int userId = getUserId(); // Implement this method

            if (userId != -1) {
                // Create order in database
                long orderId = db.createNewOrder(userId, currentDate, currentTime,
                        grandTotal, address, paymentMethod, phone);

                if (orderId != -1) {
                    // Order created successfully
                    Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();

                    // Navigate to order confirmation
                    Intent intent = new Intent(this, OrderConfirmationActivity.class);
                    intent.putExtra("order_id", orderId);
                    intent.putExtra("total_amount", grandTotal);
                    startActivity(intent);
                    finish(); // Close checkout activity
                } else {
                    Toast.makeText(this, "Failed to place order. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "User not logged in. Please login again.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error placing order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private int getUserId() {
        // Implement this method to get current user ID
        // You can use SharedPreferences or pass from previous activity
        // For now, return a dummy value
        return 1; // Replace with actual user ID retrieval
    }
}
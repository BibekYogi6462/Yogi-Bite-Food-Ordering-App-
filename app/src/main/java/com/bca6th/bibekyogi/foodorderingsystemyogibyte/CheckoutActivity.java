package com.bca6th.bibekyogi.foodorderingsystemyogibyte;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private final double deliveryFee = 2.99;
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
    }

    private void updateUI() {
        totalAmountTextView.setText(String.format(Locale.getDefault(), "$%.2f", cartTotal));
        deliveryFeeTextView.setText(String.format(Locale.getDefault(), "$%.2f", deliveryFee));
        grandTotalTextView.setText(String.format(Locale.getDefault(), "$%.2f", grandTotal));
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
        Database db = null;
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

            // Get user ID (you should pass this from previous activity or get from SharedPreferences)
            int userId = getUserId();

            if (userId != -1) {
                db = new Database(this);

                // Create order in database using the correct method name
                long orderId = db.createOrder(userId, currentDate, currentTime,
                        grandTotal, address, paymentMethod, phone);

                if (orderId != -1) {
                    // Order created successfully
                    Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();

                    // TODO: Here you need to add the cart items to order_items table
                    // using db.addOrderItem(orderId, foodName, quantity, price);

                    // Navigate to confirmation or back to home
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.putExtra("order_id", orderId);
                    intent.putExtra("total_amount", grandTotal);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); // Close checkout activity
                } else {
                    Toast.makeText(this, "Failed to place order. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "User not logged in. Please login again.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }

        } catch (Exception e) {
            Log.e("CheckoutActivity", "Error placing order", e);
            Toast.makeText(this, "Error placing order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    private int getUserId() {
        // Implement this method to get current user ID
        // You can use SharedPreferences
        // For now, return a dummy value or get from intent
        return getIntent().getIntExtra("user_id", 1); // Default to 1 for testing
    }
}
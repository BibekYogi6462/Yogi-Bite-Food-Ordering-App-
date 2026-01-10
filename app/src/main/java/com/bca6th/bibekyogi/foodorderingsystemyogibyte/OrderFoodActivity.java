package com.bca6th.bibekyogi.foodorderingsystemyogibyte;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class OrderFoodActivity extends AppCompatActivity {

    private RecyclerView foodRecyclerView, cartRecyclerView;
    private FoodAdapter foodAdapter;
    private CartAdapter cartAdapter;
    private List<FoodItem> foodList = new ArrayList<>();
    private List<CartItem> cartList = new ArrayList<>();
    private Database db;
    private TextView cartTotalTextView, cartEmptyTextView;
    private Button checkoutButton;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_food);

        db = new Database(this);

        // Initialize views
        backButton = findViewById(R.id.backButton);
        foodRecyclerView = findViewById(R.id.foodRecyclerView);
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartTotalTextView = findViewById(R.id.cartTotal);
        cartEmptyTextView = findViewById(R.id.cartEmpty);
        checkoutButton = findViewById(R.id.checkoutButton);

        // Setup back button
        backButton.setOnClickListener(v -> finish());

        // Setup RecyclerViews
        setupFoodRecyclerView();
        setupCartRecyclerView();

        // Load food items
        loadFoodItems();

        // Setup checkout button
        checkoutButton.setOnClickListener(v -> {
            if (cartList.isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                proceedToCheckout();
            }
        });

        updateCartUI();
    }

    private void setupFoodRecyclerView() {
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodAdapter = new FoodAdapter(foodList, new FoodAdapter.OnItemClickListener() {
            @Override
            public void onAddToCart(FoodItem foodItem) {
                addToCart(foodItem);
            }
        });
        foodRecyclerView.setAdapter(foodAdapter);
    }

    private void setupCartRecyclerView() {
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(cartList, new CartAdapter.CartListener() {
            @Override
            public void onQuantityChanged(int position, int newQuantity) {
                updateCartItemQuantity(position, newQuantity);
            }

            @Override
            public void onRemoveItem(int position) {
                removeFromCart(position);
            }
        });
        cartRecyclerView.setAdapter(cartAdapter);
    }

    private void loadFoodItems() {
        // Sample food items - in real app, you'd fetch from database/API
        foodList.clear();
        foodList.add(new FoodItem(1, "Cheese Pizza", "Classic cheese pizza with mozzarella", 12.99, R.drawable.pizza));
        foodList.add(new FoodItem(2, "Burger", "Beef burger with cheese and veggies", 8.99, R.drawable.burger));
        foodList.add(new FoodItem(3, "French Fries", "Crispy golden fries", 4.99, R.drawable.fries));
        foodList.add(new FoodItem(4, "Coca Cola", "500ml chilled coke", 2.49, R.drawable.coke));
        foodList.add(new FoodItem(5, "Chicken Wings", "Spicy buffalo wings", 10.99, R.drawable.wings));
        foodList.add(new FoodItem(6, "Caesar Salad", "Fresh salad with croutons", 7.99, R.drawable.salad));
        foodList.add(new FoodItem(7, "Pasta Carbonara", "Creamy pasta with bacon", 11.99, R.drawable.pasta));
        foodList.add(new FoodItem(8, "Ice Cream", "Vanilla ice cream sundae", 5.49, R.drawable.icecream));

        foodAdapter.notifyDataSetChanged();
    }

    private void addToCart(FoodItem foodItem) {
        // Check if item already in cart
        for (CartItem cartItem : cartList) {
            if (cartItem.getFoodId() == foodItem.getId()) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                cartAdapter.notifyDataSetChanged();
                updateCartUI();
                return;
            }
        }

        // Add new item to cart
        cartList.add(new CartItem(
                foodItem.getId(),
                foodItem.getName(),
                foodItem.getPrice(),
                1,
                foodItem.getImageResource()
        ));
        cartAdapter.notifyDataSetChanged();
        updateCartUI();

        Toast.makeText(this, foodItem.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
    }

    private void updateCartItemQuantity(int position, int newQuantity) {
        if (newQuantity <= 0) {
            cartList.remove(position);
        } else {
            cartList.get(position).setQuantity(newQuantity);
        }
        cartAdapter.notifyDataSetChanged();
        updateCartUI();
    }

    private void removeFromCart(int position) {
        cartList.remove(position);
        cartAdapter.notifyDataSetChanged();
        updateCartUI();
    }

    private void updateCartUI() {
        double total = 0;
        for (CartItem item : cartList) {
            total += item.getPrice() * item.getQuantity();
        }

        cartTotalTextView.setText(String.format("$%.2f", total));

        if (cartList.isEmpty()) {
            cartEmptyTextView.setVisibility(View.VISIBLE);
            cartRecyclerView.setVisibility(View.GONE);
            checkoutButton.setEnabled(false);
            checkoutButton.setAlpha(0.5f);
        } else {
            cartEmptyTextView.setVisibility(View.GONE);
            cartRecyclerView.setVisibility(View.VISIBLE);
            checkoutButton.setEnabled(true);
            checkoutButton.setAlpha(1f);
        }
    }

    private void proceedToCheckout() {
        Intent intent = new Intent(this, CheckoutActivity.class);
        intent.putExtra("cart_total", calculateCartTotal());
        startActivity(intent);
    }

    private double calculateCartTotal() {
        double total = 0;
        for (CartItem item : cartList) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }
}
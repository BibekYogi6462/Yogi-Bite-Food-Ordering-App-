package com.bca6th.bibekyogi.foodorderingsystemyogibyte;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FoodOrderingDB";
    private static final int DATABASE_VERSION = 3; // Incremented version to 3

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table with ID
        String qry1 = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE," +
                "email TEXT," +
                "password TEXT)";
        db.execSQL(qry1);

        // Create bookings table
        String qry2 = "CREATE TABLE bookings (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "name TEXT," +
                "phone TEXT," +
                "booking_date TEXT," +
                "booking_time TEXT," +
                "seats INTEGER," +
                "booking_timestamp LONG," +
                "FOREIGN KEY(user_id) REFERENCES users(id))";
        db.execSQL(qry2);

        // Create orders table
        String qry3 = "CREATE TABLE orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "order_date TEXT," +
                "order_time TEXT," +
                "total_amount REAL," +
                "status TEXT DEFAULT 'Pending'," +
                "delivery_address TEXT," +
                "payment_method TEXT," +
                "phone_number TEXT," +
                "order_timestamp LONG," +
                "FOREIGN KEY(user_id) REFERENCES users(id))";
        db.execSQL(qry3);

        // Create order_items table
        String qry4 = "CREATE TABLE order_items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "order_id INTEGER," +
                "food_name TEXT," +
                "quantity INTEGER," +
                "price REAL," +
                "FOREIGN KEY(order_id) REFERENCES orders(id))";
        db.execSQL(qry4);

        Log.d("Database", "All tables created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // For version 1 to 2 upgrade
            db.execSQL("DROP TABLE IF EXISTS bookings");
            db.execSQL("DROP TABLE IF EXISTS users");
            onCreate(db);
        } else if (oldVersion < 3) {
            // For version 2 to 3 upgrade - add order tables
            db.execSQL("DROP TABLE IF EXISTS order_items");
            db.execSQL("DROP TABLE IF EXISTS orders");
            // Recreate the order tables
            String qry3 = "CREATE TABLE orders (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER," +
                    "order_date TEXT," +
                    "order_time TEXT," +
                    "total_amount REAL," +
                    "status TEXT DEFAULT 'Pending'," +
                    "delivery_address TEXT," +
                    "payment_method TEXT," +
                    "phone_number TEXT," +
                    "order_timestamp LONG," +
                    "FOREIGN KEY(user_id) REFERENCES users(id))";
            db.execSQL(qry3);

            String qry4 = "CREATE TABLE order_items (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "order_id INTEGER," +
                    "food_name TEXT," +
                    "quantity INTEGER," +
                    "price REAL," +
                    "FOREIGN KEY(order_id) REFERENCES orders(id))";
            db.execSQL(qry4);
        }
    }

    // Register - returns user ID
    public long register(String username, String email, String password) {
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("email", email);
        cv.put("password", password);

        SQLiteDatabase db = getWritableDatabase();
        long userId = db.insert("users", null, cv);
        db.close();

        return userId; // Returns -1 if failed, or the new row ID if successful
    }

    // Login - returns user ID if successful, -1 if failed
    public int login(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {"id"};
        String selection = "username = ? AND password = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query("users", columns, selection, selectionArgs, null, null, null);

        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }

        cursor.close();
        db.close();
        return userId;
    }

    // Add booking with user ID
    public boolean addBooking(int userId, String name, String phone, String date, String time, int seats) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("user_id", userId);
        values.put("name", name);
        values.put("phone", phone);
        values.put("booking_date", date);
        values.put("booking_time", time);
        values.put("seats", seats);
        values.put("booking_timestamp", System.currentTimeMillis());

        long result = db.insert("bookings", null, values);
        db.close();

        return result != -1;
    }

    // Get all bookings for a specific user
    public Cursor getBookingsByUserId(int userId) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM bookings WHERE user_id = ? ORDER BY booking_timestamp DESC";
        String[] selectionArgs = {String.valueOf(userId)};

        return db.rawQuery(query, selectionArgs);
    }

    // Get user details by ID
    public Cursor getUserById(int userId) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM users WHERE id = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        return db.rawQuery(query, selectionArgs);
    }

    // Check if username already exists
    public boolean checkUsernameExists(String username) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT id FROM users WHERE username = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.rawQuery(query, selectionArgs);
        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();
        return exists;
    }

    // Get all users (for admin purposes)
    public Cursor getAllUsers() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT id, username, email FROM users ORDER BY username", null);
    }

    // Get all bookings (for admin purposes)
    public Cursor getAllBookings() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT b.*, u.username FROM bookings b " +
                "LEFT JOIN users u ON b.user_id = u.id " +
                "ORDER BY b.booking_timestamp DESC";
        return db.rawQuery(query, null);
    }

    // Delete booking
    public boolean deleteBooking(int bookingId) {
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete("bookings", "id = ?", new String[]{String.valueOf(bookingId)});
        db.close();
        return result > 0;
    }

    // ========== ORDER SYSTEM METHODS ==========

    // Create a new order
    public long createOrder(int userId, String orderDate, String orderTime,
                            double totalAmount, String address, String paymentMethod, String phone) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("user_id", userId);
        values.put("order_date", orderDate);
        values.put("order_time", orderTime);
        values.put("total_amount", totalAmount);
        values.put("delivery_address", address);
        values.put("payment_method", paymentMethod);
        values.put("phone_number", phone);
        values.put("order_timestamp", System.currentTimeMillis());

        long orderId = db.insert("orders", null, values);
        db.close();
        return orderId;
    }

    // Add items to an order
    public boolean addOrderItem(long orderId, String foodName, int quantity, double price) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("order_id", orderId);
        values.put("food_name", foodName);
        values.put("quantity", quantity);
        values.put("price", price);

        long result = db.insert("order_items", null, values);
        db.close();
        return result != -1;
    }

    // Get all orders for a specific user
    public Cursor getOrdersByUserId(int userId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_timestamp DESC";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }

    // Get order items for a specific order
    public Cursor getOrderItems(long orderId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM order_items WHERE order_id = ?";
        return db.rawQuery(query, new String[]{String.valueOf(orderId)});
    }

    // Get order details by order ID
    public Cursor getOrderById(long orderId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM orders WHERE id = ?";
        return db.rawQuery(query, new String[]{String.valueOf(orderId)});
    }

    // Update order status
    public boolean updateOrderStatus(long orderId, String status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", status);

        int rowsAffected = db.update("orders", values, "id = ?", new String[]{String.valueOf(orderId)});
        db.close();
        return rowsAffected > 0;
    }

    // Get all orders (for admin purposes)
    public Cursor getAllOrders() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT o.*, u.username FROM orders o " +
                "LEFT JOIN users u ON o.user_id = u.id " +
                "ORDER BY o.order_timestamp DESC";
        return db.rawQuery(query, null);
    }

    // Delete an order
    public boolean deleteOrder(long orderId) {
        SQLiteDatabase db = getWritableDatabase();

        // First delete order items
        db.delete("order_items", "order_id = ?", new String[]{String.valueOf(orderId)});

        // Then delete the order
        int result = db.delete("orders", "id = ?", new String[]{String.valueOf(orderId)});
        db.close();
        return result > 0;
    }

    // Get user's cart count (if you implement a cart table)
    public int getCartItemCount(int userId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT SUM(quantity) as total FROM cart WHERE user_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        int count = 0;
        if (cursor.moveToFirst() && !cursor.isNull(0)) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return count;
    }
}
//package com.bca6th.bibekyogi.foodorderingsystemyogibyte;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import androidx.annotation.Nullable;
//
//public class Database extends SQLiteOpenHelper {
//
//    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//
//        String qry1 = "create table users(username text, email text, password text)";
//        db.execSQL(qry1);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//    }
//
//
//    //Register
//    public void register(String username, String email, String password){
//        ContentValues cv = new ContentValues();
//        cv.put("username", username);
//        cv.put("email", email);
//        cv.put("password", password);
//        SQLiteDatabase db = getWritableDatabase();
//        db.insert("users", null, cv);
//        db.close();
//    }
//
//
//    //Login
//    public int login(String username, String password){
//        int result = 0 ;
//        String str[] = new String[2];
//        str[0] = username;
//        str[1] = password;
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor c = db.rawQuery("select * from users where username=? and password=?", str);
//
//        if(c.moveToFirst()){
//            result=1;
//        }
//
//
//        return result;
//    }
//
//
//
//
//}


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
    private static final int DATABASE_VERSION = 2; // Incremented version

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

        Log.d("Database", "Tables created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // For version 1 to 2 upgrade
            db.execSQL("DROP TABLE IF EXISTS bookings");
            db.execSQL("DROP TABLE IF EXISTS users");
            onCreate(db);
        }
    }

    // Register - returns user ID
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
}
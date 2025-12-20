package com.bca6th.bibekyogi.foodorderingsystemyogibyte;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText edUsername, edEmail, edPassword, edConfirm;
    Button btn;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        edUsername = findViewById(R.id.editTextRegisterUsername);
        edEmail = findViewById(R.id.editTextRegisterEmail);
        edPassword = findViewById(R.id.editTextRegisterPassword);
        edConfirm = findViewById(R.id.editTextRegisterConfirmPassword);
        btn = findViewById(R.id.buttonRegister);
        tv = findViewById(R.id.textViewExistingUser);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edUsername.getText().toString().trim();
                String email = edEmail.getText().toString().trim();
                String password = edPassword.getText().toString();
                String confirm = edConfirm.getText().toString();
                Database db = new Database(getApplicationContext(), "yogieat", null, 1);

                // Check if any field is empty
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Username validation
                if (!isValidUsername(username)) {
                    Toast.makeText(getApplicationContext(), "Username must be 3-20 characters and can only contain letters, numbers, and underscores", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Email validation
                if (!isValidEmail(email)) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Password validation
                if (!isValidPassword(password)) {
                    Toast.makeText(getApplicationContext(),
                            "Password must contain:\n• At least 8 characters\n• One uppercase letter\n• One lowercase letter\n• One number\n• One special character",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                // Confirm password validation
                if (!password.equals(confirm)) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                db.register(username, email, password);
                // All validations passed
                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();

                // TODO: Add your registration logic here (API call, database insertion, etc.)
                // After successful registration, you can redirect to login or main activity
                 startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                // finish();
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    // Username validation method
    private boolean isValidUsername(String username) {
        // Username should be 3-20 characters, alphanumeric with underscores allowed
        String usernamePattern = "^[a-zA-Z0-9_]{3,20}$";
        return username.matches(usernamePattern);
    }

    // Email validation method
    private boolean isValidEmail(String email) {
        // Simple email pattern check
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailPattern);
    }

    // Password validation method
    private boolean isValidPassword(String password) {
        // Password must contain:
        // At least 8 characters
        // At least one uppercase letter
        // At least one lowercase letter
        // At least one digit
        // At least one special character
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(passwordPattern);
    }


}
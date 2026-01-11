package com.bca6th.bibekyogi.foodorderingsystemyogibyte;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class SupportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        // Back button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Initialize support options
        CardView callSupport = findViewById(R.id.callSupport);
        CardView emailSupport = findViewById(R.id.emailSupport);
        CardView chatSupport = findViewById(R.id.chatSupport);
        CardView faqCard = findViewById(R.id.faqCard);
        CardView rateApp = findViewById(R.id.rateApp);
        CardView aboutApp = findViewById(R.id.aboutApp);

        // Call Support
        callSupport.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:+9779800000000"));
            startActivity(callIntent);
        });

        // Email Support
        emailSupport.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:support@yogieat.com"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Yogi Eat Support");
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        });

        // Chat Support (Live Chat)
        chatSupport.setOnClickListener(v -> {
            // You can integrate with a chat SDK like Firebase, SendBird, etc.
            // For now, show a message
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("Live Chat")
                    .setMessage("Live chat feature will be available soon!")
                    .setPositiveButton("OK", null)
                    .show();
        });

        // FAQ
        faqCard.setOnClickListener(v -> {
            Intent faqIntent = new Intent(SupportActivity.this, FAQActivity.class);
            startActivity(faqIntent);
        });

        // Rate App
        rateApp.setOnClickListener(v -> {
            // Open Play Store or App Store
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + getPackageName())));
            } catch (android.content.ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        });

        // About App
        aboutApp.setOnClickListener(v -> {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("About Yogi Eat")
                    .setMessage("Version 1.0.0\n\n" +
                            "Yogi Eat is a food ordering and delivery app that connects you " +
                            "with the best restaurants in your area.\n\n" +
                            "© 2024 Yogi Eat. All rights reserved.")
                    .setPositiveButton("OK", null)
                    .show();
        });

        // Set user email if available
        TextView userEmail = findViewById(R.id.userEmail);
        String savedEmail = getSharedPreferences("shared_prefs", MODE_PRIVATE)
                .getString("email", "user@example.com");
        userEmail.setText(savedEmail);
    }
}
package com.bca6th.bibekyogi.foodorderingsystemyogibyte;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class FAQActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        // Back button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Setup FAQ list
        RecyclerView faqRecyclerView = findViewById(R.id.faqRecyclerView);
        faqRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<FAQItem> faqList = new ArrayList<>();
        faqList.add(new FAQItem("How do I place an order?",
                "Go to Order Food section, select restaurant, choose items, and proceed to checkout."));
        faqList.add(new FAQItem("What payment methods are accepted?",
                "We accept credit/debit cards, digital wallets, and cash on delivery."));
        faqList.add(new FAQItem("How long does delivery take?",
                "Delivery typically takes 30-45 minutes depending on your location."));
        faqList.add(new FAQItem("Can I modify my order?",
                "You can modify your order within 5 minutes of placing it from My Orders section."));
        faqList.add(new FAQItem("How do I apply promo codes?",
                "Enter promo code at checkout page in the 'Apply Coupon' section."));
        faqList.add(new FAQItem("What is your refund policy?",
                "Refunds are processed within 5-7 business days for cancelled orders."));

        FAQAdapter adapter = new FAQAdapter(faqList);
        faqRecyclerView.setAdapter(adapter);
    }
}
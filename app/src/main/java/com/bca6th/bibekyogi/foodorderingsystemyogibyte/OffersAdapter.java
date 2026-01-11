package com.bca6th.bibekyogi.foodorderingsystemyogibyte;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.OfferViewHolder> {

    private List<Offer> offersList;
    private Context context;

    public OffersAdapter(List<Offer> offersList) {
        this.offersList = offersList;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_offer, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        Offer offer = offersList.get(position);

        holder.title.setText(offer.getTitle());
        holder.description.setText(offer.getDescription());
        holder.code.setText(offer.getCode().replace("Use Code: ", ""));

        // Set card background color
        holder.cardView.setCardBackgroundColor(Color.parseColor(offer.getColor()));

        // Copy button click
        holder.copyButton.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Offer Code", offer.getCode());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Code copied: " + offer.getCode(), Toast.LENGTH_SHORT).show();
        });

        // Whole card click
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, offer.getTitle() + " - " + offer.getDescription(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return offersList.size();
    }

    static class OfferViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title, description, code, copyButton;

        OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.offerCard);
            title = itemView.findViewById(R.id.offerTitle);
            description = itemView.findViewById(R.id.offerDescription);
            code = itemView.findViewById(R.id.offerCode);
            copyButton = itemView.findViewById(R.id.copyButton);
        }
    }
}
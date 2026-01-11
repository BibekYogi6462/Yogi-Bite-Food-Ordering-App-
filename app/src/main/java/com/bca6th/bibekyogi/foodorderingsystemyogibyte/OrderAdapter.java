package com.bca6th.bibekyogi.foodorderingsystemyogibyte;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private OnItemClickListener listener;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(Order order);
    }

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.orderId.setText("Order #" + order.getId());
        holder.orderDate.setText(order.getOrderDate() + " at " + order.getOrderTime());
        holder.orderAmount.setText("$" + String.format("%.2f", order.getTotalAmount()));
        holder.orderStatus.setText(order.getStatus());
        holder.paymentMethod.setText("Payment: " + order.getPaymentMethod());

        // Show food items
        StringBuilder foodItemsText = new StringBuilder("Items: ");
        List<OrderItem> foodItemsList = order.getFoodItems();
        if (foodItemsList != null && !foodItemsList.isEmpty()) {
            for (int i = 0; i < Math.min(foodItemsList.size(), 3); i++) {
                OrderItem item = foodItemsList.get(i);
                foodItemsText.append(item.getFoodName())
                        .append(" x")
                        .append(item.getQuantity());
                if (i < Math.min(foodItemsList.size(), 3) - 1) {
                    foodItemsText.append(", ");
                }
            }
            if (foodItemsList.size() > 3) {
                foodItemsText.append("...");
            }
        } else {
            foodItemsText.append("No items");
        }
        holder.foodItems.setText(foodItemsText.toString());

        // Set status color
        String status = order.getStatus().toLowerCase();
        int statusColor = android.R.color.darker_gray;

        if (status.contains("pending")) {
            statusColor = android.R.color.holo_orange_dark;
        } else if (status.contains("confirmed") || status.contains("processing")) {
            statusColor = android.R.color.holo_blue_dark;
        } else if (status.contains("delivered") || status.contains("completed")) {
            statusColor = android.R.color.holo_green_dark;
        } else if (status.contains("cancelled")) {
            statusColor = android.R.color.holo_red_dark;
        }

        holder.orderStatus.setTextColor(context.getResources().getColor(statusColor));

        // Set click listener
        holder.orderCard.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        CardView orderCard;
        TextView orderId, orderDate, orderAmount, orderStatus, paymentMethod, foodItems;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderCard = itemView.findViewById(R.id.orderCard);
            orderId = itemView.findViewById(R.id.orderId);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderAmount = itemView.findViewById(R.id.orderAmount);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            paymentMethod = itemView.findViewById(R.id.paymentMethod);
            foodItems = itemView.findViewById(R.id.foodItems);
        }
    }
}
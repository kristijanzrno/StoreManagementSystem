package Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storemanagementsystem.R;


import java.util.ArrayList;

import Data.CustomerPurchaseItem;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BasketRecyclerViewAdapter extends RecyclerView.Adapter<BasketRecyclerViewAdapter.ViewHolder> {
    private ArrayList<CustomerPurchaseItem> items;
    private BasketItemActions actions;

    public interface BasketItemActions{
        void increaseQuantity(int position);
        void decreaseQuantity(int position);
        void basketRemoveItem(int position);
    }

    public BasketRecyclerViewAdapter(@NonNull ArrayList<CustomerPurchaseItem> items, BasketItemActions actions) {
        this.items = items;
        this.actions = actions;
    }

    @NonNull
    @Override
    public BasketRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.itemName.setText(items.get(position).getItemName());
        holder.quantityTV.setText(""+items.get(position).getQuantity());

        holder.increaseQuantity.setOnClickListener(v -> {
            actions.increaseQuantity(position);
        });

        holder.decreaseQuantity.setOnClickListener(v -> {
            actions.decreaseQuantity(position);
        });
        holder.removeItem.setOnClickListener(v -> {
            actions.basketRemoveItem(position);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.basketItemName)
        TextView itemName;
        @BindView(R.id.quantityTV)
        TextView quantityTV;
        @BindView(R.id.basketIncreaseQuantity)
        CardView increaseQuantity;
        @BindView(R.id.basketDecreaseQuantity)
        CardView decreaseQuantity;
        @BindView(R.id.basketRemoveItem)
        ImageView removeItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

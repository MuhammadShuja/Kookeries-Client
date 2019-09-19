package com.kookeries.shop.ui.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kookeries.shop.R;
import com.kookeries.shop.models.Product;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductGridAdapter extends RecyclerView.Adapter<ProductGridAdapter.ViewHolder> {

    private Context mContext;
    private List<Product> products;
    private OnItemClickListener listener;

    public ProductGridAdapter(Context mContext, List<Product> products, OnItemClickListener listener) {
        this.mContext = mContext;
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_product_vertical, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(products.get(i), listener);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView thumbnail;
        private TextView name;
        private TextView price;
        private TextView oldPrice;
        private TextView discount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            name = (TextView) itemView.findViewById(R.id.name);
            price = (TextView) itemView.findViewById(R.id.price);
            oldPrice = (TextView) itemView.findViewById(R.id.oldPrice);
            oldPrice.setPaintFlags(oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            discount = (TextView) itemView.findViewById(R.id.discount);
        }

        public void bind(final Product product, final OnItemClickListener listener) {
            Glide
                    .with(mContext)
                    .load(product.getThumbnail())
                    .into(thumbnail);

            name.setText(product.getName());
            price.setText(product.getPrice());
            if(product.getOldPrice() != null){
                oldPrice.setText(product.getOldPrice());
                oldPrice.setVisibility(View.VISIBLE);

                discount.setText(product.getDiscount());
                discount.setVisibility(View.VISIBLE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(product);
                }
            });
        }
    }

    public void setData(List<Product> products){
        this.products = products;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onItemClick(Product product);
    }
}
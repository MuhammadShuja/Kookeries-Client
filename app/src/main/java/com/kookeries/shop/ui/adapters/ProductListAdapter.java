package com.kookeries.shop.ui.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kookeries.shop.R;
import com.kookeries.shop.models.Product;

import java.util.List;

public class ProductListAdapter extends GenericListAdapter<Product> {

    private Context mContext;
    private int resource;
    private OnItemClickListener onItemClickListener;

    public ProductListAdapter(Context mContext, List<Product> items, int resource, OnItemClickListener onItemClickListener) {
        super(items);

        this.mContext = mContext;
        this.resource = resource;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup, final Product product) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(resource, null);

            holder.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.price = (TextView) view.findViewById(R.id.price);
            holder.oldPrice = (TextView) view.findViewById(R.id.oldPrice);
            holder.discount = (TextView) view.findViewById(R.id.discount);
            holder.oldPriceWrapper = (LinearLayout) view.findViewById(R.id.oldPriceWrapper);
            holder.expiry = (TextView) view.findViewById(R.id.expiry);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(product.getName());
        holder.expiry.setText(product.getExpiry());

        holder.price.setText(product.getPrice());
        if (product.getOldPrice() != null) {
            holder.oldPrice.setText(product.getOldPrice());
            holder.oldPrice.setPaintFlags(holder.oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.discount.setText(product.getDiscount());

            holder.oldPriceWrapper.setVisibility(View.VISIBLE);
        }

        Glide
                .with(mContext)
                .load(product.getThumbnail())
                .into(holder.thumbnail);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(product);
            }
        });

        return view;
    }

    private class ViewHolder {
        private ImageView thumbnail;
        private TextView name;
        private TextView price;
        private TextView oldPrice;
        private TextView discount;
        private TextView expiry;
        private LinearLayout oldPriceWrapper;
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }
}
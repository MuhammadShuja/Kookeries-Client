package com.kookeries.shop.ui.adapters;

import android.content.Context;
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

public class CartAdapter extends ArrayAdapter<Product> {
    private Context mContext;
    private List<Product> items;
    private int resource;

    public CartAdapter(Context mContext, List<Product> items, int resource) {
        super(mContext, resource, items);
        this.mContext = mContext;
        this.items = items;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        CartAdapter.ViewHolder holder = null;
        if (view == null) {
            holder = new CartAdapter.ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(resource, null);

            holder.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.price = (TextView) view.findViewById(R.id.price);
            holder.oldPrice = (TextView) view.findViewById(R.id.oldPrice);
            holder.discount = (TextView) view.findViewById(R.id.discount);
            holder.oldPriceWrapper = (LinearLayout) view.findViewById(R.id.oldPriceWrapper);

            view.setTag(holder);
        }
        else{
            holder = (CartAdapter.ViewHolder) view.getTag();
        }

        holder.name.setText(items.get(position).getName());
        holder.price.setText(items.get(position).getPrice());
        if(items.get(position).getOldPrice() != null){
            holder.oldPrice.setText(items.get(position).getOldPrice());
            holder.discount.setText(items.get(position).getDiscount());

            holder.oldPriceWrapper.setVisibility(View.VISIBLE);
        }
        Glide
                .with(mContext)
                .load(items.get(position).getThumbnail())
                .into(holder.thumbnail);

        return view;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public class ViewHolder{
        private ImageView thumbnail;
        private TextView name;
        private TextView price;
        private TextView oldPrice;
        private TextView discount;
        private LinearLayout oldPriceWrapper;
    }
}

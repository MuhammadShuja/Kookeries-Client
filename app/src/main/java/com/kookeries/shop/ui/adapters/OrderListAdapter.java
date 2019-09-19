package com.kookeries.shop.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kookeries.shop.R;
import com.kookeries.shop.models.OrderItem;

import java.util.List;

public class OrderListAdapter extends GenericListAdapter<OrderItem> {

    private Context mContext;
    private int resource;
    private OnItemClickListener onItemClickListener;

    public OrderListAdapter(Context mContext, List<OrderItem> items, int resource, OnItemClickListener onItemClickListener) {
        super(items);

        this.mContext = mContext;
        this.resource = resource;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, final OrderItem orderItem) {
        View view = convertView;
        OrderListAdapter.ViewHolder holder = null;
        if (view == null) {
            holder = new OrderListAdapter.ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(resource, null);

            holder.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.quantity = (TextView) view.findViewById(R.id.quantity);
            holder.price = (TextView) view.findViewById(R.id.price);

            view.setTag(holder);
        }
        else{
            holder = (OrderListAdapter.ViewHolder) view.getTag();
        }

        holder.name.setText(orderItem.getName());
        holder.quantity.setText(String.valueOf(orderItem.getQuantity()));
        holder.price.setText(orderItem.getPrice());

        Glide
                .with(mContext)
                .load(orderItem.getThumbnail())
                .into(holder.thumbnail);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(orderItem);
            }
        });

        return view;
    }

    private class ViewHolder {
        private ImageView thumbnail;
        private TextView name;
        private TextView price;
        private TextView quantity;
    }

    public interface OnItemClickListener {
        void onItemClick(OrderItem orderItem);
    }
}
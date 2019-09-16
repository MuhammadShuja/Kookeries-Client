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

public class OrderListAdapter extends ArrayAdapter<OrderItem> {

    private Context mContext;
    private List<OrderItem> items;
    private int resource;

    public OrderListAdapter(Context mContext, List<OrderItem> items, int resource) {
        super(mContext, resource, items);
        this.mContext = mContext;
        this.items = items;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

        holder.name.setText(items.get(position).getName());
        holder.quantity.setText(String.valueOf(items.get(position).getQuantity()));
        holder.price.setText(items.get(position).getPrice());

        Glide
                .with(mContext)
                .load(mContext.getResources().getIdentifier(items.get(position).getThumbnail(), "drawable", mContext.getPackageName()))
                .into(holder.thumbnail);

        return view;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void setData(List<OrderItem> items){
        this.items = items;
        notifyDataSetChanged();
    }

    public class ViewHolder{
        private ImageView thumbnail;
        private TextView name;
        private TextView price;
        private TextView quantity;
    }

}

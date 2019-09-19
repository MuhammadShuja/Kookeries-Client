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
import com.kookeries.shop.models.Category;

import java.util.List;

public class CategoriesAdapter extends GenericListAdapter<Category> {

    private Context mContext;
    private int resource;
    private OnItemClickListener onItemClickListener;

    public CategoriesAdapter(Context mContext, List<Category> categories, int resource, OnItemClickListener onItemClickListener) {
        super(categories);

        this.mContext = mContext;
        this.resource = resource;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup, final Category category) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(resource, null);

            holder.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.action = (TextView) view.findViewById(R.id.action);

            view.setTag(holder);
        }
        else{
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(category.getName());
        Glide
                .with(mContext)
                .load(category.getThumbnail())
                .into(holder.thumbnail);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(category);
            }
        });

        return view;
    }

    private class ViewHolder {
        private ImageView thumbnail;
        private TextView name;
        private TextView action;
    }

    public interface OnItemClickListener {
        void onItemClick(Category category);
    }
}
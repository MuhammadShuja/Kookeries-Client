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

public class CategoriesAdapter extends ArrayAdapter<Category>{

    private Context mContext;
    private List<Category> categories;
    private int resource;

    public CategoriesAdapter(Context mContext, List<Category> categories, int resource) {
        super(mContext, resource, categories);
        this.mContext = mContext;
        this.categories = categories;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = null;
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

        holder.name.setText(categories.get(position).getName());
        Glide
                .with(mContext)
                .load(categories.get(position).getThumbnail())
                .into(holder.thumbnail);

        return view;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    public void setData(List<Category> data){
        this.categories = data;
        notifyDataSetChanged();
    }

    public class ViewHolder{
        private ImageView thumbnail;
        private TextView name;
        private TextView action;
    }
}

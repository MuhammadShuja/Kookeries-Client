package com.kookeries.shop.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kookeries.shop.R;
import com.kookeries.shop.models.ProductImage;

import java.util.List;

public class ImageGridAdapter extends RecyclerView.Adapter<ImageGridAdapter.ViewHolder> {

    private Context mContext;
    private List<ProductImage> productImages;
    private MenuInflater menuInflater;
    private OnItemLongClickListener longClickListener;

    public ImageGridAdapter(Context mContext, List<ProductImage> productImages, MenuInflater menuInflater, OnItemLongClickListener listener) {
        this.mContext = mContext;
        this.productImages = productImages;
        this.menuInflater = menuInflater;
        this.longClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_image_vertical, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(productImages.get(i), i, longClickListener);
    }

    @Override
    public int getItemCount() {
        return productImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener{

        private ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.image);

            itemView.setOnCreateContextMenuListener(this);
        }

        public void bind(final ProductImage productImage, final int position, final OnItemLongClickListener longClickListener) {
            Glide
                    .with(mContext)
                    .load(productImage.getSource())
                    .into(image);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longClickListener.onItemLongClick(view, position, productImage);
                    return true;
                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            menuInflater.inflate(R.menu.image_options_menu, contextMenu);
        }
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view, int position, ProductImage productImage);
    }

    public void setData(List<ProductImage> images){
        this.productImages = images;
        notifyDataSetChanged();
    }
}
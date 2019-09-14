package com.kookeries.shop.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kookeries.shop.R;
import com.kookeries.shop.models.ProductImage;

import java.util.List;

public class ProductImagesAdapter extends PagerAdapter {
    private List<ProductImage> images;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public ProductImagesAdapter(List<ProductImage> images, Context mContext) {
        this.images = images;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View imageLayout = mLayoutInflater.inflate(R.layout.slider_slide, container, false);

        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.slideImage);

        Glide.with(mContext)
                .load(images.get(position).getSource())
                .into(imageView);

        container.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public int getImage(String imageName) {

        int drawableResourceId = mContext.getResources().getIdentifier(imageName, "drawable", mContext.getPackageName());

        return drawableResourceId;
    }
}
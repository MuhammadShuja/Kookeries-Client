package com.kookeries.shop.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kookeries.shop.R;
import com.kookeries.shop.models.Slide;

import java.util.List;

public class SliderAdapter extends PagerAdapter {
    private List<Slide> slides;
    private FrameLayout wrapper;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public SliderAdapter(List<Slide> slides, FrameLayout wrapper, Context mContext) {
        this.slides = slides;
        this.wrapper = wrapper;
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
                .load(getImage(slides.get(position).getImage()))
                .into(imageView);

        if (wrapper != null) {
            wrapper.getBackground().setTint(Color.parseColor(slides.get(position).getColor()));
        }

//        Toast.makeText(mContext, slides.get(position).getImage()+" - "+slides.get(position).getColor(), Toast.LENGTH_SHORT).show();

        container.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public int getCount() {
        return slides.size();
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

    public void setData(List<Slide> slides) {
        this.slides = slides;
        notifyDataSetChanged();
    }
}
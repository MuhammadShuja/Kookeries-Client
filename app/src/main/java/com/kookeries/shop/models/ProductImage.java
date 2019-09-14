package com.kookeries.shop.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProductImage {
    private static final String TAG = "Model/ProductImage";
    public static ProductImage selected = null;

    private int id;
    private String thumbnail;
    private String source;

    public ProductImage(String source) {
        this.source = source;
    }

    public ProductImage(int id, String thumbnail, String source) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.source = source;
    }

    public int getId() {
        return id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getSource() {
        return source;
    }

    public byte[] getImageBytes(Context context) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        getBitmap(context).compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private Bitmap getBitmap(Context context) throws IOException {
        return MediaStore.Images.Media.getBitmap(context.getContentResolver() , Uri.parse(this.source));
    }
}
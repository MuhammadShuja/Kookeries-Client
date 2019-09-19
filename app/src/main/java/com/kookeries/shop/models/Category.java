package com.kookeries.shop.models;

import android.util.Log;

import com.kookeries.shop.api.API;
import com.kookeries.shop.api.config.ApiResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Category {
    private static final String TAG = "Model/Category";

    public static Category seleted = null;

    private int id;
    private String thumbnail;
    private String name;

    private List<Product> products = null;

    /*
    ------------------------------------
    |   Category Instances
    ------------------------------------
    */

    public Category(){

    }

    public Category(int id, String thumbnail, String name) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
    }

    /*
    ------------------------------------
    |   Category Info
    ------------------------------------
    */

    public int getId() {
        return id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getName() {
        return name;
    }

    public static Category find(int id, List<Category> mCategories) {
        for (Category category : mCategories) {
            if (category.getId() == id) {
                return category;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!Category.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final Category other = (Category) obj;
//        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
        if ((this.id < 0) ? (other.id > 0) : this.id != other.id) {
            return false;
        }

        return true;

    }

    /*
    ------------------------------------
    |   Category Products
    ------------------------------------
    */

    public void getProducts(boolean reload, DataReadyListener listener) {
        if (reload || products == null) {
            loadProducts(reload, listener);
        }
        else{
            listener.onReady(products);
        }
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    private void loadProducts(boolean relaod, final DataReadyListener listener) {
        API.getProducts(this, relaod, new ApiResponse.CatalogListener<Product>() {
            @Override
            public void onSuccess(List<Product> data) {
                Category.this.setProducts(data);
                listener.onReady(data);
            }

            @Override
            public void onFailure(JSONObject response) {
                Log.d(TAG, API.PRELOG_FAILURE + response.toString());
            }

            @Override
            public void onException(Exception e) {
                Log.d(TAG, API.PRELOG_EXCEPTION + e.getMessage());
            }
        });
    }

    public void takeProducts(boolean reload, final int count, final boolean shuffle, final Product excludeProduct, final DataReadyListener listener) {

        this.getProducts(reload, new DataReadyListener() {
            @Override
            public void onReady(List<Product> products) {
                if(shuffle){
                    Collections.shuffle(products);
                }
                int c = count;
                if(products.size() < count) c = products.size();
                Set<Product> productsSet = new HashSet<>();

                for(int i = 0 ; i < c ; i++){
                    Product product = products.get(i);
                    if(excludeProduct != null){
                        if(product.getId() == excludeProduct.getId())
                            continue;
                    }
                    productsSet.add(product);
                }

                listener.onReady(new ArrayList<Product>(productsSet));
            }
        });
    }

    /*
    ------------------------------------
    |   Callback Listeners
    ------------------------------------
    */

    public interface DataReadyListener{
        void onReady(List<Product> products);
    }
}
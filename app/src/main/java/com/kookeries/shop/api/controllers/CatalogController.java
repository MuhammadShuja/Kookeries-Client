package com.kookeries.shop.api.controllers;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kookeries.shop.api.API;
import com.kookeries.shop.api.config.ApiResponse;
import com.kookeries.shop.api.config.Event;
import com.kookeries.shop.api.config.Router;
import com.kookeries.shop.api.config.VolleySingleton;
import com.kookeries.shop.models.Category;
import com.kookeries.shop.models.Product;
import com.kookeries.shop.models.ProductImage;
import com.kookeries.shop.persistence.SPM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalogController {
    private static final String TAG = "API/CatalogController";

    public static void categories(final Context mContext, final ApiResponse.CatalogListener<Category> listener){
        Event.active = Event.CATALOG_CATEGORY_INDEX;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Router.getURL(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, API.PRELOG_SUCCESS + response);
                try {
                    List<Category> categories = new ArrayList<>();
                    JSONArray cats = response.getJSONArray("categories");
                    JSONObject cat;
                    for(int i = 0 ; i < cats.length(); i++){
                        cat = cats.getJSONObject(i);
                        categories.add(new Category(cat.getInt("id"), cat.getString("thumbnail"), cat.getString("name")));
                    }
                    API.setCategories(categories);
                    listener.onSuccess(categories);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        JSONObject object = new JSONObject(res);
                        Log.d(TAG, API.PRELOG_ERROR + object);
                        listener.onFailure(object);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Log.d(TAG, API.PRELOG_EXCEPTION + e.getMessage());
                        listener.onException(e);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, API.PRELOG_EXCEPTION + e.getMessage());
                        listener.onException(e);
                    }
                }
                else if (response != null && response.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        JSONObject object = new JSONObject(res);
                        Log.d(TAG, API.PRELOG_UNAUTHORIZED + object);
                        listener.onFailure(object);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Log.d(TAG, API.PRELOG_EXCEPTION + e.getMessage());
                        listener.onException(e);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, API.PRELOG_EXCEPTION + e.getMessage());
                        listener.onException(e);
                    }
                }
            }
        }){
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+ SPM.getInstance(mContext).get(SPM.ACCESS_TOKEN, null));
                return params;
            }
        };
        VolleySingleton.getInstance(mContext).addToRequestQueue(request);
    }

    public static void products(final Context mContext, final Category category, final ApiResponse.CatalogListener<Product> listener){
        Event.active = Event.CATALOG_PRODUCT_INDEX;
        String url = Router.getURL();
        if(category != null){
            url = url+"?category="+category.getId();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, API.PRELOG_SUCCESS + response);
                try {
                    JSONArray items = response.getJSONArray("products");
                    List<Product> products = new ArrayList<>();
                    JSONObject item;
                    for (int i = 0; i < items.length(); i++) {
                        item = items.getJSONObject(i);

                        //  BEGIN FETCHING PRODUCT IMAGES
                        JSONArray imagesJsonArray = item.getJSONArray("images");
                        List<ProductImage> images = new ArrayList<>();
                        for (int j = 0; j < imagesJsonArray.length(); j++) {
                            JSONObject imageObj = (JSONObject) imagesJsonArray.get(j);
                            images.add(new ProductImage(
                                    imageObj.getInt("id"),
                                    imageObj.getString("thumbnail"),
                                    imageObj.getString("source"))
                            );
                        }
                        //  END FETCHING PRODUCT IMAGES

                        Product p = Product.Buyable(
                                item.getInt("id"),
                                item.getInt("category_id"),
                                item.getString("thumbnail"),
                                item.getString("name"),
                                item.getString("price"),
                                item.getString("quantity"),
                                item.getString("expiry"),
                                item.getString("location"),
                                item.getString("description"),
                                images);

                        products.add(p);
                    }
                    if(category == null){
                        API.setProducts(products);
                    }
                    listener.onSuccess(products);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        JSONObject object = new JSONObject(res);
                        Log.d(TAG, API.PRELOG_ERROR + object);
                        listener.onFailure(object);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Log.d(TAG, API.PRELOG_EXCEPTION + e.getMessage());
                        listener.onException(e);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, API.PRELOG_EXCEPTION + e.getMessage());
                        listener.onException(e);
                    }
                }
                else if (response != null && response.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        JSONObject object = new JSONObject(res);
                        Log.d(TAG, API.PRELOG_UNAUTHORIZED + object);
                        listener.onFailure(object);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Log.d(TAG, API.PRELOG_EXCEPTION + e.getMessage());
                        listener.onException(e);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, API.PRELOG_EXCEPTION + e.getMessage());
                        listener.onException(e);
                    }
                }
            }
        }){
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+ SPM.getInstance(mContext).get(SPM.ACCESS_TOKEN, null));
                return params;
            }
        };
        VolleySingleton.getInstance(mContext).addToRequestQueue(request);
    }
}
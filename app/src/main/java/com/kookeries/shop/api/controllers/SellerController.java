package com.kookeries.shop.api.controllers;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.kookeries.shop.api.config.MultipartRequest;
import com.kookeries.shop.api.config.Router;
import com.kookeries.shop.api.config.VolleySingleton;
import com.kookeries.shop.models.Product;
import com.kookeries.shop.models.ProductImage;
import com.kookeries.shop.models.User;
import com.kookeries.shop.persistence.SPM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kookeries.shop.api.API.PRELOG_ERROR;
import static com.kookeries.shop.api.API.PRELOG_EXCEPTION;
import static com.kookeries.shop.api.API.PRELOG_RESPONSE;
import static com.kookeries.shop.api.API.PRELOG_UNAUTHORIZED;

public class SellerController {
    private static final String TAG = "API/SellerController";

    public static void products(final Context mContext, final ApiResponse.CatalogListener<Product> listener){
        Event.active = Event.SELLER_PRODUCT_INDEX;
        productsRequest(mContext, listener);
    }

    public static void latestProducts(final Context mContext, final ApiResponse.CatalogListener<Product> listener){
        Event.active = Event.SELLER_PRODUCT_LATEST;
        productsRequest(mContext, listener);
    }

    public static void newProduct(final Context mContext, final Product product, final ApiResponse.ProductListener listener){
        Event.active = Event.SELLER_PRODUCT_NEW;
        MultipartRequest request = new MultipartRequest(Request.Method.POST, Router.getURL(),
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d(TAG, PRELOG_RESPONSE + response.statusCode);
                        if(response.statusCode == 200){
                            String resultResponse = new String(response.data);
                            Log.d(TAG, API.PRELOG_SUCCESS + resultResponse);
                            try {
                                new JSONObject(resultResponse);
                                Product p = new Product();
                                p.setName("test product");
                                listener.onSuccess(p);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG, PRELOG_EXCEPTION + e.getMessage());
                                listener.onException(e);
                            }
                        }
                        else{
                            listener.onException(new Exception("Unable to save product, error code: "+response.statusCode));
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        Log.d(TAG, PRELOG_ERROR + error);
                        Log.d(TAG, PRELOG_RESPONSE + response);
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                Log.d(TAG, PRELOG_ERROR + res);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                Log.d(TAG, PRELOG_EXCEPTION + e.getMessage());
                                listener.onException(e);
                            }
                        }
                        else if (response != null && response.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                            try {
                                String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                JSONObject object = new JSONObject(res);
                                Log.d(TAG, PRELOG_UNAUTHORIZED + object);
                                listener.onFailure(object);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                Log.d(TAG, PRELOG_EXCEPTION + e.getMessage());
                                listener.onException(e);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG, PRELOG_EXCEPTION + e.getMessage());
                                listener.onException(e);
                            }
                        }
                    }
                }
        ){
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+ SPM.getInstance(mContext).get(SPM.ACCESS_TOKEN, null));
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("category_id", String.valueOf(product.getCategory().getId()));
                params.put("name", product.getName());
                params.put("price", product.getPrice());
                params.put("earning", product.getEarning());
                params.put("quantity", product.getQuantity());
                params.put("expiry_date", product.getExpiry());
                params.put("location", product.getLocation());
                params.put("description", product.getDescription());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                int i = 0;
                for(ProductImage image : product.getImages()){
                    try {
                        params.put("images["+i+"]", new DataPart("image-"+System.currentTimeMillis()+".png", image.getImageBytes(mContext), "image/png"));
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d(TAG, PRELOG_EXCEPTION + e.getMessage());
                        listener.onException(e);
                    }
                    i++;
                }

                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(0, 1, 2));
        VolleySingleton.getInstance(mContext).addToRequestQueue(request);
    }

    private static void productsRequest(final Context mContext, final ApiResponse.CatalogListener listener){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Router.getURL(), null, new Response.Listener<JSONObject>() {
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
                    User.setProducts(products);
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
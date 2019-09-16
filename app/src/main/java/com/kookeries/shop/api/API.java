package com.kookeries.shop.api;

import android.content.Context;
import android.util.Log;

import com.kookeries.shop.api.config.ApiResponse;
import com.kookeries.shop.api.controllers.AuthController;
import com.kookeries.shop.api.controllers.CatalogController;
import com.kookeries.shop.api.controllers.SellerController;
import com.kookeries.shop.api.controllers.UserController;
import com.kookeries.shop.models.Category;
import com.kookeries.shop.models.Product;
import com.kookeries.shop.models.User;

import org.json.JSONObject;

import java.util.List;

public class API {
    private static final String TAG = "API";
    public static final String PRELOG_SUCCESS = "------------------------------------------ Success: ";
    public static final String PRELOG_FAILURE = "------------------------------------------ Failure: ";
    public static final String PRELOG_ERROR = "------------------------------------------ Error: ";
    public static final String PRELOG_Request = "------------------------------------------ Request: ";
    public static final String PRELOG_RESPONSE = "------------------------------------------ Response: ";
    public static final String PRELOG_UNAUTHORIZED = "------------------------------------------ Unauthorized: ";
    public static final String PRELOG_EXCEPTION = "------------------------------------------ Exception: ";
    public static final String PRELOG_CHECK = "------------------------------------------ Check: ";

    private static API instance;
    private static Context mContext;

    private static List<Category> mCategories = null;
    private static List<Product> mProducts = null;

    /*
    ------------------------------------
    |   API Instances
    ------------------------------------
    */

    private API(Context context) {
        mContext = context;
    }

    public static synchronized void instantiate(Context context) {
        if (instance == null) {
            instance = new API(context);
        }
    }

    public static API getInstance(){
        return instance;
    }

    /*
    ------------------------------------
    |   Auth Calls
    ------------------------------------
    */

    public static void register(String name, String username, String password, String passwordConfirmation, final ApiResponse.AuthListener listener) {
        AuthController.register(mContext, name, username, password, passwordConfirmation, listener);
    }

    public static void login(String username, String password, final ApiResponse.AuthListener listener) {
        AuthController.login(mContext, username, password, listener);
    }

    public static void getUser(final ApiResponse.UserListener listener) {
        UserController.profile(mContext, listener);
    }

    /*
    ------------------------------------
    |   Catalog Calls
    ------------------------------------
    */

    public static void getCategories(final ApiResponse.CatalogListener<Category> listener){
        if(mCategories == null){
            CatalogController.categories(mContext, listener);
        }
        else{
            listener.onSuccess(mCategories);
        }
    }

    public static void setCategories(List<Category> categories){
        mCategories = categories;
    }

    public static void getProducts(Category category, boolean relaod, final ApiResponse.CatalogListener<Product> listener) {
        if(category != null){
            CatalogController.products(mContext, category, listener);
        }
        else{
            if (relaod || mProducts == null) {
                CatalogController.products(mContext, null, listener);
            }
            else {
                listener.onSuccess(mProducts);
            }
        }
    }

    public static void setProducts(List<Product> products){
        mProducts = products;
    }

    /*
    ------------------------------------
    |   Seller Calls
    ------------------------------------
    */

    public static void getSellerOrders(final ApiResponse.CatalogListener<Product> listener){
//        SellerController.products(mContext, listener);
    }

    public static void getSellerLatestOrders(final ApiResponse.CatalogListener<Product> listener) {
//        SellerController.latestProducts(mContext, listener);
    }

    public static void getSellerProducts(final ApiResponse.CatalogListener<Product> listener){
        SellerController.products(mContext, listener);
    }

    public static void getSellerLatestProducts(final ApiResponse.CatalogListener<Product> listener){
        SellerController.latestProducts(mContext, listener);
    }

    public static void newProduct(final Product product, final ApiResponse.ProductListener listener){
        SellerController.newProduct(mContext, product, listener);
    }

    /*
    ------------------------------------
    |   Refresh Calls
    ------------------------------------
    */

    public static void reloadBuyerData(){
        getUser(new ApiResponse.UserListener() {
            @Override
            public void onSuccess(User user) {
                User.setUser(user);
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

        getCategories(new ApiResponse.CatalogListener<Category>() {
            @Override
            public void onSuccess(List<Category> response) {
                getProducts(null, false, new ApiResponse.CatalogListener<Product>() {
                    @Override
                    public void onSuccess(List<Product> response) {
                        Log.d(TAG, PRELOG_SUCCESS+ response.size() + " products");
                    }

                    @Override
                    public void onFailure(JSONObject response) {
                        Log.d(TAG, PRELOG_FAILURE + response.toString());
                    }

                    @Override
                    public void onException(Exception e) {
                        Log.d(TAG, PRELOG_EXCEPTION + e.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(JSONObject response) {
                Log.d(TAG, PRELOG_FAILURE + response.toString());
            }

            @Override
            public void onException(Exception e) {
                Log.d(TAG, PRELOG_EXCEPTION + e.getMessage());
            }
        });
    }

    public static void reloadSellerData(){
        getUser(new ApiResponse.UserListener() {
            @Override
            public void onSuccess(User user) {
                User.setUser(user);
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

        getCategories(new ApiResponse.CatalogListener<Category>() {
            @Override
            public void onSuccess(List<Category> response) {
                getProducts(null, false, new ApiResponse.CatalogListener<Product>() {
                    @Override
                    public void onSuccess(List<Product> response) {
                        Log.d(TAG, PRELOG_SUCCESS+ response.size() + " products");
                    }

                    @Override
                    public void onFailure(JSONObject response) {
                        Log.d(TAG, PRELOG_FAILURE + response.toString());
                    }

                    @Override
                    public void onException(Exception e) {
                        Log.d(TAG, PRELOG_EXCEPTION + e.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(JSONObject response) {
                Log.d(TAG, PRELOG_FAILURE + response.toString());
            }

            @Override
            public void onException(Exception e) {
                Log.d(TAG, PRELOG_EXCEPTION + e.getMessage());
            }
        });
    }
}
package com.kookeries.shop.models;

import android.util.Log;

import com.kookeries.shop.api.API;
import com.kookeries.shop.api.config.ApiResponse;

import org.json.JSONObject;

import java.util.List;

public class User {
    private static final String TAG = "Model/User";

    private static User user = null;
    private static List<OrderItem> orders = null;
    private static List<OrderItem> latestOrders = null;
    private static List<Product> products = null;
    private static List<Product> latestProducts = null;
    private static List<Cart> cart = null;

    private static int orderCount = -1;
    private static int productCount = -1;

    private String name;
    private String email;
    private String phone;
    private List<UserAddress> addresses;
    private Wallet wallet;

    /*
    ------------------------------------
    |   User Instances
    ------------------------------------
    */

    public User(String name, String email, String phone, List<UserAddress> addresses, Wallet wallet) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.addresses = addresses;
        this.wallet = wallet;
    }

    /*
    ------------------------------------
    |   User Info
    ------------------------------------
    */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /*
    ------------------------------------
    |   User Addresses
    ------------------------------------
    */

    public List<UserAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<UserAddress> addresses) {
        this.addresses = addresses;
    }

    /*
    ------------------------------------
    |   User Wallet
    ------------------------------------
    */

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    /*
    ------------------------------------
    |   User & User Cart
    ------------------------------------
    */

    public static void getUser(final DataReadyListener listener) {
        if(user == null){
            API.getUser(new ApiResponse.UserListener() {
                @Override
                public void onSuccess(User userData) {
                    user = userData;
                    listener.onReady(user);
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
        else{
            listener.onReady(user);
        }
    }

    public static void setUser(User user) {
        User.user = user;
    }

    public List<Cart> getCart() {
        return cart;
    }

    public void setCart(List<Cart> cart) {
        this.cart = cart;
    }

    /*
    ------------------------------------
    |   Seller Dashboard
    ------------------------------------
    */

    public static void getDashboard(boolean reload, final DashboardReadyListener listener) {
        if (reload || productCount < 0 || orderCount < 0 || latestProducts == null || latestOrders == null) {
            API.getSellerDashboard(new ApiResponse.SellerDashboardListener() {
                @Override
                public void onSuccess(int ordersCount, int productsCount, List<OrderItem> latestOrders, List<Product> latestProducts) {
                    User.orderCount = ordersCount;
                    User.productCount = productsCount;
                    User.latestOrders = latestOrders;
                    User.latestProducts = latestProducts;

                    listener.onReady(ordersCount, productsCount, latestOrders, latestProducts);
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
        } else {
            listener.onReady(orderCount, productCount, latestOrders, latestProducts);
        }
    }

    /*
    ------------------------------------
    |   Seller Orders
    ------------------------------------
    */

    public static int getOrdersCount(){
        return orders.size();
    }

    public static void getOrders(OrdersReadyListener listener) {
        if(orders == null){
            API.getSellerOrders(new ApiResponse.CatalogListener<Product>() {
                @Override
                public void onSuccess(List<Product> data) {
//                    User.latestProducts = data;
//                    listener.onReady(data);
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
        else{
            listener.onReady(orders);
        }
    }

    public static void setOrders(List<OrderItem> orders) {
        User.orders = orders;
    }

    public static void getLatestOrders(OrdersReadyListener listener) {
        if(latestOrders == null){
            API.getSellerLatestOrders(new ApiResponse.CatalogListener<Product>() {
                @Override
                public void onSuccess(List<Product> data) {
//                    User.latestProducts = data;
//                    listener.onReady(data);
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
        else{
            listener.onReady(latestOrders);
        }
    }

    public static void setLatestOrders(List<OrderItem> latestOrders) {
        User.latestOrders = latestOrders;
    }

    /*
    ------------------------------------
    |   Seller Products
    ------------------------------------
    */


    public static int getProductsCount(){
        return products.size();
    }

    public static void getProducts(final ProductsReadyListener listener) {
        if(products == null){
            API.getSellerProducts(new ApiResponse.CatalogListener<Product>() {
                @Override
                public void onSuccess(List<Product> data) {
                    User.products = data;
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
        else{
            listener.onReady(products);
        }
    }

    public static void setProducts(List<Product> products) {
        User.products = products;
    }

    public static void getLatestProducts(final ProductsReadyListener listener) {
        if(latestProducts == null){
            API.getSellerLatestProducts(new ApiResponse.CatalogListener<Product>() {
                @Override
                public void onSuccess(List<Product> data) {
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
        else{
            listener.onReady(latestProducts);
        }
    }

    public static void setLatestProducts(List<Product> latestProducts) {
        User.latestProducts = latestProducts;
    }

    public interface DataReadyListener{
        void onReady(User user);
    }

    public static void addNewProduct(Product product) {
        if (User.latestProducts != null) {
            User.latestProducts.add(0, product);
        }

        if (User.products != null) {
            User.products.add(0, product);
        }
    }

    /*
    ------------------------------------
    |   Callback Listeners
    ------------------------------------
    */

    public interface OrdersReadyListener{
        void onReady(List<OrderItem> data);
    }

    public interface ProductsReadyListener{
        void onReady(List<Product> data);
    }

    public interface DashboardReadyListener {
        void onReady(int orderCount, int productCount, List<OrderItem> latestOrders, List<Product> latestProducts);
    }
}

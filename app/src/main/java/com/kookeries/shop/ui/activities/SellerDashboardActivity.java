package com.kookeries.shop.ui.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kookeries.shop.R;
import com.kookeries.shop.ui.adapters.OrderListAdapter;
import com.kookeries.shop.ui.adapters.ProductListAdapter;
import com.kookeries.shop.api.API;
import com.kookeries.shop.models.OrderItem;
import com.kookeries.shop.models.Product;
import com.kookeries.shop.models.User;
import com.kookeries.shop.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.kookeries.shop.utils.Utils.setListViewHeightBasedOnChildren;

public class SellerDashboardActivity extends AppCompatActivity {
    private static final String TAG = "SellerDashboardActivity";

    private ListView lvOrders, lvProducts;

    private List<OrderItem> orderItems = new ArrayList<>();
    private List<Product> products = new ArrayList<>();

    private OrderListAdapter orderListAdapter;
    private ProductListAdapter productListAdapter;

    private View.OnClickListener viewAllOrdersListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            startActivity(new Intent(SellerDashboardActivity.this, SellerOrdersActivity.class));
        }
    };

    private View.OnClickListener viewAllProductsListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            startActivity(new Intent(SellerDashboardActivity.this, SellerProductsActivity.class));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_seller_dashboard);
        API.instantiate(getApplicationContext());

        setupToolbar();
        setupOrders();
        setupProducts();
        populateData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.seller_dashboard_menu, menu);
        Utils.menuIconColor(menu.findItem(R.id.home), Color.WHITE);
        Utils.menuIconColor(menu.findItem(R.id.add), Color.WHITE);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add) {
            startActivity(new Intent(SellerDashboardActivity.this, SellerProductNewActivity.class));
            return true;
        }
        else if (id == R.id.home) {
            startActivity(new Intent(SellerDashboardActivity.this, HomeActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Seller Dashboard");
    }

    private void setupOrders(){
        ((TextView) findViewById(R.id.btnViewOrders)).setOnClickListener(viewAllOrdersListener);

        FrameLayout flOrders = (FrameLayout) findViewById(R.id.sectionLatestOrders);
        flOrders.setBackgroundColor(getResources().getColor(R.color.white));
        TextView tvOrders = (TextView) flOrders.findViewById(R.id.sectionTitle);
        tvOrders.setTextSize(14);
        tvOrders.setText("Latest Orders");

        lvOrders = (ListView) flOrders.findViewById(R.id.sectionBody);
        orderListAdapter = new OrderListAdapter(this, orderItems, R.layout.card_order_horizontal);
        lvOrders.setAdapter(orderListAdapter);
        setListViewHeightBasedOnChildren(lvOrders);
        ((TextView) flOrders.findViewById(R.id.sectionAction)).setOnClickListener(viewAllOrdersListener);
    }

    private void setupProducts(){
        ((TextView) findViewById(R.id.btnViewProducts)).setOnClickListener(viewAllProductsListener);

        FrameLayout flProducts = (FrameLayout) findViewById(R.id.sectionLatestProducts);
        flProducts.setBackgroundColor(getResources().getColor(R.color.white));
        TextView tvProducts = (TextView) flProducts.findViewById(R.id.sectionTitle);
        tvProducts.setTextSize(14);
        tvProducts.setText("Latest Products");

        lvProducts = (ListView) flProducts.findViewById(R.id.sectionBody);
        productListAdapter = new ProductListAdapter(this, products, R.layout.card_product_horizontal);
        lvProducts.setAdapter(productListAdapter);
        setListViewHeightBasedOnChildren(lvProducts);
        ((TextView) flProducts.findViewById(R.id.sectionAction)).setOnClickListener(viewAllProductsListener);
    }

    private void populateData() {
        User.getProducts(new User.ProductsReadyListener() {
            @Override
            public void onReady(List<Product> data) {
//                ((TextView) findViewById(R.id.productCount)).setText(data.size());
            }
        });

        User.getLatestProducts(new User.ProductsReadyListener() {
            @Override
            public void onReady(List<Product> data) {
                products.clear();
                products.addAll(data);

                productListAdapter.setProducts(data);
                setListViewHeightBasedOnChildren(lvProducts);
            }
        });

//        User.getLatestOrders(new User.OrdersReadyListener() {
//            @Override
//            public void onReady(List<OrderItem> data) {
//
//            }
//        });

        orderItems.add(new OrderItem(1, "f1", "Lorem ipsum dolor sit amet lorem ipsum dolor sit amet", 5, "500", "0"));
        orderItems.add(new OrderItem(1, "f2", "Lorem ipsum dolor sit amet lorem ipsum dolor sit amet", 5, "500", "0"));
        orderItems.add(new OrderItem(1, "f3", "Lorem ipsum dolor sit amet lorem ipsum dolor sit amet", 5, "500", "0"));
        orderItems.add(new OrderItem(1, "f4", "Lorem ipsum dolor sit amet lorem ipsum dolor sit amet", 5, "500", "0"));
        orderItems.add(new OrderItem(1, "f5", "Lorem ipsum dolor sit amet lorem ipsum dolor sit amet", 5, "500", "0"));

        orderListAdapter.setData(orderItems);

    }
}
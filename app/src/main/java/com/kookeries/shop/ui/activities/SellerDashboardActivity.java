package com.kookeries.shop.ui.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.kookeries.shop.R;
import com.kookeries.shop.ui.adapters.GenericListAdapter;
import com.kookeries.shop.ui.adapters.OrderListAdapter;
import com.kookeries.shop.ui.adapters.ProductListAdapter;
import com.kookeries.shop.api.API;
import com.kookeries.shop.models.OrderItem;
import com.kookeries.shop.models.Product;
import com.kookeries.shop.models.User;
import com.kookeries.shop.ui.sections.ListSection;
import com.kookeries.shop.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class SellerDashboardActivity extends AppCompatActivity {
    private static final String TAG = "SellerDashboardActivity";

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private OrderListAdapter.OnItemClickListener orderClickListener = null;
    private ListSection<OrderItem> orderListSection = null;

    private ProductListAdapter.OnItemClickListener productClickListener = null;
    private ListSection<Product> productListSection = null;

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
        initSwipeRefreshLayout();
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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Seller Dashboard");
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout = findViewById(R.id.refreshLayout);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.white);
        int indicatorColorArray[] = {R.color.primaryColor, R.color.orange};
        mSwipeRefreshLayout.setColorSchemeResources(indicatorColorArray);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData(true);
            }
        });
    }

    private void populateData() {
        findViewById(R.id.btnViewOrders).setOnClickListener(viewAllOrdersListener);
        findViewById(R.id.btnViewProducts).setOnClickListener(viewAllProductsListener);

        reloadData(false);
    }

    private void reloadData(boolean reloadFromServer) {
        User.getDashboard(reloadFromServer, new User.DashboardReadyListener() {
            @Override
            public void onReady(int orderCount, int productCount, List<OrderItem> latestOrders, List<Product> latestProducts) {
                setupCounts(orderCount, productCount);
                setupLatestOrders(latestOrders);
                setupLatestProducts(latestProducts);

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setupCounts(int ordersCount, int productsCount) {
        ((TextView) findViewById(R.id.orderCount)).setText(String.valueOf(ordersCount));
        ((TextView) findViewById(R.id.productCount)).setText(String.valueOf(productsCount));
    }

    private void setupLatestOrders(final List<OrderItem> latestOrders) {
        if (orderClickListener == null) {
            orderClickListener = new OrderListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(OrderItem orderItem) {
                }
            };
        }

        if (orderListSection == null) {
            orderListSection = new ListSection<>(findViewById(R.id.sectionLatestOrders),
                    new OrderListAdapter(this, new ArrayList<OrderItem>(), R.layout.card_order_horizontal, orderClickListener),
                    new ListSection.SectionStateObserver<OrderItem>() {
                        @Override
                        public void sectionReadyToReload(final GenericListAdapter<OrderItem> adapter) {
                            adapter.setData(latestOrders);
                        }

                        @Override
                        public void sectionEmpty() {

                        }

                        @Override
                        public void sectionLoaded() {

                        }
                    });
            orderListSection.setBackgroundColor(getResources().getColor(R.color.white));
            orderListSection.setTitle("Latest Orders");
            orderListSection.setTitleSize(14);
            orderListSection.setSectionHeightBasedOnChildren(true);
            orderListSection.setActionListener(viewAllOrdersListener);
        } else {
            orderListSection.setData(latestOrders);
        }
    }

    private void setupLatestProducts(final List<Product> latestProducts) {
        if (productClickListener == null) {
            productClickListener = new ProductListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Product product) {
                    Product.selected = product;
                    startActivity(new Intent(SellerDashboardActivity.this, ProductActivity.class));
                }
            };
        }

        if (productListSection == null) {
            productListSection = new ListSection<>(findViewById(R.id.sectionLatestProducts),
                    new ProductListAdapter(this, new ArrayList<Product>(), R.layout.card_product_horizontal, productClickListener),
                    new ListSection.SectionStateObserver<Product>() {
                        @Override
                        public void sectionReadyToReload(final GenericListAdapter<Product> adapter) {
                            adapter.setData(latestProducts);
                        }

                        @Override
                        public void sectionEmpty() {

                        }

                        @Override
                        public void sectionLoaded() {

                        }
                    });
            productListSection.setBackgroundColor(getResources().getColor(R.color.white));
            productListSection.setTitle("Latest Products");
            productListSection.setTitleSize(14);
            productListSection.setSectionHeightBasedOnChildren(true);
            productListSection.setActionListener(viewAllProductsListener);
        } else {
            productListSection.setData(latestProducts);
        }
    }
}
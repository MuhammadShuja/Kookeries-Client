package com.kookeries.shop.ui.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;

import com.kookeries.shop.R;
import com.kookeries.shop.api.API;
import com.kookeries.shop.models.OrderItem;
import com.kookeries.shop.models.User;
import com.kookeries.shop.ui.adapters.GenericListAdapter;
import com.kookeries.shop.ui.adapters.OrderListAdapter;
import com.kookeries.shop.ui.sections.ListSection;
import com.kookeries.shop.ui.sections.OrderListSection;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SellerOrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_seller_orders);
        API.instantiate(getApplicationContext());

        setupToolbar();
        setupOrders();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    private void setupOrders() {
        OrderListAdapter.OnItemClickListener listener = new OrderListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(OrderItem orderItem) {
//                Product.selected = product;
//                startActivity(new Intent(SellerDashboardActivity.this, ProductActivity.class));
            }
        };

        ListSection orderListSection = new ListSection(findViewById(R.id.sectionListOrders),
                new OrderListAdapter(this, new ArrayList<OrderItem>(), R.layout.card_order_horizontal, listener),
                new ListSection.SectionStateObserver<OrderItem>() {
                    @Override
                    public void sectionReadyToReload(final GenericListAdapter<OrderItem> adapter) {
                        adapter.setData(new ArrayList<OrderItem>());
                    }

                    @Override
                    public void sectionEmpty() {

                    }

                    @Override
                    public void sectionLoaded() {

                    }
                });
        orderListSection.setBackgroundColor(getResources().getColor(R.color.white));
    }
}
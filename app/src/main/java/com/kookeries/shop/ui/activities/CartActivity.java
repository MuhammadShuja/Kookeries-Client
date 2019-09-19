package com.kookeries.shop.ui.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.kookeries.shop.R;
import com.kookeries.shop.ui.adapters.CartAdapter;
import com.kookeries.shop.api.API;
import com.kookeries.shop.models.Cart;
import com.kookeries.shop.models.Product;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CartActivity extends AppCompatActivity {

    private TextView tvSubTotal, tvTotal;

    private List<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_cart);
        API.instantiate(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        tvSubTotal = (TextView) findViewById(R.id.subTotal);
        tvTotal = (TextView) findViewById(R.id.total);

        populateData();

        ListView lvItems = (ListView) findViewById(R.id.items);
        lvItems.setAdapter(new CartAdapter(this, products, R.layout.card_cart_item));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateData(){
        products.clear();
        products.addAll(Cart.getItems());

        tvSubTotal.setText(getString(R.string.currency_symbol)+" "+Cart.getSubTotal());
        tvTotal.setText(getString(R.string.currency_symbol)+" "+Cart.getTotal());

        setTitle("My Cart ("+products.size()+")");
    }
}
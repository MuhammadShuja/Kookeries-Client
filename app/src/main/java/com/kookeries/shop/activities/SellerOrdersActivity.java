package com.kookeries.shop.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kookeries.shop.R;
import com.kookeries.shop.api.API;

public class SellerOrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_orders);
        API.instantiate(getApplicationContext());
    }
}

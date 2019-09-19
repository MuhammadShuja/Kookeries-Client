package com.kookeries.shop.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.kookeries.shop.R;
import com.kookeries.shop.api.API;
import com.kookeries.shop.persistence.SPM;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        API.instantiate(getApplicationContext());

        String token = SPM.getInstance(getApplicationContext()).get(SPM.ACCESS_TOKEN, null);
        if(token == null){
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
        else{
            API.reloadBuyerData();
            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
//            startActivity(new Intent(SplashActivity.this, SellerDashboardActivity.class));
            finish();
        }
    }
}

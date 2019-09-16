package com.kookeries.shop.ui.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kookeries.shop.R;
import com.kookeries.shop.api.API;
import com.kookeries.shop.api.config.ApiResponse;
import com.kookeries.shop.persistence.SPM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class RegisterActivity extends AppCompatActivity {
    private TextView tvName, tvEmail, tvPassword, tvPasswordConfirmation;
    private TextView tvNameError, tvEmailError, tvPasswordError, tvPasswordConfirmationError;
    private TextView btnLogin, btnRegister;

    private static final String TAG = "Register Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_register);
        API.instantiate(getApplicationContext());

        tvName = (TextView) findViewById(R.id.name);
        tvNameError = (TextView) findViewById(R.id.name_error);
        tvEmail = (TextView) findViewById(R.id.email);
        tvEmailError = (TextView) findViewById(R.id.email_error);
        tvPassword = (TextView) findViewById(R.id.password);
        tvPasswordError = (TextView) findViewById(R.id.password_error);
        tvPasswordConfirmation = (TextView) findViewById(R.id.password_confirmation);
        tvPasswordConfirmationError = (TextView) findViewById(R.id.password_confirmation_error);

        btnLogin = (TextView) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        btnRegister = (TextView) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int validationFlag = 0;
                String name = tvName.getText().toString();
                String email = tvEmail.getText().toString();
                String password = tvPassword.getText().toString();
                String passwordConfirmation = tvPasswordConfirmation.getText().toString();

                if(name.length() == 0){
                    tvNameError.setVisibility(View.VISIBLE);
                    tvNameError.setText("Name field is required");
                    validationFlag = 1;
                }
                else{
                    tvNameError.setVisibility(View.GONE);
                    validationFlag = 0;
                }

                if(email.length() == 0){
                    tvEmailError.setVisibility(View.VISIBLE);
                    tvEmailError.setText("Email field is required");
                    validationFlag = 1;
                }
                else{
                    tvEmailError.setVisibility(View.GONE);
                    validationFlag = 0;
                }

                if(password.length() < 6){
                    tvPasswordError.setVisibility(View.VISIBLE);
                    tvPasswordError.setText("Password must be 6 characters long");
                    validationFlag = 1;
                }
                else{
                    tvPasswordError.setVisibility(View.GONE);
                    validationFlag = 0;
                }

                if(!passwordConfirmation.equals(password)){
                    tvPasswordConfirmationError.setVisibility(View.VISIBLE);
                    tvPasswordConfirmationError.setText("Password mismatch");
                    validationFlag = 1;
                }
                else{
                    tvPasswordConfirmationError.setVisibility(View.GONE);
                    validationFlag = 0;
                }

                if(validationFlag == 0){
                    API.register(name, email, password, passwordConfirmation, new ApiResponse.AuthListener() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Log.d(TAG, "--------------------------------------- Success: "+response);

                            try {
                                String tokenType = response.getString("token_type");
                                String tokenExpiry = response.getString("expires_in");
                                String accessToken = response.getString("access_token");
                                String refreshToken = response.getString("refresh_token");

                                SPM prefManager = SPM.getInstance(getApplicationContext());
                                prefManager.save(SPM.TOKEN_TYPE, tokenType);
                                prefManager.save(SPM.EXPIRES_IN, tokenExpiry);
                                prefManager.save(SPM.ACCESS_TOKEN, accessToken);
                                prefManager.save(SPM.REFRESH_TOKEN, refreshToken);

                                startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                Log.d(TAG, "--------------------------------------- Exception: "+e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(JSONObject response) {
                            try {
                                JSONObject errors = response.getJSONObject("errors");
                                Iterator<String> iterator = errors.keys();
                                while (iterator.hasNext()) {
                                    String key = iterator.next();
                                    JSONArray value;
                                    switch (key) {
                                        case "name":
                                            value = (JSONArray) errors.get(key);
                                            tvNameError.setVisibility(View.VISIBLE);
                                            tvNameError.setText(value.get(0).toString());
                                            Log.d(TAG, "--------------------------------------- Failure: "+value.get(0));
                                            break;
                                        case "email":
                                            value = (JSONArray) errors.get(key);
                                            tvEmailError.setVisibility(View.VISIBLE);
                                            tvEmailError.setText(value.get(0).toString());
                                            Log.d(TAG, "--------------------------------------- Failure: "+value.get(0));
                                            break;
                                        case "password":
                                            value = (JSONArray) errors.get(key);
                                            tvPasswordError.setVisibility(View.VISIBLE);
                                            tvPasswordError.setText(value.get(0).toString());
                                            Log.d(TAG, "--------------------------------------- Failure: "+value.get(0));
                                            break;
                                        case "phone":
                                            value = (JSONArray) errors.get(key);
                                            Toast.makeText(getApplicationContext(), "Phone: "+value.get(0), Toast.LENGTH_LONG).show();
                                            Log.d(TAG, "--------------------------------------- Failure: "+value.get(0));
                                            break;
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                Log.d(TAG, "--------------------------------------- Exception: "+e.getMessage());
                            }
                        }

                        @Override
                        public void onException(Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}

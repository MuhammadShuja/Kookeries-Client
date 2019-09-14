package com.kookeries.shop.activities;

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

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private TextView tvEmail, tvPassword;
    private TextView tvEmailError, tvPasswordError;
    private TextView btnLogin, btnRegister;

    private static final String TAG = "Login Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
        API.instantiate(getApplicationContext());

        tvEmail = (TextView) findViewById(R.id.email);
        tvEmailError = (TextView) findViewById(R.id.email_error);
        tvPassword = (TextView) findViewById(R.id.password);
        tvPasswordError = (TextView) findViewById(R.id.password_error);

        btnLogin = (TextView) findViewById(R.id.btnLogin);
        btnRegister = (TextView) findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int validationFlag = 0;
                String email = tvEmail.getText().toString();
                String password = tvPassword.getText().toString();

                if(email.length() == 0){
                    tvEmailError.setVisibility(View.VISIBLE);
                    tvEmailError.setText("Email field is required");
                    validationFlag = 1;
                }
                else{
                    tvEmailError.setVisibility(View.GONE);
                    validationFlag = 0;
                }

                if(password.length() == 0){
                    tvPasswordError.setVisibility(View.VISIBLE);
                    tvPasswordError.setText("Password must be 6 characters long");
                    validationFlag = 1;
                }
                else{
                    tvPasswordError.setVisibility(View.GONE);
                    validationFlag = 0;
                }

                if(validationFlag == 0){
                    API.login(email, password, new ApiResponse.AuthListener() {
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

                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                Log.d(TAG, "--------------------------------------- Exception: "+e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(JSONObject response) {
                            try {
                                String error = response.getString("error");
                                switch (error) {
                                    case "invalid_request":
                                        tvEmailError.setText(response.getString("hint"));
                                        tvEmailError.setVisibility(View.VISIBLE);
                                        Log.d(TAG, "--------------------------------------- Failure: "+response.getString("hint"));
                                        break;
                                    case "invalid_credentials":
                                        tvEmailError.setText("Incorrect email or password");
                                        tvEmailError.setVisibility(View.VISIBLE);
                                        Log.d(TAG, "--------------------------------------- Failure: "+response.getString("message"));
                                        break;
                                    default:
                                        Log.d(TAG, "--------------------------------------- Failure: "+error);
                                        break;
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

        btnRegister = (TextView) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}

package com.kookeries.shop.api.controllers;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kookeries.shop.api.API;
import com.kookeries.shop.api.config.ApiResponse;
import com.kookeries.shop.api.config.Event;
import com.kookeries.shop.api.config.Router;
import com.kookeries.shop.api.config.VolleySingleton;
import com.kookeries.shop.models.User;
import com.kookeries.shop.models.UserAddress;
import com.kookeries.shop.models.Wallet;
import com.kookeries.shop.persistence.SPM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserController {
    private static final String TAG = "API/UserController";

    public static void profile(final Context mContext, final ApiResponse.UserListener listener){
        Event.active = Event.USER_PROFILE;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Router.getURL(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, API.PRELOG_SUCCESS + response);
                try {
                    JSONObject profile = response.getJSONObject("profile");

                    JSONArray addressesArray = profile.getJSONArray("addresses");
                    List<UserAddress> addresses = new ArrayList<>();

                    for(int i = 0; i < addressesArray.length(); i++){
                        JSONObject addressObject = addressesArray.getJSONObject(i);
                        addresses.add(new UserAddress(
                                addressObject.getInt("id"),
                                addressObject.getString("address"),
                                addressObject.getBoolean("default")
                        ));
                    }

                    JSONObject walletObject = profile.getJSONObject("wallet");

                    Wallet wallet = new Wallet(
                            walletObject.getDouble("balance")
                    );

                    User user = new User(
                            profile.getString("name"),
                            profile.getString("email"),
                            profile.getString("phone"),
                            addresses,
                            wallet
                    );

                    User.setUser(user);
                    listener.onSuccess(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        JSONObject object = new JSONObject(res);
                        Log.d(TAG, API.PRELOG_ERROR + object);
                        listener.onFailure(object);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Log.d(TAG, API.PRELOG_EXCEPTION + e.getMessage());
                        listener.onException(e);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, API.PRELOG_EXCEPTION + e.getMessage());
                        listener.onException(e);
                    }
                }
                else if (response != null && response.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        JSONObject object = new JSONObject(res);
                        Log.d(TAG, API.PRELOG_UNAUTHORIZED + object);
                        listener.onFailure(object);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Log.d(TAG, API.PRELOG_EXCEPTION + e.getMessage());
                        listener.onException(e);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, API.PRELOG_EXCEPTION + e.getMessage());
                        listener.onException(e);
                    }
                }
            }
        }){
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+ SPM.getInstance(mContext).get(SPM.ACCESS_TOKEN, null));
                return params;
            }
        };
        VolleySingleton.getInstance(mContext).addToRequestQueue(request);
    }
}

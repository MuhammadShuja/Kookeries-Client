package com.kookeries.shop.api.controllers;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kookeries.shop.R;
import com.kookeries.shop.api.API;
import com.kookeries.shop.api.config.ApiResponse;
import com.kookeries.shop.api.config.Event;
import com.kookeries.shop.api.config.Router;
import com.kookeries.shop.utils.Serializer;
import com.kookeries.shop.api.config.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthController {
    private static final String TAG = "API/AuthController";

    public static void register(Context mContext, String name, String username, String password, String passwordConfirmation, final ApiResponse.AuthListener listener) {
        Event.active = Event.USER_REGISTER;
        List<Pair<String, String>> data = new ArrayList<>();
        data.add(new Pair<>("grant_type", "password"));
        data.add(new Pair<>("client_id", mContext.getString(R.string.api_client_id)));
        data.add(new Pair<>("client_secret", mContext.getString(R.string.api_client_secret)));
        data.add(new Pair<>("scope", "*"));
        data.add(new Pair<>("name", name));
        data.add(new Pair<>("email", username));
        data.add(new Pair<>("password", password));
        data.add(new Pair<>("password_confirmation", passwordConfirmation));

        Log.d(TAG, API.PRELOG_Request+Serializer.serializeData(data).toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Router.getURL(), Serializer.serializeData(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, API.PRELOG_SUCCESS+response);
                        listener.onSuccess(response);
                        API.reloadBuyerData();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                JSONObject object = new JSONObject(res);
                                Log.d(TAG, API.PRELOG_ERROR+object);
                                listener.onFailure(object);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                Log.d(TAG, API.PRELOG_EXCEPTION+e.getMessage());
                                listener.onException(e);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG, API.PRELOG_EXCEPTION+e.getMessage());
                                listener.onException(e);
                            }
                        }
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        VolleySingleton.getInstance(mContext).addToRequestQueue(request);
    }

    public static void login(Context mContext, String username, String password, final ApiResponse.AuthListener listener) {
        Event.active = Event.USER_LOGIN;
        List<Pair<String, String>> data = new ArrayList<>();
        data.add(new Pair<>("grant_type", "password"));
        data.add(new Pair<>("client_id", mContext.getString(R.string.api_client_id)));
        data.add(new Pair<>("client_secret", mContext.getString(R.string.api_client_secret)));
        data.add(new Pair<>("scope", "*"));
        data.add(new Pair<>("username", username));
        data.add(new Pair<>("password", password));

        Log.d(TAG, API.PRELOG_Request+Serializer.serializeData(data).toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Router.getURL(), Serializer.serializeData(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, API.PRELOG_SUCCESS+response);
                        listener.onSuccess(response);
                        API.reloadBuyerData();
                    }
                },
                new Response.ErrorListener() {
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
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        VolleySingleton.getInstance(mContext).addToRequestQueue(request);
    }

}

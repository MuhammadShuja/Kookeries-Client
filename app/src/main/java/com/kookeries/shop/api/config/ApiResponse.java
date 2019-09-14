package com.kookeries.shop.api.config;

import com.kookeries.shop.models.Product;
import com.kookeries.shop.models.User;

import org.json.JSONObject;

import java.util.List;

public class ApiResponse {
    public interface AuthListener{
        void onSuccess(JSONObject response);
        void onFailure(JSONObject response);
        void onException(Exception e);
    }

    public interface UserListener{
        void onSuccess(User user);
        void onFailure(JSONObject response);
        void onException(Exception e);
    }

    public interface CatalogListener<T>{
        void onSuccess(List<T> data);
        void onFailure(JSONObject response);
        void onException(Exception e);
    }

    public interface ProductListener<T>{
        void onSuccess(Product product);
        void onFailure(JSONObject response);
        void onException(Exception e);
    }
}

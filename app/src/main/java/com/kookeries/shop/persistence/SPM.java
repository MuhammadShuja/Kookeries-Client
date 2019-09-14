package com.kookeries.shop.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SPM {
    private static final String PREFS_NAME = "IQShop";

    public static final String ACCESS_TOKEN = "acess_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String EXPIRES_IN = "expires_in";
    public static final String TOKEN_TYPE = "token_type";

    private static Context mContext;
    private static SharedPreferences sharedPreferences = null;
    private static SharedPreferences.Editor editor = null;

    public static SPM getInstance(Context context){
        mContext = context;
        if(sharedPreferences == null){
            sharedPreferences = mContext.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        }
        if(editor == null){
            editor = sharedPreferences.edit();
        }
        return new SPM();
    }

    public void save(String key, String value){
        editor.putString(key, value);
        editor.apply();
    }
    public void save(String key, int value){
        editor.putInt(key, value);
        editor.apply();
    }
    public void save(String key, boolean value){
        editor.putBoolean(key, value);
        editor.apply();
    }

    public String get(String key, String defValule){
        return sharedPreferences.getString(key, defValule);
    }
    public int get(String key, int defValule){
        return sharedPreferences.getInt(key, defValule);
    }
    public boolean get(String key, boolean defValule){
        return sharedPreferences.getBoolean(key, defValule);
    }

}

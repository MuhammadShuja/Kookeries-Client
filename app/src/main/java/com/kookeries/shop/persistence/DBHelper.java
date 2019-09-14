package com.kookeries.shop.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    //    TABLE NAMES
    public static final String TABLE_USERS = "users";
    public static final String TABLE_API_TOKENS = "api_tokens";

    //    TABLE COLUMNS
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String PHONE = "phone";
    public static final String SEX = "sex";

    public static final String TOKEN_TYPE = "token_type";
    public static final String TOKEN_EXPIRY = "token_expiry";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";

    //    QUERIES
    private static final String CREATE_USERS_TABLE = "create table " + TABLE_USERS + "(" + ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT NOT NULL, " + EMAIL + " TEXT NOT NULL, "
            + PASSWORD + " TEXT NOT NULL, " + PHONE + " TEXT, " + SEX + " TEXT);";

    private static final String CREATE_API_TOKENS_TABLE = "create table " + TABLE_API_TOKENS + "(" + ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TOKEN_TYPE + " TEXT, " + TOKEN_EXPIRY + " TEXT, "
            + ACCESS_TOKEN + " TEXT, " + REFRESH_TOKEN + " TEXT);";

    private static final String DB_NAME = "IQShop.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USERS_TABLE);
        sqLiteDatabase.execSQL(CREATE_API_TOKENS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + CREATE_USERS_TABLE + "'");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + CREATE_API_TOKENS_TABLE + "'");
        onCreate(sqLiteDatabase);
    }
}

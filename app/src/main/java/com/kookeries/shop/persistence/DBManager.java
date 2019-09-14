package com.kookeries.shop.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DBHelper dbHelper;

    private Context mContext;

    private SQLiteDatabase database;

    public DBManager(Context context) {
        mContext = context;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DBHelper(mContext);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    /*
    |-------------------------------------
    |   USER OPERATIONS
    |-------------------------------------
    */

    public boolean storeUser(String name, String email, String password, String phone, String sex) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.NAME, name);
        contentValue.put(DBHelper.EMAIL, email);
        contentValue.put(DBHelper.PASSWORD, password);
        contentValue.put(DBHelper.PHONE, phone);
        contentValue.put(DBHelper.SEX, sex);
        if(database.insert(DBHelper.TABLE_USERS, null, contentValue) > -1)
            return true;
        else return false;
    }

    /*
    |-------------------------------------
    |   API OPERATIONS
    |-------------------------------------
    */

    public boolean storeAPI(String tokenType, String tokenExpiry, String accessToken, String refreshToken) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.TOKEN_TYPE, tokenType);
        contentValue.put(DBHelper.TOKEN_EXPIRY, tokenExpiry);
        contentValue.put(DBHelper.ACCESS_TOKEN, accessToken);
        contentValue.put(DBHelper.REFRESH_TOKEN, refreshToken);
        if(database.insert(DBHelper.TABLE_API_TOKENS, null, contentValue) > -1)
            return true;
        else return false;
    }

//    public Cursor fetchSMS() {
//        String[] columns = new String[] { DBHelper._ID, DBHelper.NAME, DBHelper.ADDRESS,
//                DBHelper.THREAD_ID, DBHelper.TYPE, DBHelper.MESSAGE, DBHelper.DATE };
//        Cursor cursor = database.query(DBHelper.TABLE_SMS, columns, null, null, null, null, null);
//        if (cursor != null) {
//            cursor.moveToFirst();
//        }
//        return cursor;
//    }

//    public String getSMSJSON(){
//        String[] columns = new String[] { DBHelper._ID, DBHelper.NAME, DBHelper.ADDRESS,
//                DBHelper.THREAD_ID, DBHelper.TYPE, DBHelper.MESSAGE, DBHelper.DATE};
//        Cursor cursor = database.query(DBHelper.TABLE_SMS, columns, null,
//                null, null, null, null, "20");
//
//
//        StringWriter stringWriter=new StringWriter();
//        JsonWriter writer=new JsonWriter(stringWriter);
//        writer.setIndent("  ");
//        try {
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//            String id = prefs.getString("ID", "");
//            if(id.equals("")) {
//                id = String.valueOf(System.currentTimeMillis());
//            }
//            writer.beginObject();
//            writer.name("type").value("sms");
//            writer.name("vic").value(id);
//            writer.name("data");
//            writer.beginArray();
//            if(cursor != null){
//                while(cursor.moveToNext()){
//                    writer.beginObject();
//                    writer.name("id").value(cursor.getString(cursor.getColumnIndex(DBHelper._ID)));
//                    writer.name("name").value(cursor.getString(cursor.getColumnIndex(DBHelper.NAME)));
//                    writer.name("address").value(cursor.getString(cursor.getColumnIndex(DBHelper.ADDRESS)));
//                    writer.name("thread_id").value(cursor.getString(cursor.getColumnIndex(DBHelper.THREAD_ID)));
//                    writer.name("type").value(cursor.getString(cursor.getColumnIndex(DBHelper.TYPE)));
//                    writer.name("message").value(cursor.getString(cursor.getColumnIndex(DBHelper.MESSAGE)));
//                    writer.name("date").value(cursor.getString(cursor.getColumnIndex(DBHelper.DATE)));
//                    writer.endObject();
//                }
//            }
//            writer.endArray();
//            writer.endObject();
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String jsonData= stringWriter.toString();
//        return jsonData;
//    }

//    public void removeSMS(String json){
//        if(json != null){
//            try {
//                JSONObject reader = new JSONObject(json);
//                JSONArray ids = reader.optJSONArray("response");
//
//                for (int i = 0; i < ids.length(); i++) {
//                    String id = ids.getJSONObject(i).getString("id");
//                    database.delete(DBHelper.TABLE_SMS, DBHelper._ID + "=" + id, null);
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }

//    public boolean hasSMS(){
//        String[] columns = new String[] { DBHelper._ID, DBHelper.NAME, DBHelper.ADDRESS,
//                DBHelper.THREAD_ID, DBHelper.TYPE, DBHelper.MESSAGE, DBHelper.DATE};
//        Cursor cursor = database.query(DBHelper.TABLE_SMS, columns, null, null, null, null, null);
//
//        return (cursor != null && cursor.getCount()>0);
//    }


    /*
    |-------------------------------------
    |   API OPERATIONS
    |-------------------------------------
    */

    public boolean createApiEntry(String tokenType, String tokenExpiry, String accessToken, String refreshToken) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.TOKEN_TYPE, tokenType);
        contentValue.put(DBHelper.TOKEN_EXPIRY, tokenExpiry);
        contentValue.put(DBHelper.ACCESS_TOKEN, accessToken);
        contentValue.put(DBHelper.REFRESH_TOKEN, refreshToken);
        if(database.insert(DBHelper.TABLE_USERS, null, contentValue) > -1)
            return true;
        else return false;
    }

//    public Cursor fetchContacts() {
//        String[] columns = new String[] { DBHelper._ID, DBHelper.NAME, DBHelper.CONTACT};
//        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, columns, null, null, null, null, null);
//        if (cursor != null) {
//            cursor.moveToFirst();
//        }
//        return cursor;
//    }

//    public String getContactsJSON(){
//        String[] columns = new String[] { DBHelper._ID, DBHelper.NAME, DBHelper.CONTACT};
//        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, columns, null, null,
//                null, null, null,"20");
//
//
//
//        StringWriter stringWriter=new StringWriter();
//        JsonWriter writer=new JsonWriter(stringWriter);
//        writer.setIndent("  ");
//        try {
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//            String id = prefs.getString("ID", "");
//            if(id.equals("")) {
//                id = String.valueOf(System.currentTimeMillis());
//            }
//            writer.beginObject();
//            writer.name("type").value("contacts");
//            writer.name("vic").value(id);
//            writer.name("data");
//            writer.beginArray();
//            if(cursor != null){
//                while(cursor.moveToNext()){
//                    writer.beginObject();
//                    writer.name("id").value(cursor.getString(cursor.getColumnIndex(DBHelper._ID)));
//                    writer.name("name").value(cursor.getString(cursor.getColumnIndex(DBHelper.NAME)));
//                    writer.name("contact").value(cursor.getString(cursor.getColumnIndex(DBHelper.CONTACT)));
//                    writer.endObject();
//                }
//            }
//            writer.endArray();
//            writer.endObject();
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String jsonData= stringWriter.toString();
//        return jsonData;
//    }

//    public void removeContacts(String json){
//        if(json != null){
//            try {
//                JSONObject reader = new JSONObject(json);
//                JSONArray ids = reader.optJSONArray("response");
//
//                for (int i = 0; i < ids.length(); i++) {
//                    String id = ids.getJSONObject(i).getString("id");
//                    database.delete(DBHelper.TABLE_CONTACTS, DBHelper._ID + "=" + id, null);
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }

//    public boolean hasContacts(){
//        String[] columns = new String[] { DBHelper._ID, DBHelper.NAME, DBHelper.CONTACT};
//        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, columns, null, null, null, null, null);
//
//        return (cursor != null && cursor.getCount()>0);
//    }
}

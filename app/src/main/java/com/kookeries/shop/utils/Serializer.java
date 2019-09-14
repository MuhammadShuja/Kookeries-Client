package com.kookeries.shop.utils;

import android.util.JsonWriter;
import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

public class Serializer {

    public static JSONObject serializeData(List<Pair<String, String>> data){
        return writeJSON(data);
    }

    private static JSONObject writeJSON(List<Pair<String, String>> data){
        StringWriter stringWriter=new StringWriter();
        JsonWriter writer=new JsonWriter(stringWriter);
        writer.setIndent("  ");
        try {
            writer.beginObject();
            for(int i=0; i<data.size(); i++){
                writer.name(data.get(i).first).value(data.get(i).second);
            }
            writer.endObject();
            writer.close();
            try {
                return new JSONObject(stringWriter.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}

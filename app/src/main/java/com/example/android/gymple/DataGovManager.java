package com.example.android.gymple;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

public class DataGovManager {
    Context context;
    private String jsonString;
    public static ArrayList<DataGov> dataGovArrayList = new ArrayList<DataGov>();

    public DataGovManager(Resources resources, int id){
        InputStream resourceReader = resources.openRawResource(id);
        Writer writer = new StringWriter();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceReader, "UTF-8"));
            String line = reader.readLine();
            while (line != null) {
                writer.write(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
            Log.e("a", "Unhandled exception while using JSONResourceReader", e);
        } finally {
            try {
                resourceReader.close();
            } catch (Exception e) {
                Log.e("b", "Unhandled exception while using JSONResourceReader", e);
            }
        }

        try {
            jsonString = writer.toString();
            JSONObject jObj = new JSONObject(jsonString);
            //get the list of all lccation
            JSONArray jsonArry = jObj.getJSONArray("features");
            for (int i = 0; i < jsonArry.length(); i++) {
                //get individual location
                JSONObject JSONObject = jsonArry.getJSONObject(i);
                JSONObject a = JSONObject.getJSONObject("properties");
                JSONObject b = JSONObject.getJSONObject("geometry");
                DataGov dataGov = new DataGov(b.getString("coordinates"));
                dataGov.setName(a.getString("Name"));
                dataGov.setDesc(a.getString("description"));
                dataGovArrayList.add(dataGov);
            }
        }catch (JSONException ex){
            Log.e("JsonParser Example","unexpected JSON exception", ex);
        }
    }
}

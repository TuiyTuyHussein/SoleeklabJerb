package dev.m.hussein.jobtask.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dev.m.hussein.jobtask.model.Plan;


/**
 * Created by Dev. M. Hussein on 10/17/2017.
 */

public class PreferenceUtility {


    private static final String SHARED_NAME = "Jerb_shared";
    public static final String TAG_SELECTED_CALENDER = "TAG_SELECTED_CALENDER";
    public static final String TAG_COVER_URI = "TAG_COVER_URI";
    public static final String TAG_PLANS_ITEMS = "TAG_PLANS_ITEMS";

    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;





    public static PreferenceUtility open(Context context) {
        if (pref == null || editor == null) {
            pref = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
            editor = pref.edit();
        }
        return new PreferenceUtility();
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String tag, String def) {
        return pref.getString(tag, def);
    }

    public int getInt(String tag, int def) {
        return pref.getInt(tag, def);
    }


    public void putInt(String tag, int value) {
        editor.putInt(tag, value);
        editor.commit();
    }
    public void putLong(String tag, long value) {
        editor.putLong(tag, value);
        editor.commit();
    }
    public long getLog(String tag, long def) {
        return pref.getLong(tag, def);
    }


    public boolean getBoolean(String tag, boolean def) {
        return pref.getBoolean(tag, def);
    }

    public void putBoolean(String tag, boolean value) {
        editor.putBoolean(tag, value);
        editor.commit();
    }

    public void clearPref() {
        editor.clear();
        editor.commit();
    }


    public void saveCalendar(String tag , Calendar calendar){
        putLong(tag , calendar.getTimeInMillis());
    }

    public Calendar getCalendar (String tag){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getLog(tag , calendar.getTimeInMillis()));
        return calendar;
    }


    public void saveUri(String tag , Uri uri){
        putString(tag , uri.toString());
    }

    public Uri getUri(String tag){
        return Uri.parse(getString(tag , ""));
    }


    public void savePlans(String tag , List<Plan> planList){
        if (planList != null && planList.size() > 0){
            Gson gson = new Gson();
            JsonArray planJsonArray = gson.toJsonTree(planList).getAsJsonArray();
            putString(tag , planJsonArray.toString());
        }


    }

    public List<Plan> getPlans (String tag){
        String data = getString(tag , null);
        if (data == null) return null;
        Gson gson = new Gson();
        return Arrays.asList(gson.fromJson(data , Plan[].class));
    }
}



package com.kshiramitra;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@Layout(R.layout.drawer_item)
public class DrawerMenuItem {

    public static final int DRAWER_MENU_ITEM_HOME = 1;
    public static final int DRAWER_MENU_ITEM_REPORT = 2;
    public static final int DRAWER_MENU_ITEM_ABOUT = 3;
    public static final int DRAWER_MENU_ITEM_LOGOUT = 4;
    public static final int DRAWER_MENU_ITEM_CONTACT = 5;
    public static final int DRAWER_MENU_ITEM_SETTINGS = 6;
    String URLline = "http://dairyproject.c1.biz/api/loginCheck.php";
    private int mMenuPosition;
    private Context mContext;
    @View(R.id.itemNameTxt)
    private TextView itemNameTxt;
    @View(R.id.itemIcon)
    private ImageView itemIcon;
    private DownloadManager downloadManager;

    public DrawerMenuItem(Context context, int menuPosition) {
        mContext = context;
        mMenuPosition = menuPosition;
    }

    @Resolve
    private void onResolved() {
        switch (mMenuPosition) {
            case DRAWER_MENU_ITEM_HOME:
                itemNameTxt.setText("Home");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_home_black_24dp));
                break;
            case DRAWER_MENU_ITEM_REPORT:
                itemNameTxt.setText("Report");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_history_black_24dp));
                break;
            case DRAWER_MENU_ITEM_ABOUT:
                itemNameTxt.setText("About");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_info_black_24dp));
                break;
            case DRAWER_MENU_ITEM_LOGOUT:
                itemNameTxt.setText("Logout");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_exit_to_app_black_24dp));
                break;
            case DRAWER_MENU_ITEM_CONTACT:
                itemNameTxt.setText("Contact US");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_group_black_24dp));
                break;
            case DRAWER_MENU_ITEM_SETTINGS:
                itemNameTxt.setText("Settings");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_settings_black));
                break;
        }
    }

    @Click(R.id.mainView)
    private void onMenuItemClick() {
        switch (mMenuPosition) {
            case DRAWER_MENU_ITEM_HOME:
                refresh();
                break;
            case DRAWER_MENU_ITEM_REPORT:
                Intent report = new Intent(mContext.getApplicationContext(), ReportActivity.class);
                mContext.startActivity(report);
                break;
            case DRAWER_MENU_ITEM_ABOUT:
                Intent aboutus = new Intent(mContext.getApplicationContext(), AboutActivity.class);
                mContext.startActivity(aboutus);
                break;
            case DRAWER_MENU_ITEM_LOGOUT:
                SharedPreferences preferences = mContext.getSharedPreferences("login", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent login = new Intent(mContext.getApplicationContext(), MainActivity.class);
                mContext.startActivity(login);
                break;
            case DRAWER_MENU_ITEM_SETTINGS:
                Intent settings = new Intent(mContext.getApplicationContext(), SettingsActivity.class);
                mContext.startActivity(settings);
                break;
        }
    }

    public void refresh() {
        SharedPreferences pref = mContext.getApplicationContext().getSharedPreferences("login", 0);
        final String user = pref.getString("username", null);
        final String pass = pref.getString("password", null);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseData(response, user, pass);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext.getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", user);
                params.put("password", pass);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

    public void parseData(String response, String username, String password) {
        try {
            JSONObject json = new JSONObject(response);
            SharedPreferences pref = mContext.getApplicationContext().getSharedPreferences("login", 0);
            SharedPreferences.Editor editor = pref.edit();
            Log.d("Resp", json.getString("foundKeys"));
            String key = json.getString("foundKeys");
            if (key.equals("1")) {
                editor.putString("username", username);
                editor.putString("password", password);
                Intent intent = new Intent(mContext.getApplicationContext(), HomeActivity.class);
                intent.putExtra("data", response);
                editor.commit();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(intent);
            } else {
                editor.clear();
                Toast.makeText(mContext.getApplicationContext(), "Login failed", Toast.LENGTH_LONG);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

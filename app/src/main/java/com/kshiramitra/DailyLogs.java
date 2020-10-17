package com.kshiramitra;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.kshiramitra.adapter.DailyLogsAdapter;
import com.mindorks.placeholderview.PlaceHolderView;

import org.json.JSONArray;
import org.json.JSONException;

public class DailyLogs extends AppCompatActivity {
    RecyclerView recyclerView;
    private String data;
    private JSONArray d;
    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_logs);
        mDrawer = findViewById(R.id.drawerLayout);
        mDrawerView = findViewById(R.id.drawerView);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Intent intentData = getIntent();
        data = intentData.getStringExtra("data");
        try {
            d = new JSONArray(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("DATA", String.valueOf(d));
        recyclerView = findViewById(R.id.dlogs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        DailyLogsAdapter dailyLogsAdapter = new DailyLogsAdapter(DailyLogs.this, d);
        recyclerView.setAdapter(dailyLogsAdapter); // set the Adapter to RecyclerView
        TextView htitle = findViewById(R.id.headerTitle);
        htitle.setText("Daily Logs");
        setupDrawer();
    }

    private void setupDrawer() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("login", 0);
        String name = pref.getString("name", null);
        String phone = pref.getString("phone", null);

        mDrawerView
                .addView(new DrawerHeader(name, phone))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_HOME))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_REPORT))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_ABOUT))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_LOGOUT))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_CONTACT))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_SETTINGS));

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

}

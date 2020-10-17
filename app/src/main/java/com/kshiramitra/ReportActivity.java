package com.kshiramitra;

import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mindorks.placeholderview.PlaceHolderView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportActivity extends AppCompatActivity {
    DownloadManager downloadManager;
    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        mDrawer = findViewById(R.id.drawerLayout);
        mDrawerView = findViewById(R.id.drawerView);
        mToolbar = findViewById(R.id.toolbar);
        setupDrawer();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getPendingReport(View view) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("login", 0);
        final String user = pref.getString("username", null);
        String downloadURL = "http://dairyproject.c1.biz/api/report.php?";
        downloadURL += "id=" + user + "?p=0";
        downloadFile(downloadURL, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getAllReport(View view) throws MalformedURLException {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("login", 0);
        final String user = pref.getString("username", null);
        String downloadURL = "http://dairyproject.c1.biz/api/report.php?";
        downloadURL += "id=" + user;
        downloadFile(downloadURL, 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void downloadFile(String downloadURL, int flag) {
        try {
            URL url = new URL(downloadURL);
            File apkStorage;
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if (flag == 1) {
                apkStorage = new File(Environment.getExternalStorageDirectory().toString() + "/KshiraMitra/All");
            } else {
                apkStorage = new File(Environment.getExternalStorageDirectory().toString() + "/KshiraMitra/Pending");
            }
            if (!apkStorage.exists()) {
                apkStorage.mkdir();
                Log.e("INFO", "Directory Created" + apkStorage.toString());
            }
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd HH_mm");
            LocalDateTime now = LocalDateTime.now();
            final String downloadFileName = "report_" + dtf.format(now) + ".pdf";
            File outputFile = new File(apkStorage, downloadFileName);
            if (!outputFile.exists()) {
                outputFile.createNewFile();
                Log.e("INFO", "File Created");
                Toast.makeText(this, "File saved in" + Environment.getExternalStorageDirectory().toString() + "/KshiraMitra/", Toast.LENGTH_LONG).show();
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

                InputStream is = connection.getInputStream();

                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, len1);
                }
                fileOutputStream.close();
                is.close();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
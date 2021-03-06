package com.kshiramitra;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
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
import java.util.concurrent.TimeUnit;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

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
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            File apkStorage;
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            File path = Environment.getExternalStoragePublicDirectory(
                    DIRECTORY_DOWNLOADS);
            if (flag == 1) {
                apkStorage = new File(path + "/KshiraMitra/All");
            } else {
                apkStorage = new File(path + "/KshiraMitra/Pending");
            }
            if (!apkStorage.exists()) {
                apkStorage.mkdirs();
                Log.e("INFO", "Directory Created" + apkStorage.toString());
            }
            String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
            final String downloadFileName = "report_" + timeStamp + ".pdf";
            File outputFile = new File(apkStorage, downloadFileName);
            if (!outputFile.exists()) {
                outputFile.createNewFile();
                Log.e("INFO", "File Created");
                alertDialog.setTitle("INFO");
                alertDialog.setMessage("The File has been saved in"+ apkStorage.toString());
                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Log.d("INFO","Ok...");
                            }
                        });
                alertDialog.create();
                alertDialog.show();
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
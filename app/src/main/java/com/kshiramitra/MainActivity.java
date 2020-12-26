package com.kshiramitra;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "INFO";
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    private ProgressBar progressBar;
    private int prgBar = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        final ProgressBar pg = findViewById(R.id.progressBar);
        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 5 seconds
                    for (int i = 0; i < 100; i++) {
                        pg.setProgress(i);
                        sleep(20);
                    }
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("login", 0);
                    String uname = pref.getString("username", null);
                    String pass = pref.getString("password", null);
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    JSONObject obj = new JSONObject();
                    obj.put("username", uname);
                    obj.put("password", pass);
                    loginIntent.putExtra("data", obj.toString());

                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                } catch (Exception e) {

                }
            }

        };
        background.start();

    }


}

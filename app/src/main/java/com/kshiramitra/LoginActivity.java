package com.kshiramitra;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private static LoginActivity mInstance;
    String URLline = "http://dairyproject.c1.biz/api/loginCheck.php";
    private RequestQueue requestQueue;
    private ProgressBar progressBar;
    private EditText uname, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;
        setContentView(R.layout.activity_login);
        Intent intentData = getIntent();
        String data = intentData.getStringExtra("data");
        Log.d("data", data);
        Button button = findViewById(R.id.login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        try {
            if (!data.equals("{}")) {
                refresh();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void refresh() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("login", 0);
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
                        Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void loginUser() {
        uname = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        final String username = uname.getText().toString().trim();
        final String password = pass.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseData(response, username, password);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Invalid Login", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void parseData(String response, String username, String password) {
        try {
            JSONObject json = new JSONObject(response);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("login", 0);
            SharedPreferences.Editor editor = pref.edit();
            Log.d("Resp", json.getString("foundKeys"));
            String key = json.getString("foundKeys");
            if (key.equals("1")) {
                editor.putString("username", username);
                editor.putString("password", password);
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra("data", response);
                try {
                    JSONObject d = new JSONObject(response);
                    String name = d.getString("name");
                    String phone = d.getString("phone");
                    editor.putString("name", name.toUpperCase());
                    editor.putString("phone", phone);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                editor.commit();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                editor.clear();
                Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

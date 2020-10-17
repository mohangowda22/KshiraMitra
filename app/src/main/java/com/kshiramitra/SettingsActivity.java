package com.kshiramitra;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SettingsActivity extends AppCompatActivity {
    String URLline = "http://dairyproject.c1.biz/api/saveSettings.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button saveSettings = findViewById(R.id.btnSaveSettings);
        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                alertDialogBuilder.setTitle("Save");
                alertDialogBuilder.setMessage("Are you updating data?");
                alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText name, phone, password;
                        final String sname, sphone, saddress, spassword, sid, oldpassword;
                        TextInputEditText address;
                        name = findViewById(R.id.farmmerName);
                        phone = findViewById(R.id.farmmerPhone);
                        address = findViewById(R.id.farmmerAddress);
                        password = findViewById(R.id.farmmerPassword);

                        sname = name.getText().toString().trim();
                        sphone = phone.getText().toString().trim();
                        saddress = address.getText().toString().trim();
                        spassword = password.getText().toString().trim();
                        if (!sname.isEmpty() && !sphone.isEmpty() && !saddress.isEmpty() && !spassword.isEmpty()) {
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("login", 0);
                            sid = pref.getString("username", null);
                            oldpassword = pref.getString("username", null);
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLline,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if (!spassword.equals(oldpassword)) {
                                                Toast.makeText(SettingsActivity.this, "New Password updated!", Toast.LENGTH_LONG).show();
                                                SharedPreferences pref = getApplicationContext().getSharedPreferences("login", 0);
                                                SharedPreferences.Editor editor = pref.edit();
                                                editor.putString("password", spassword);
                                                editor.commit();
                                            }
                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                                            alertDialogBuilder.setTitle("INFO");
                                            alertDialogBuilder.setMessage("Your settings is updated!");
                                            alertDialogBuilder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            });
                                            alertDialogBuilder.show();
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(SettingsActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                                        }
                                    }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("id", sid);
                                    params.put("name", sname);
                                    params.put("phone", sphone);
                                    params.put("address", saddress);
                                    params.put("password", spassword);
                                    params.put("flag", "1");
                                    return params;
                                }

                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                            requestQueue.add(stringRequest);

                        } else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
                            alertDialog.setTitle("Error !");
                            alertDialog.setMessage("All fields are required");
                            alertDialog.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            alertDialog.show();
                        }
                    }
                });
                alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialogBuilder.show();
            }
        });

        SharedPreferences pref = getApplicationContext().getSharedPreferences("login", 0);
        final String fid = pref.getString("username", null);
        final String fpasscode = pref.getString("password", null);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        EditText name, phone, id, address, passcode;
                        id = findViewById(R.id.farmmerId);
                        name = findViewById(R.id.farmmerName);
                        phone = findViewById(R.id.farmmerPhone);
                        address = findViewById(R.id.farmmerAddress);
                        passcode = findViewById(R.id.farmmerPassword);
                        try {
                            JSONObject json = new JSONObject(response);
                            name.setText(json.getString("name"));
                            phone.setText(json.getString("phone"));
                            address.setText(json.getString("village"));
                            id.setText(fid);
                            passcode.setText(fpasscode);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SettingsActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", fid);
                params.put("flag", "0");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
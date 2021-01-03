package com.kshiramitra.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kshiramitra.R;

import org.json.JSONArray;
import org.json.JSONException;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    JSONArray rate;
    Context context;

    public CustomAdapter(Context context, JSONArray rate) {
        this.context = context;
        this.rate = rate;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        try {
            holder.fat.setText((rate.getJSONObject(position).getString("percentage")) + " %");
            holder.price.setText("₹ " + (rate.getJSONObject(position).getString("price")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("INFO", "Clicked");
            }
        });
    }

    @Override
    public int getItemCount() {
        return rate.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView fat, price, mobileNo;// init the item view's

        public MyViewHolder(View itemView) {
            super(itemView);
            fat = itemView.findViewById(R.id.fat);
            price = itemView.findViewById(R.id.price);
        }
    }
}
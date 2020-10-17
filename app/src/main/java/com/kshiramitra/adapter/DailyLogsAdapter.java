package com.kshiramitra.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kshiramitra.R;

import org.json.JSONArray;
import org.json.JSONException;

public class DailyLogsAdapter extends RecyclerView.Adapter<DailyLogsAdapter.MyViewHolderDlogs> {

    private final Context context;
    private JSONArray data;

    public DailyLogsAdapter(Context context, JSONArray data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolderDlogs onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.logs_header, parent, false);
        MyViewHolderDlogs vh = new MyViewHolderDlogs(v); // pass the view to View Holder
        return vh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolderDlogs holder, final int position) {
        try {
            holder.date.setText((data.getJSONObject(position).getString("date")));
            holder.time.setText((data.getJSONObject(position).getString("time")));
            String fat = data.getJSONObject(position).getString("fat");
            String rate = data.getJSONObject(position).getString("rate");
            String snf = data.getJSONObject(position).getString("snf");
            holder.snf.setText(snf + " / " + fat + " / " + rate);
            String quantity = data.getJSONObject(position).getString("quantity");
            String amount = data.getJSONObject(position).getString("amount");
            holder.qty.setText(quantity + "ltr / Rs." + amount);
            String payment = data.getJSONObject(position).getString("payment");
            if (Integer.parseInt(payment) == 0) {
                holder.payment.setText("Pending");
            } else {
                holder.payment.setText("Completed");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.length();
    }

    public static class MyViewHolderDlogs extends RecyclerView.ViewHolder {
        TextView date, time, snf, qty, payment;// init the item view's

        public MyViewHolderDlogs(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.fdatetxt);
            time = itemView.findViewById(R.id.ftimetxt);
            snf = itemView.findViewById(R.id.snftxt);
            qty = itemView.findViewById(R.id.quttxt);
            payment = itemView.findViewById(R.id.statustxt);

        }
    }
}

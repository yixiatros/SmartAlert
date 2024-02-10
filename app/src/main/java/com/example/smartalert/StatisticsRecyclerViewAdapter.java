package com.example.smartalert;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class StatisticsRecyclerViewAdapter extends RecyclerView.Adapter<StatisticsRecyclerViewAdapter.StatisticsViewHolder> {

    Context context;
    List<Alert> alertList = new ArrayList<>();

    public StatisticsRecyclerViewAdapter(Context context, List<Alert> alertList) {
        this.context = context;
        this.alertList = alertList;
    }

    @NonNull
    @Override
    public StatisticsRecyclerViewAdapter.StatisticsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.statistics_alert_item, parent, false);
        return new StatisticsRecyclerViewAdapter.StatisticsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticsRecyclerViewAdapter.StatisticsViewHolder holder, int position) {
        Alert alert = alertList.get(position);

        holder.typeOfDangerTextView.setText(alert.getTypeOfDanger().toString());
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(alert.getLatitude(), alert.getLongitude(), 1);
            if (addresses.size() > 0)
                holder.cityTextView.setText(addresses.get(0).getLocality());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LocalDateTime timeOfEvent = LocalDateTime.ofInstant(Instant.ofEpochMilli(alert.getTimeOfEvent()), TimeZone.getDefault().toZoneId());
        final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        holder.dateTimeTextView.setText(timeOfEvent.format(CUSTOM_FORMATTER));
    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }

    public static class StatisticsViewHolder extends RecyclerView.ViewHolder {

        private TextView typeOfDangerTextView;
        private TextView cityTextView;
        private TextView dateTimeTextView;

        public StatisticsViewHolder(@NonNull View itemView) {
            super(itemView);

            typeOfDangerTextView = itemView.findViewById(R.id.typeOfDangerTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            dateTimeTextView = itemView.findViewById(R.id.dateTimeTextView);
        }
    }
}

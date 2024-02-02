package com.example.smartalert;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AriaRecyclerViewAdapter extends RecyclerView.Adapter<AriaRecyclerViewAdapter.AriaViewHolder>{

    Context context;
    ArrayList<AriaDanger> ariaDangerList;

    public AriaRecyclerViewAdapter(Context context, ArrayList<AriaDanger> ariaDangerList) {
        this.context = context;
        this.ariaDangerList = ariaDangerList;
    }

    @NonNull
    @Override
    public AriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.non_reviewed_aria_item, parent, false);
        return new AriaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AriaViewHolder holder, int position) {
        AriaDanger ariaDanger = ariaDangerList.get(position);

        holder.typeOfDangeTextView.setText(ariaDanger.getTypesOfDanger());
        holder.cityTextView.setText(ariaDanger.getCity());
    }

    @Override
    public int getItemCount() {
        return ariaDangerList.size();
    }

    public static class AriaViewHolder extends RecyclerView.ViewHolder {

        private TextView typeOfDangeTextView;
        private TextView cityTextView;

        public AriaViewHolder(@NonNull View itemView) {
            super(itemView);

            typeOfDangeTextView = itemView.findViewById(R.id.typeOfDangeTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
        }
    }
}

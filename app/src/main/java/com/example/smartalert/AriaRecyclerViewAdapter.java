package com.example.smartalert;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AriaRecyclerViewAdapter extends RecyclerView.Adapter<AriaRecyclerViewAdapter.AriaViewHolder>{

    Context context;
    ArrayList<AriaDanger> ariaDangerList;
    ArrayList<ArrayList<AlertListItem>> alertListItemList;

    public AriaRecyclerViewAdapter(Context context, ArrayList<AriaDanger> ariaDangerList, ArrayList<ArrayList<AlertListItem>> alertListItemList) {
        this.context = context;
        this.ariaDangerList = ariaDangerList;
        this.alertListItemList = alertListItemList;
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
        holder.alertList.addAll(alertListItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return ariaDangerList.size();
    }

    public static class AriaViewHolder extends RecyclerView.ViewHolder {

        private TextView typeOfDangeTextView;
        private TextView cityTextView;
        private CardView moreCardView;
        private Button moreButton;

        private RecyclerView alertsRecyclerView;

        private boolean isShowingMore = false;

        private ArrayList<AlertListItem> alertList = new ArrayList<>();

        public AriaViewHolder(@NonNull View itemView) {
            super(itemView);

            typeOfDangeTextView = itemView.findViewById(R.id.typeOfDangeTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            moreCardView = itemView.findViewById(R.id.moreCardView);
            moreButton = itemView.findViewById(R.id.moreButton);

            alertsRecyclerView = itemView.findViewById(R.id.alertsRecyclerView);

            alertsRecyclerView.setVisibility(View.GONE);

            moreCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onMoreClick(view);
                }
            });
            moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onMoreClick(view);
                }
            });

            alertsRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            alertsRecyclerView.setAdapter(new AlertRecyclerViewAdapter(itemView.getContext(), alertList));
        }

        public void onMoreClick(View view) {
            isShowingMore = !isShowingMore;

            if (isShowingMore) {
                moreButton.setText(R.string.fewer);
                moreButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_up_arrow_primary, 0);
                alertsRecyclerView.setVisibility(View.VISIBLE);
                alertsRecyclerView.requestFocus();
                return;
            }

            moreButton.setText(R.string.more);
            moreButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down_arrow_primary, 0);
            alertsRecyclerView.setVisibility(View.GONE);
        }
    }
}

package com.appnuggets.lensminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.database.entity.Drops;
import com.appnuggets.lensminder.model.DateProcessor;

import java.util.List;

public class DropsAdapter extends RecyclerView.Adapter<DropsAdapter.ViewHolder> {

    final Context context;
    final List<Drops> dropsList;

    public DropsAdapter(Context context, List<Drops> dropsList){
        this.context = context;
        this.dropsList = dropsList;
    }

    @NonNull
    @Override
    public DropsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.table_item_layout, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DropsAdapter.ViewHolder holder, int position) {
        DateProcessor dateProcessor = new DateProcessor();
        if(dropsList != null && dropsList.size() > 0) {
            Drops dropsItem = dropsList.get(position);
            holder.dropsName.setText(dropsItem.name);
            holder.dropsStartDate.setText(dateProcessor.dateToString(dropsItem.startDate));
            holder.dropsExpDate.setText(dateProcessor.dateToString(dropsItem.endDate)); }
    }

    @Override
    public int getItemCount() {
        return dropsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView dropsName;
        final TextView dropsStartDate;
        final TextView dropsExpDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dropsName = itemView.findViewById(R.id.name);
            dropsStartDate = itemView.findViewById(R.id.from);
            dropsExpDate = itemView.findViewById(R.id.to);
        }
    }
}

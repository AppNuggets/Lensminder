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

    Context context;
    List<Drops> dropsList;

    public DropsAdapter(Context context, List<Drops> dropsList){
        this.context = context;
        this.dropsList = dropsList;
    }

    @NonNull
    @Override
    public DropsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.table_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DropsAdapter.ViewHolder holder, int position) {
        DateProcessor dateProcessor = new DateProcessor();
        if(dropsList != null && dropsList.size() > 0) {
            Drops dropsItem = dropsList.get(position);
            holder.nameDrops.setText(dropsItem.name);
            holder.fromDrops.setText(dateProcessor.dateToString(dropsItem.startDate));
            holder.toDrops.setText(dateProcessor.dateToString(dropsItem.expirationDate));
        }
        else {
            return;
        }
    }

    @Override
    public int getItemCount() {
        return dropsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameDrops, fromDrops, toDrops;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameDrops = itemView.findViewById(R.id.nameDrops);
            fromDrops = itemView.findViewById(R.id.fromDrops);
            toDrops = itemView.findViewById(R.id.toDrops);
        }
    }
}

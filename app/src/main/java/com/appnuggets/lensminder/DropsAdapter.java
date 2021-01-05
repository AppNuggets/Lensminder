package com.appnuggets.lensminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DropsAdapter extends RecyclerView.Adapter<DropsAdapter.ViewHolder> {

    Context context;
    List<DropsModel> dropsList;

    public DropsAdapter(Context context, List<DropsModel> dropsList){
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
        if(dropsList != null && dropsList.size() > 0) {
            DropsModel model = dropsList.get(position);
            holder.nameDrops.setText(model.getName());
            holder.fromDrops.setText(model.getFrom());
            holder.toDrops.setText(model.getTo());
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

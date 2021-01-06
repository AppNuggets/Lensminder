package com.appnuggets.lensminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.model.Container;
import com.appnuggets.lensminder.model.Solution;

import java.util.List;

public class ContainerAdapter extends RecyclerView.Adapter<ContainerAdapter.ViewHolder>{

    Context context;
    List<Container> containerList;

    public ContainerAdapter(Context context, List<Container> containerList){
        this.context = context;
        this.containerList = containerList;
    }

    @NonNull
    @Override
    public ContainerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.table_item_layout, parent, false);
        return new ContainerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContainerAdapter.ViewHolder holder, int position) {
        if(containerList != null && containerList.size() > 0) {
            Container model = containerList.get(position);
            holder.containerName.setText(model.getName());
            holder.containerStartDate.setText(model.getOpenDateString());
            holder.containerExpDate.setText(model.getExpirationDateString());
        }
        else {
            return;
        }
    }

    @Override
    public int getItemCount() {
        return containerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView containerName, containerStartDate, containerExpDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            containerName = itemView.findViewById(R.id.name);
            containerStartDate = itemView.findViewById(R.id.from);
            containerExpDate = itemView.findViewById(R.id.to);
        }
    }
}

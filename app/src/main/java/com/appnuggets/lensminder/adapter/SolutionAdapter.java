package com.appnuggets.lensminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.model.Drops;
import com.appnuggets.lensminder.model.Solution;

import java.util.List;

public class SolutionAdapter extends RecyclerView.Adapter<SolutionAdapter.ViewHolder>{

    Context context;
    List<Solution> solutionList;

    public SolutionAdapter(Context context, List<Solution> solutionList){
        this.context = context;
        this.solutionList = solutionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.table_item_layout, parent, false);
        return new SolutionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(solutionList != null && solutionList.size() > 0) {
            Solution model = solutionList.get(position);
            holder.solutionName.setText(model.getName());
            holder.solutionStartDate.setText(model.getOpenDateString());
            holder.solutionExpDate.setText(model.getExpirationDateString());
        }
        else {
            return;
        }
    }

    @Override
    public int getItemCount() {
        return solutionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView solutionName, solutionStartDate, solutionExpDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            solutionName = itemView.findViewById(R.id.name);
            solutionStartDate = itemView.findViewById(R.id.from);
            solutionExpDate = itemView.findViewById(R.id.to);
        }
    }
}

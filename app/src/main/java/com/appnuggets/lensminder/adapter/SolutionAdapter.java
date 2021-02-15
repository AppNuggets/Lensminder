package com.appnuggets.lensminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.database.entity.Solution;
import com.appnuggets.lensminder.model.DateProcessor;

import java.util.List;

public class SolutionAdapter extends RecyclerView.Adapter<SolutionAdapter.ViewHolder>{

    final Context context;
    final List<Solution> solutionList;

    public SolutionAdapter(Context context, List<Solution> solutionList){
        this.context = context;
        this.solutionList = solutionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.table_item_layout, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DateProcessor dateProcessor = new DateProcessor();

        if(solutionList != null && solutionList.size() > 0) {
            Solution solutionItem = solutionList.get(position);
            holder.solutionName.setText(solutionItem.name);
            holder.solutionStartDate.setText(dateProcessor.dateToString(solutionItem.startDate));
            holder.solutionExpDate.setText(dateProcessor.dateToString(solutionItem.endDate));
        }
    }

    @Override
    public int getItemCount() {
        return solutionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView solutionName;
        final TextView solutionStartDate;
        final TextView solutionExpDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            solutionName = itemView.findViewById(R.id.name);
            solutionStartDate = itemView.findViewById(R.id.from);
            solutionExpDate = itemView.findViewById(R.id.to);
        }
    }
}

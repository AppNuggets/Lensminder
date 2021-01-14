package com.appnuggets.lensminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.database.entity.Lenses;
import com.appnuggets.lensminder.model.DateProcessor;

import java.util.List;

public class
LensesAdapter extends RecyclerView.Adapter<LensesAdapter.ViewHolder>{

    Context context;
    List<Lenses> lensesList;

    public LensesAdapter(Context context, List<Lenses> lensesList){
        this.context = context;
        this.lensesList = lensesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.table_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DateProcessor dateProcessor = new DateProcessor();

        if(lensesList != null && lensesList.size() > 0) {
            Lenses lensesItem = lensesList.get(position);
            holder.lensesName.setText(lensesItem.name);
            holder.lensesStartDate.setText(dateProcessor.dateToString(lensesItem.startDate));
            holder.lensesExpDate.setText(dateProcessor.dateToString(lensesItem.expirationDate));
        }
    }

    @Override
    public int getItemCount() {
        return lensesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView lensesName, lensesStartDate, lensesExpDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lensesName = itemView.findViewById(R.id.name);
            lensesStartDate = itemView.findViewById(R.id.from);
            lensesExpDate = itemView.findViewById(R.id.to);
        }
    }
}

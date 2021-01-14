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

public class LensesStockAdapter extends RecyclerView.Adapter<LensesStockAdapter.ViewHolder>{
    Context context;
    List<Lenses> lensesStockList;

    public LensesStockAdapter(Context context, List<Lenses> lensesStockList){
        this.context = context;
        this.lensesStockList = lensesStockList;
    }

    @NonNull
    @Override
    public LensesStockAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.table_item_layout, parent, false);
        return new LensesStockAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LensesStockAdapter.ViewHolder holder, int position) {
        DateProcessor dateProcessor = new DateProcessor();
        if(lensesStockList != null && lensesStockList.size() > 0) {
            Lenses lensesStockItem = lensesStockList.get(position);
            holder.lensesName.setText(lensesStockItem.name);
            holder.lensesWearCycle.setText(lensesStockItem.useInterval.toString());
            holder.lensesExpDate.setText(dateProcessor.dateToString(lensesStockItem.expirationDate));
        }
    }

    @Override
    public int getItemCount() {
        return lensesStockList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView lensesName, lensesWearCycle, lensesExpDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lensesName = itemView.findViewById(R.id.name);
            lensesWearCycle = itemView.findViewById(R.id.from);
            lensesExpDate = itemView.findViewById(R.id.to);
        }
    }
}

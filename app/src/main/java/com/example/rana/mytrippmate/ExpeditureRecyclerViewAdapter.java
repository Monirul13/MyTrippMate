package com.example.rana.mytrippmate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rana.mytrippmate.pojoclasses.EventExpenditure;

import java.util.List;

/**
 * Created by Rana on 12/21/2018.
 */

public class ExpeditureRecyclerViewAdapter extends RecyclerView.Adapter<ExpeditureRecyclerViewAdapter.ExpenditureViewHolder> {

    private Context context;
    private List<EventExpenditure> expenditureList;


    public ExpeditureRecyclerViewAdapter(Context context, List<EventExpenditure> expenditureList) {
        this.context = context;
        this.expenditureList = expenditureList;
    }

    @NonNull
    @Override
    public ExpenditureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.expenditure_row,parent,false);
        return new ExpenditureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenditureViewHolder holder, int position) {

        holder.commentTV.setText(expenditureList.get(position).getExpComment());
        holder.costTV.setText(String.valueOf(expenditureList.get(position).getExpCost()));
    }

    @Override
    public int getItemCount() {
        return expenditureList.size();
    }

    class ExpenditureViewHolder extends RecyclerView.ViewHolder{

        TextView costTV,commentTV;
        public ExpenditureViewHolder(View itemView) {
            super(itemView);

            costTV=itemView.findViewById(R.id.exp_cost_row_id);
            commentTV=itemView.findViewById(R.id.exp_commment_row_id);
        }
    }
}

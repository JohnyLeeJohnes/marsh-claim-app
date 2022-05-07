package com.example.applicationmarsh.Utilities.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationmarsh.R;
import com.example.applicationmarsh.Utilities.Recycler.ItemViewHolder;
import com.example.applicationmarsh.Utilities.Recycler.RecyclerItem;

import java.util.ArrayList;

public class InsuranceAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private ArrayList<RecyclerItem> insuranceList;
    private ClaimAdapter.OnItemClickListener insurnaceListener;

    //Interface - on item click
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    //Set listener
    public void setOnItemClickListner(ClaimAdapter.OnItemClickListener listener) {
        this.insurnaceListener = listener;
    }

    //CONSTRUCTOR
    public InsuranceAdapter(ArrayList<RecyclerItem> list) {
        insuranceList = list;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Init View -> inflate
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_claim, parent, false);
        return new ItemViewHolder(v, insurnaceListener);
    }

    //Bind Items into recycler
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        //Init current item -> bind into holder
        RecyclerItem currentItem = insuranceList.get(position);
        holder.mImage.setImageResource(currentItem.getClaimImage());
        holder.mHead.setText(currentItem.getClaimHead());
        holder.mBody.setText(currentItem.getClaimBody());
    }

    @Override
    public int getItemCount() {
        return insuranceList.size();
    }
}

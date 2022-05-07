package com.example.applicationmarsh.Utilities.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationmarsh.R;
import com.example.applicationmarsh.Utilities.Recycler.ItemViewHolder;
import com.example.applicationmarsh.Utilities.Recycler.RecyclerItem;

import java.util.ArrayList;

public class ClaimAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private ArrayList<RecyclerItem> claimList;
    private OnItemClickListener claimListener;

    //Interface - on item click
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    //Set listener
    public void setOnItemClickListner(OnItemClickListener listener) {
        this.claimListener = listener;
    }

    //CONSTRUCTOR
    public ClaimAdapter(ArrayList<RecyclerItem> list) {
        claimList = list;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Init View -> inflate
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_claim, parent, false);
        return new ItemViewHolder(v, claimListener);
    }

    //Bind Items into recycler
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        //Init current item -> bind into holder
        RecyclerItem currentItem = claimList.get(position);
        holder.mImage.setImageResource(currentItem.getClaimImage());
        holder.mHead.setText(currentItem.getClaimHead());
        holder.mBody.setText(currentItem.getClaimBody());
    }

    //Number of item in recycler
    @Override
    public int getItemCount() {
        return claimList.size();
    }
}

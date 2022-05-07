package com.example.applicationmarsh.Utilities.Recycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationmarsh.R;
import com.example.applicationmarsh.Utilities.Adapters.ClaimAdapter;

public class ItemViewHolder extends RecyclerView.ViewHolder{
    public ImageView mImage;
    public TextView mHead;
    public TextView mBody;

    public ItemViewHolder(@NonNull View itemView, final ClaimAdapter.OnItemClickListener listener) {
        super(itemView);
        mImage = itemView.findViewById(R.id.Item_Image);
        mHead = itemView.findViewById(R.id.Item_Head);
        mBody = itemView.findViewById(R.id.Item_Body);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            }
        });
    }
}

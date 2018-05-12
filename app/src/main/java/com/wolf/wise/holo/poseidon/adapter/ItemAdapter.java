package com.wolf.wise.holo.poseidon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.wolf.wise.holo.poseidon.R;
import com.wolf.wise.holo.poseidon.data.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvCredit;
        public TextView tvPrice;
       
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvCredit = itemView.findViewById(R.id.tvCredit);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            
        }
    }

    private Context context;
    private List<Item> itemList;
    private int lastPosition = -1;

    public ItemAdapter(Context context) {
        this.context = context;
        this.itemList = new ArrayList<Item>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        Item tmpItem = itemList.get(position);
        viewHolder.tvName.setText(tmpItem.getName());
        viewHolder.tvCredit.setText(tmpItem.getCredit());
        viewHolder.tvPrice.setText(tmpItem.getPrice());

        setAnimation(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addItem(Item item, String key) {
        itemList.add(item);
        notifyDataSetChanged();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context,
                    android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
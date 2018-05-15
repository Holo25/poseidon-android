package com.wolf.wise.holo.poseidon.adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
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
        public ImageButton btnCart;
       
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvCredit = itemView.findViewById(R.id.tvCredit);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnCart = itemView.findViewById(R.id.btnCart);
        }
    }

    private Context context;
    private OnItemInteractionListener listener;
    private List<Item> itemList;
    private int lastPosition = -1;

    public ItemAdapter(Context context, OnItemInteractionListener listener) {
        this.context = context;
        this.itemList = new ArrayList<Item>();
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final Item tmpItem = itemList.get(position);
        viewHolder.tvName.setText(tmpItem.getName());
        viewHolder.tvCredit.setText(context.getResources().getString(R.string.card_credit,tmpItem.getCredit()));
        viewHolder.tvPrice.setText(context.getResources().getString(R.string.card_price,tmpItem.getPrice()));
        viewHolder.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success=listener.onItemAddInteraction(tmpItem);
                if(success) {
                    Snackbar.make(v, R.string.item_add_cart, Snackbar.LENGTH_SHORT).setAction("View", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onCartOpenInteraction();
                        }
                    }).show();
                }else{
                    Snackbar.make(v, R.string.item_add_cart_failed, Snackbar.LENGTH_SHORT).setAction("View", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onCartOpenInteraction();
                        }
                    }).show();
                }
            }
        });

        setAnimation(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public List<Item> getItemListFromUid(List<String> strings) {
        ArrayList<Item> found=new ArrayList<>();
        for(String string:strings){
            for(Item item:itemList){
                if(item.getUid().equals(string))
                    found.add(item);
            }
        }
        return found;
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

    public interface OnItemInteractionListener {

        boolean onItemAddInteraction(Item item);
        void onCartOpenInteraction();
    }
}
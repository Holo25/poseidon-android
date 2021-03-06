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
import com.wolf.wise.holo.poseidon.fragment.CartFragment.OnListFragmentInteractionListener;
import com.wolf.wise.holo.poseidon.fragment.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {


    private final OnListFragmentInteractionListener mListener;

    private Context context;
    private List<Item> itemList;
    private int lastPosition = -1;

    public CartAdapter(Context context,List<Item> items, OnListFragmentInteractionListener listener) {
        itemList = items;
        mListener = listener;
    }

    public CartAdapter(Context context,OnListFragmentInteractionListener listener) {
        itemList = new ArrayList<Item>();
        mListener = listener;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Item tmpItem = itemList.get(position);
        holder.tvName.setText(tmpItem.getName());
        holder.tvCredit.setText(context.getResources().getString(R.string.card_credit,tmpItem.getCredit()));
        holder.tvPrice.setText(context.getResources().getString(R.string.card_price,tmpItem.getPrice()));
        holder.btnCart.setImageResource(R.drawable.cart_off);
        holder.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(tmpItem);
                Snackbar.make(v, R.string.item_removed_cart, Snackbar.LENGTH_SHORT).show();
            }
        });

        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context,
                    android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public int addItem(Item item) {
        if(itemList.contains(item)) return 0;
        else {
            itemList.add(item);
            notifyDataSetChanged();
            return 1;
        }
    }

    public void removeItem(Item item) {
        int position=itemList.indexOf(item);
        itemList.remove(item);
        notifyItemRemoved(position);
    }
    public void removeAll() {
        int size=itemList.size();
        itemList=new ArrayList<>();
        notifyItemRangeRemoved(0,size);
    }

    public int getCost(){
        int cost=0;
        for (Item item:itemList) {
            cost+=item.getPrice();
        }
        return cost;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

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

}

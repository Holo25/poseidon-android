package com.wolf.wise.holo.poseidon.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wolf.wise.holo.poseidon.R;
import com.wolf.wise.holo.poseidon.adapter.ProfileAdapter;
import com.wolf.wise.holo.poseidon.data.Item;


public class ProfileFragment extends Fragment {


    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private OnUserFragmentInteractionListener mListener;
    private ProfileAdapter profileAdapter;



    private String username;
    private int balance;

    public ProfileFragment() {
    }


    @SuppressWarnings("unused")
    public static ProfileFragment newInstance(int columnCount) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_list, container, false);

        TextView tvUsername=view.findViewById(R.id.tvProfileUsername);
        TextView tvBalance=view.findViewById(R.id.tvProfileBalance);
        tvUsername.setText(username);
        tvBalance.setText(getString(R.string.nav_header_subtitle,balance));

        View recView=view.findViewById(R.id.rlist);
        // Set the adapter
        if (recView instanceof RecyclerView) {
            Context context = recView.getContext();
            RecyclerView recyclerView = (RecyclerView) recView;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            if(profileAdapter !=null)
                recyclerView.setAdapter(profileAdapter);
            else recyclerView.setAdapter(new ProfileAdapter(context,mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserFragmentInteractionListener) {
            mListener = (OnUserFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setProfileAdapter(ProfileAdapter adapter){
        profileAdapter =adapter;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnUserFragmentInteractionListener {
        void onUserFragmentInteraction(Item item);
    }
}

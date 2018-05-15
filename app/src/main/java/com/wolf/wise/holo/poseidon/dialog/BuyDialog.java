package com.wolf.wise.holo.poseidon.dialog;




import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.TextView;

import com.wolf.wise.holo.poseidon.R;

public class BuyDialog extends AppCompatDialogFragment {

    public interface BuyDialogListener {
        void onDialogPositiveClick();
        void onDialogNegativeClick();
    }
    private static final String ARG_COST = "costArg";
    private static final String ARG_BALANCE = "balanceArg";

    private BuyDialogListener listener;
    private int cost;
    private int balance;

    public static BuyDialog newInstace(int cost, int balance){
        BuyDialog dialog=new BuyDialog();
        Bundle args=new Bundle();
        args.putInt(ARG_COST,cost);
        args.putInt(ARG_BALANCE,balance);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            cost = getArguments().getInt(ARG_COST);
            balance = getArguments().getInt(ARG_BALANCE);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Buy items?").setPositiveButton("Buy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDialogPositiveClick();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDialogNegativeClick();
            }
        });
        View view=getActivity().getLayoutInflater().inflate(R.layout.dialog_buy,null);
        TextView tvCost=view.findViewById(R.id.tvdCost);
        TextView tvBalance=view.findViewById(R.id.tvdBalance);
        TextView tvNewBalance=view.findViewById(R.id.tvdNewBalance);
        TextView tvNewBalanceCredit=view.findViewById(R.id.tvdNewBalanceCredit);

        tvCost.setText(getString(R.string.credit_value,cost));
        tvBalance.setText(getString(R.string.credit_value,balance));
        tvNewBalanceCredit.setText(getString(R.string.credit_value,balance-cost));

        if(cost>balance){
            tvNewBalance.setTextColor(getResources().getColor(R.color.red));
            tvNewBalanceCredit.setTextColor(getResources().getColor(R.color.red));
        }

        builder.setView(view);
        AlertDialog dialog=builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener(){

            @Override
            public void onShow(DialogInterface dialog) {
                if(cost>balance)
                    ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
        });
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BuyDialogListener) {
            listener = (BuyDialogListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement BuyDialogListener");
        }
    }

}

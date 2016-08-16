package com.example.srikar.magic.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.example.srikar.magic.R;

/**
 * Fragment for dialog that confirms attackers or blockers.
 * Created by Srikar on 8/12/2016.
 */
public class CombatDialogFragment extends AppCompatDialogFragment {
    DialogInterface.OnClickListener onPositive, onNeutral, onNegative;

    public void setListeners(DialogInterface.OnClickListener onPositive, DialogInterface.OnClickListener onNeutral,
                                DialogInterface.OnClickListener onNegative) {
        this.onPositive = onPositive;
        this.onNeutral = onNeutral;
        this.onNegative = onNegative;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //create default dialog box
        builder.setMessage(R.string.dialog_attackers)
                .setPositiveButton(R.string.dialog_positive, onPositive)
                .setNeutralButton(R.string.dialog_neutral, onNeutral)
                .setNegativeButton(R.string.dialog_negative, onNegative);

        return builder.create();
    }
}

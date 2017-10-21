package com.example.darwin.umnify.feed;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.darwin.umnify.R;

/**
 * Created by darwin on 9/23/17.
 */

public class DeleteConfirmationDialog extends DialogFragment {

    private TextView messageView;
    private int pos;
    private String message;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_confirmation, null);
        messageView = (TextView) layout.findViewById(R.id.confirmation_message);

        Bundle bundle = getArguments();

        if (bundle != null){

            message = bundle.getString("MESSAGE");
            pos = bundle.getInt("POSITION");

            messageView.setText(message);
        }

        builder.setView(layout)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DeleteConfirmationDialog.this.getDialog().cancel();
            }
        }).setPositiveButton("Delete", null);

        return builder.create();
    }
}
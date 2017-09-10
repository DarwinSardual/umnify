package com.example.darwin.umnify.calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.darwin.umnify.R;

/**
 * Created by darwin on 9/9/17.
 */

public class ViewEventDialog extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_view_event, null);

        builder.setView(layout)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ViewEventDialog.this.getDialog().cancel();
                    }
                });

        Bundle bundle = getArguments();

        if(bundle != null){

            LinearLayout container = (LinearLayout) layout.findViewById(R.id.view_event_container);

            for(String key : bundle.keySet()){

                TextView idView = new TextView(getActivity());
                TextView nameView = new TextView(getActivity());
                TextView descriptionView = new TextView(getActivity());
                TextView startDateView = new TextView(getActivity());
                TextView endDateView = new TextView(getActivity());

                idView.setText(bundle.getBundle(key).getInt("id")+ "");
                nameView.setText(bundle.getBundle(key).getString("name"));
                nameView.setTypeface(null, Typeface.BOLD);
                descriptionView.setText(bundle.getBundle(key).getString("description"));
                startDateView.setText(bundle.getBundle(key).getString("start_date"));
                endDateView.setText(bundle.getBundle(key).getString("end_date"));


                container.addView(nameView);
                container.addView(descriptionView);
                container.addView(startDateView);
                container.addView(endDateView);

            }

        }



        return builder.create();
    }

}

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

import com.example.darwin.umnify.DateHelper;
import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.calendar.data_action_wrapper.DeleteEventDataActionWrapper;
import com.example.darwin.umnify.wrapper.WebServiceAction;

import java.util.HashMap;

/**
 * Created by darwin on 9/9/17.
 */

public class ViewEventDialogAdmin extends DialogFragment implements OnDeleteEvent{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_view_event, null);

        builder.setView(layout)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ViewEventDialogAdmin.this.getDialog().cancel();
                    }
                });

        final Bundle bundle = getArguments();

        if(bundle != null){

            final LinearLayout container = (LinearLayout) layout.findViewById(R.id.view_event_container);

            for(final String key : bundle.keySet()){

                final LinearLayout eventContainer = new LinearLayout(getActivity());
                LinearLayout.LayoutParams eventContainerParams = new
                        LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                eventContainer.setLayoutParams(eventContainerParams);
                eventContainer.setOrientation(LinearLayout.VERTICAL);

                TextView idView = new TextView(getActivity());
                TextView nameView = new TextView(getActivity());
                TextView descriptionView = new TextView(getActivity());
                TextView dateView = new TextView(getActivity());

                idView.setText(bundle.getBundle(key).getInt("id")+ "");
                nameView.setText(bundle.getBundle(key).getString("name"));
                nameView.setTypeface(null, Typeface.BOLD);
                descriptionView.setText(bundle.getBundle(key).getString("description"));

                String startDate = bundle.getBundle(key).getString("start_date");
                String endDate = bundle.getBundle(key).getString("end_date");
                String dateDisplay = null;

                if(startDate.equalsIgnoreCase(endDate)){
                    dateDisplay = DateHelper.convertDateToMDY(startDate);
                }else{
                    dateDisplay = DateHelper.convertDateToMDY(startDate) + " - " + DateHelper.convertDateToMDY(endDate);
                }

                dateView.setText(dateDisplay);

                nameView.setTextSize(16);

                eventContainer.addView(nameView);
                eventContainer.addView(descriptionView);
                eventContainer.addView(dateView);

                LinearLayout buttonLayout = new LinearLayout(getActivity());
                LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                buttonLayout.setOrientation(LinearLayout.HORIZONTAL);

                Button editButton = new Button(getActivity());
                editButton.setTextSize(10);
                editButton.setText("Edit");

                Button deleteButton = new Button(getActivity());
                deleteButton.setTextSize(10);
                deleteButton.setText("Delete");
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        HashMap<String, String> textDataOutput = new HashMap<String, String>();
                        textDataOutput.put("id", bundle.getBundle(key).getInt("id") + "");

                        WebServiceAsync async = new WebServiceAsync();
                        DeleteEventDataActionWrapper wrapper =
                                new DeleteEventDataActionWrapper(textDataOutput,
                                        ViewEventDialogAdmin.this.getActivity(), eventContainer, container,  ViewEventDialogAdmin.this);

                        async.execute(wrapper);
                    }
                });

                //buttonLayout.addView(editButton);
                buttonLayout.addView(deleteButton);

                eventContainer.addView(buttonLayout);

                container.addView(eventContainer);
            }

        }

        return builder.create();
    }

    @Override
    public void deleteEvent(ViewGroup parent, ViewGroup container) {
        parent.removeView(container);

    }
}

package com.example.darwin.umnify.personal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by darwin on 9/4/17.
 */

public class SubjectsEnrolledDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        if(args != null){

            builder.setTitle("Details");

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Code: " + args.getInt("code"));
            stringBuilder.append("\n");
            stringBuilder.append("Name: " + args.getString("name"));
            stringBuilder.append("\n");
            stringBuilder.append("Description: " + args.getString("description"));
            stringBuilder.append("\n");
            stringBuilder.append("Start time: " + args.getString("time_from"));
            stringBuilder.append("\n");
            stringBuilder.append("End time: " + args.getString("time_to"));
            stringBuilder.append("\n");
            stringBuilder.append("Room: " + args.getString("room"));
            stringBuilder.append("\n");
            stringBuilder.append("Term: " + args.getString("term"));
            stringBuilder.append("\n");
            stringBuilder.append("Day Schedule: " + args.getString("day_schedule"));
            stringBuilder.append("\n");
            stringBuilder.append("Units: " + args.getString("units"));

            builder.setMessage(stringBuilder.toString());

        }

        return builder.create();
    }
}

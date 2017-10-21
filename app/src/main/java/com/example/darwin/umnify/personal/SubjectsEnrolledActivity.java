package com.example.darwin.umnify.personal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.personal.data_action_wrapper.FetchSubjectsEnrolledDataActionWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SubjectsEnrolledActivity extends AppCompatActivity {

    private Bundle userData;
    private TableLayout table;
    private ImageButton toolbarBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects_enrolled);

        userData = getIntent().getExtras();
        table = (TableLayout) findViewById(R.id.subjects_table);

        toolbarBackButton = (ImageButton) findViewById(R.id.back);

        toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        HashMap<String, String> textDataOutput = new HashMap<>();
        textDataOutput.put("id", userData.getInt("USER_ID") + "");

        WebServiceAsync async = new WebServiceAsync();
        FetchSubjectsEnrolledDataActionWrapper fetchSubjectsEnrolledDataActionWrapper =
                new FetchSubjectsEnrolledDataActionWrapper(textDataOutput, this);

        async.execute(fetchSubjectsEnrolledDataActionWrapper);
    }

    public void displayData(String jsonDataArray) throws JSONException{

        JSONArray jsonArray = new JSONArray(jsonDataArray);
        TableRow row;
        TextView codeView;
        TextView nameView;
        TextView descriptionView;
        TextView semTermView;
        TextView timeView;
        TextView dayView;
        Button moreButton;

        TableRow.LayoutParams params1 = new TableRow.LayoutParams(1);
        TableRow.LayoutParams params2 = new TableRow.LayoutParams(2);
        TableRow.LayoutParams params3 = new TableRow.LayoutParams(3);
        TableRow.LayoutParams params4 = new TableRow.LayoutParams(4);
        TableRow.LayoutParams params5 = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params5.column = 4;

        TableRow.LayoutParams params6 = new TableRow.LayoutParams(6);

        for(int counter = 0; counter < jsonArray.length(); counter++){

            String str = jsonArray.getString(counter);
            JSONObject data = new JSONObject(str);
            Bundle bundle = new Bundle();

            codeView = new TextView(this);
            codeView.setLayoutParams(params1);
            codeView.setTextSize(12);
            codeView.setText(data.getInt("id") + "");
            bundle.putInt("code", data.getInt("id"));

            nameView = new TextView(this);
            nameView.setLayoutParams(params2);
            nameView.setTextSize(12);
            nameView.setText(data.getString("name"));
            bundle.putString("name", data.getString("name"));

            descriptionView = new TextView(this);
            descriptionView.setLayoutParams(params3);
            descriptionView.setTextSize(12);
            descriptionView.setText(data.getString("description"));
            bundle.putString("description", data.getString("description"));

            bundle.putString("time_from",data.getString("time_from"));
            bundle.putString("time_to",data.getString("time_to"));
            bundle.putString("term", data.getString("term").equalsIgnoreCase("null") ? "sem" : data.getString("term"));
            bundle.putString("room", data.getString("room"));
            bundle.putString("day_schedule",data.getString("day_schedule"));
            bundle.putString("units",data.getString("units"));


            /* semTermView = new TextView(this);
            semTermView.setLayoutParams(params4);
            semTermView.setTextSize(14);
            String semTerm = data.getString("term") == null? "sem" : data.getString("term");
            semTermView.setText(semTerm); */

            moreButton = new Button(this);
            moreButton.setText("More");
            moreButton.setAllCaps(false);
            moreButton.setTextSize(10);
            moreButton.setMinHeight(0);
            moreButton.setMinWidth(0);
            //moreButton.setBackground(null);
            moreButton.setLayoutParams(params5);
            moreButton.setOnClickListener(new MoreClickListener(bundle));

            /*timeView = new TextView(this);
            timeView.setLayoutParams(params5);

            dayView = new TextView(this);
            dayView.setLayoutParams(params6);
            dayView.setTextSize(12);
            dayView.setText(data.getString("day_schedule"));*/

            row = new TableRow(this);

            row.addView(codeView);
            row.addView(nameView);
            row.addView(descriptionView);
            //row.addView(semTermView);
            row.addView(moreButton);

            table.addView(row);

        }
    }

    private class MoreClickListener implements View.OnClickListener{

        private Bundle data;

        private MoreClickListener(Bundle data){

            this.data = data;
        }

        @Override
        public void onClick(View view) {

            SubjectsEnrolledDialogFragment dialog = new SubjectsEnrolledDialogFragment();
            dialog.setArguments(data);
            dialog.show(getFragmentManager(), "Subjects Enrolled");
        }
    }
}

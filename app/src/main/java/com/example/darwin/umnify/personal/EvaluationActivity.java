package com.example.darwin.umnify.personal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.personal.data_action_wrapper.FetchEvaluationDataActionWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EvaluationActivity extends AppCompatActivity {

    private Bundle userData;
    private TableLayout table;
    private ImageButton toolbarBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        userData = getIntent().getExtras();
        table = (TableLayout) findViewById(R.id.evaluation_table);

        toolbarBackButton = (ImageButton) findViewById(R.id.back);

        toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        HashMap<String, String> textDataOutput = new HashMap<>();
        textDataOutput.put("id", userData.getInt("USER_ID") + "");
        textDataOutput.put("course", userData.getInt("USER_COURSE") + "");


        WebServiceAsync async = new WebServiceAsync();
        FetchEvaluationDataActionWrapper fetchEvaluationDataActionWrapper = new FetchEvaluationDataActionWrapper(textDataOutput,
                this);

        async.execute(fetchEvaluationDataActionWrapper);
    }

    public void displayData(String jsonDataArray) throws JSONException{

        JSONArray jsonArray = new JSONArray(jsonDataArray);
        TableRow row;

        TextView nameView;
        TextView descriptionView;
        TextView unitsView;
        TextView prerequisiteView;

        int sem = 0;
        String semesterHeading = "";
        int totaUnits;

        TableRow.LayoutParams params1 = new TableRow.LayoutParams(1);
        TableRow.LayoutParams params2 = new TableRow.LayoutParams(2);
        TableRow.LayoutParams params3 = new TableRow.LayoutParams(3);
        TableRow.LayoutParams params4 = new TableRow.LayoutParams(4);

        for(int counter = 0; counter < jsonArray.length(); counter++){

            String str = jsonArray.getString(counter);
            JSONObject data = new JSONObject(str);

            int tempSem = data.getInt("semester");

            if(sem != tempSem){

                sem = data.getInt("semester");

            }

            nameView = new TextView(this);
            nameView.setLayoutParams(params1);
            descriptionView = new TextView(this);
            descriptionView.setLayoutParams(params2);
            unitsView = new TextView(this);
            unitsView.setLayoutParams(params3);
            prerequisiteView = new TextView(this);
            prerequisiteView.setLayoutParams(params4);

            nameView.setText(data.getString("name"));
            descriptionView.setText(data.getString("description"));
            unitsView.setText(data.getString("units"));
            String prerequisite = data.isNull("prerequisite")? "" : data.getString("prerequisite");

            prerequisiteView.setText(prerequisite);

            row = new TableRow(this);

            row.addView(nameView);
            row.addView(descriptionView);
            row.addView(unitsView);
            row.addView(prerequisiteView);

            table.addView(row);
        }

        toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

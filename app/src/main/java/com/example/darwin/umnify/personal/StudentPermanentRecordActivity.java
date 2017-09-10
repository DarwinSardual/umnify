package com.example.darwin.umnify.personal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.darwin.umnify.R;
import com.example.darwin.umnify.async.WebServiceAsync;
import com.example.darwin.umnify.personal.data_action_wrapper.FetchStudentPermanentRecordDataActionWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class StudentPermanentRecordActivity extends AppCompatActivity{

    private Bundle userData;
    private TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_permanent_record);

        userData = getIntent().getExtras();
        table = (TableLayout) findViewById(R.id.student_permanent_record_table);

        HashMap<String, String> textDataOutput = new HashMap<>();
        textDataOutput.put("id", userData.getInt("USER_ID") + "");

        WebServiceAsync async = new WebServiceAsync();
        FetchStudentPermanentRecordDataActionWrapper fetchStudentPermanentRecordDataActionWrapper =
               new FetchStudentPermanentRecordDataActionWrapper(textDataOutput, this);

        async.execute(fetchStudentPermanentRecordDataActionWrapper);
    }

    public void displayData(String jsonData) throws JSONException{
       Log.e("Fetch", "SPR - " + jsonData);

        JSONArray jsonArray = new JSONArray(jsonData);
        TableRow row;
        TableRow semesterRow;
        TextView semesterView;
        TextView nameView;
        TextView descriptionView;
        TextView gradeView;
        int sem = 0;
        String semesterHeading = "";

        TableRow.LayoutParams params1 = new TableRow.LayoutParams(1);
        TableRow.LayoutParams params2 = new TableRow.LayoutParams(2);
        TableRow.LayoutParams params3 = new TableRow.LayoutParams(3);

        TableRow.LayoutParams semesterParams = new TableRow.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        for(int counter = 0; counter < jsonArray.length(); counter++){

            String str = jsonArray.getString(counter);
            JSONObject data = new JSONObject(str);

            int tempSem = data.getInt("semester");
            String tempDate = data.getString("date_from");

            if(sem != tempSem){
                sem = data.getInt("semester");
                String syFrom = tempDate.substring(0,4);
                String syTo = Integer.parseInt(syFrom) + 1 + "";


                if(sem == 1)
                    semesterHeading = "1st semester " + syFrom + "-" + syTo;
                else if(sem == 2)
                    semesterHeading = "2nd semester " + syFrom + "-" + syTo;
                else if(sem ==3 )
                    semesterHeading = "Summer " + syFrom + "-" + syTo;

                semesterParams.span = 3;
                semesterParams.topMargin = 10;

                semesterRow = new TableRow(this);
                semesterView = new TextView(this);
                semesterView.setLayoutParams(semesterParams);
                semesterView.setText(semesterHeading);

                semesterRow.addView(semesterView);

                table.addView(semesterRow);

            }

            nameView = new TextView(this);
            nameView.setLayoutParams(params1);
            descriptionView = new TextView(this);
            descriptionView.setLayoutParams(params2);
            gradeView = new TextView(this);
            gradeView.setLayoutParams(params3);

            nameView.setText(data.getString("name"));
            descriptionView.setText(data.getString("description"));
            gradeView.setText(data.getString("grade") + "");

            row = new TableRow(this);

            row.addView(nameView);
            row.addView(descriptionView);
            row.addView(gradeView);

            table.addView(row);

        }


    }
}

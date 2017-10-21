package com.example.darwin.umnify.calendar;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.darwin.umnify.R;

public class AddEventActivity extends AppCompatActivity{

    private TextView startTextView;
    private TextView endTextView;

    private Button startDateButton;
    private Button endDateButton;

    private ImageButton submitButton;
    private ImageButton backButton;

    private EditText nameView;
    private EditText descriptionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        submitButton = (ImageButton) findViewById(R.id.submit_button);
        backButton = (ImageButton) findViewById(R.id.back_button);

        startDateButton = (Button) findViewById(R.id.start);
        startTextView = (TextView) findViewById(R.id.start_text);

        endDateButton = (Button) findViewById(R.id.end);
        endTextView = (TextView) findViewById(R.id.end_text);

        startDateButton.setOnClickListener(new DatePickListener(R.id.start_text, "Start date"));
        endDateButton.setOnClickListener(new DatePickListener(R.id.end_text, "End date"));

        nameView = (EditText) findViewById(R.id.name_field);
        descriptionView = (EditText) findViewById(R.id.description_field);

        ActionHandler actionHandler = new ActionHandler();
        submitButton.setOnClickListener(actionHandler);
        backButton.setOnClickListener(actionHandler);


    }

    private class DatePickListener implements View.OnClickListener{

        private int viewRef;
        private String tag;

        public DatePickListener(int viewRef,String tag){
            this.viewRef = viewRef;
            this.tag = tag;

        }

        @Override
        public void onClick(View view) {

            DialogFragment dateFragment = new DatePickerFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("view", viewRef);
            dateFragment.setArguments(bundle);
            dateFragment.show(getFragmentManager(), tag);
        }
    }

    private class ActionHandler implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            if(view == submitButton){
                //check if title is empty
                //check if end date is greater than start date

                String startDate = startTextView.getText().toString();
                String endDate = endTextView.getText().toString();
                String name = nameView.getText().toString();
                String description = descriptionView.getText().toString();

                Intent data = new Intent();
                data.putExtra("name", name);
                data.putExtra("description", description);
                data.putExtra("start_date", startDate);
                data.putExtra("end_date", endDate);

                setResult(RESULT_OK, data);
                AddEventActivity.this.finish();

            }else if(view == backButton){

            }

        }
    }

}

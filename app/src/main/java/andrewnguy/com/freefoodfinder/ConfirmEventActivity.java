package andrewnguy.com.freefoodfinder;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class ConfirmEventActivity extends Activity implements View.OnClickListener {

    private Button confirm, cancel;
    private EditText title, eventDesc, locDesc, start, end, date;
    private String titleStr, eventDescStr, locDescStr, dateStr, startStr, endStr;
    private EventArray ea; // reference the main EventArray


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_event_activity);


        //Stops keyboard from popping up all the time on activity create
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        ea = MainActivity.ea;


        confirm = (Button) findViewById(R.id.confirm_event_activity_confirm_button); // confirm post button
        cancel = (Button) findViewById(R.id.confirm_event_activity_cancel_button);   // cancel post button

        title = (EditText) findViewById(R.id.confirm_event_activity_title);

        eventDesc = (EditText) findViewById(R.id.confirm_event_activity_event_description);



        locDesc = (EditText) findViewById(R.id.confirm_event_activity_location_description);

        start = (EditText) findViewById(R.id.event_date_time_startTime); // start time
        start.setOnClickListener(this);

        end = (EditText) findViewById(R.id.event_date_time_endTime); // end time
        end.setOnClickListener(this);

        date = (EditText) findViewById(R.id.event_date_time_date);
        date.setOnClickListener(this);

        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Intent returnIntent = new Intent(); // the return intent
        if (v.getId() == R.id.confirm_event_activity_cancel_button) { // cancel the post
            setResult(Activity.RESULT_CANCELED, returnIntent); // return 0
            finish();
        }
        else if(v.getId() == R.id.event_date_time_date){
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getFragmentManager(), "datePicker");

        }
        else if(v.getId() == R.id.event_date_time_startTime){

            Bundle b = new Bundle();
            b.putString("startTime", "start");
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.setArguments(b);
            newFragment.show(getFragmentManager(), "timePicker");
        }
        else if(v.getId() == R.id.event_date_time_endTime){

            Bundle b = new Bundle();
            b.putString("endTime", "end");
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.setArguments(b);
            newFragment.show(getFragmentManager(), "timePicker");
        }
        else { // finish the add

            /** ----- TODO:: FIELD/EVENT VARIABLE CHECKS ----- **/

            StringBuffer msg = new StringBuffer("Please fill out these fields:");
            boolean emptyFields = false;
            if (isEmpty(title)) {
                msg.append(" Title");
                emptyFields = true;
            }
            if (isEmpty(locDesc)) {
                if (emptyFields) {
                    msg.append(",");
                }
                msg.append(" Location Description");
                emptyFields = true;
            }
            if (isEmpty(date)) {
                if (emptyFields) {
                    msg.append(",");
                }
                msg.append(" Date of Event");
                emptyFields = true;
            }
            if (isEmpty(start)) {
                if (emptyFields) {
                    msg.append(",");
                }
                msg.append(" Start Time");
                emptyFields = true;
            }
            if (isEmpty(end)) {
                if (emptyFields) {
                    msg.append(",");
                }
                msg.append(" End Time");
                emptyFields = true;
            }
            if (emptyFields) {
                msg.append(".");
                Toast.makeText(this, msg.toString(), Toast.LENGTH_LONG).show();
                return;
            }

            if (isEmpty(eventDesc)) {
                // maybe warn them? it's okay to have empty event description
            }


            titleStr = title.getText().toString();
            eventDescStr = eventDesc.getText().toString();
            locDescStr = locDesc.getText().toString();
            dateStr = date.getText().toString();
            startStr = start.getText().toString();
            endStr = end.getText().toString();

            // create some dummmy coords
            double lat = 0, lng = 0;
            double currLat = 0, currLng = 0;

            // try to get the coords
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                try {
                    lat = extras.getDouble("latitude");
                    lng = extras.getDouble("longitude");
                    currLat = extras.getDouble("currLat");
                    currLng = extras.getDouble("currLng");
                } catch (NullPointerException npe) { // if no good, set all to 0
                    Log.d("mylocation", npe.getMessage());
                    lat = 0;
                    lng = 0;
                    currLat = 0;
                    currLng = 0;
                }
            }
            LatLng eventLoc = new LatLng(lat, lng);
            LatLng currentLoc = new LatLng(currLat, currLng);

            // add the new event
            ea.add(new Event(titleStr, dateStr, startStr, endStr,
                             eventDescStr, eventLoc, currentLoc));

            setResult(Activity.RESULT_OK, returnIntent); //return 1
            finish();
        }
    }

    private boolean isEmpty(EditText text) {
        if (text.getText().toString().trim().length() > 0)
            return false;
        else
            return true;
    }
}

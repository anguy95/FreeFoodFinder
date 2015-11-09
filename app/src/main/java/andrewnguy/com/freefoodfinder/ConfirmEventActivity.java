package andrewnguy.com.freefoodfinder;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        setContentView(R.layout.activity_confirm_event);

        ea = MainActivity.ea;


        confirm = (Button) findViewById(R.id.buttonConfirmPost); // confirm post button
        cancel = (Button) findViewById(R.id.buttonCancelPost);   // cancel post button

        title = (EditText) findViewById(R.id.editEventTitle);
        eventDesc = (EditText) findViewById(R.id.editEventDescription);
        locDesc = (EditText) findViewById(R.id.editLocationDescription);

        start = (EditText) findViewById(R.id.editEventStartTime); // start time
        start.setOnClickListener(this);

        end = (EditText) findViewById(R.id.editEventEndTime); // end time
        end.setOnClickListener(this);

        date = (EditText) findViewById(R.id.editEventDate);
        date.setOnClickListener(this);

        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Intent returnIntent = new Intent(); // the return intent
        if (v.getId() == R.id.buttonCancelPost) { // cancel the post
            setResult(Activity.RESULT_CANCELED, returnIntent); // return 0
            finish();
        }
        else if(v.getId() == R.id.editEventDate){
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getFragmentManager(), "datePicker");

        }
        else if(v.getId() == R.id.editEventStartTime){

            Bundle b = new Bundle();
            b.putString("startTime", "start");
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.setArguments(b);
            newFragment.show(getFragmentManager(), "timePicker");
        }
        else if(v.getId() == R.id.editEventEndTime){

            Bundle b = new Bundle();
            b.putString("endTime", "end");
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.setArguments(b);
            newFragment.show(getFragmentManager(), "timePicker");
        }
        else { // finish the add
            StringBuffer msg = new StringBuffer("Please enter a value for:");
            boolean emptyFields = false;
            boolean emptyDesc = true;
            if (isEmpty(title)) {
                msg.append(" Title");
                emptyFields = true;
            }
            if (isEmpty(eventDesc)) {
                if (emptyFields)
                    msg.append(",");
                msg.append(" Event Description");
                emptyDesc = false;
            }
            if (isEmpty(locDesc)) {
                if (emptyFields)
                    msg.append(",");
                msg.append(" Location Description");
                emptyFields = true;
            }
            if (isEmpty(start)) {
                if (emptyFields)
                    msg.append(",");
                msg.append(" Start Time");
                emptyFields = true;
            }
            if (isEmpty(end)) {
                if (emptyFields)
                    msg.append(",");
                msg.append(" End Time");
                emptyFields = true;
            }

            /*
            if (emptyFields) {
                msg.append(".");
                Toast.makeText(this, msg.toString(), Toast.LENGTH_LONG).show();
                return;
            }
            else if (!emptyDesc) {
                // maybe ask if they want to have an empty description field
            }
            */

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
                } catch (NullPointerException npe) {
                    Log.d("mylocation", npe.getMessage());
                    lat = 0;
                    lng = 0;
                    currLat = 0;
                    currLng = 0;
                }
            }

            LatLng eventLoc = new LatLng(lat, lng);
            LatLng currentLoc = new LatLng(currLat, currLng);

            Event newEvent = new Event(titleStr, dateStr, (startStr + " - " + endStr),
                                       eventDescStr, eventLoc, currentLoc);
            ea.add(newEvent);

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

    private Date setDate(int year, int month, int day, int hour, int minute)
    {
        /*  year - the year plus 1900.                  *
         *  month - the month between 0-11.             *
         *  date - the day of the month between 1-31.   *
         *  hrs - the hours between 0-23.               *
         *  min - the minutes between 0-59              */

        // need to do conversions
        int Y = year - 1900;
        // month in CEA will be be from 0-11
        // everything else is normal

        return new Date(Y, month, day, hour, minute);
    }

}

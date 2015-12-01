package andrewnguy.com.freefoodfinder;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class ConfirmEventActivity extends Activity implements View.OnClickListener {

    private EditText title, desc, tags, start, end, date;
    private TextView loc;

    private EventArray ea; // reference the main EventArray

    // create some dummmy coords
    private double lat = 0, lng = 0;
    private double currLat = 0, currLng = 0;

    private int times; // event desc check


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_event_activity);

        times = 0;

        //Stops keyboard from popping up all the time on activity create
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        ea = MainActivity.ea;

        // edit texts
        title = (EditText) findViewById(R.id.confirm_event_activity_title); // event description
        desc = (EditText) findViewById(R.id.confirm_event_activity_event_description);  // event description
        loc = (EditText) findViewById(R.id.confirm_event_activity_location_description); // event location
        tags = (EditText) findViewById(R.id.confirm_event_activity_tags); // event tags

        start = (EditText) findViewById(R.id.event_date_time_startTime); // start time
        start.setOnClickListener(this);

        end = (EditText) findViewById(R.id.event_date_time_endTime); // end time
        end.setOnClickListener(this);

        date = (EditText) findViewById(R.id.event_date_time_date); // date of event
        date.setOnClickListener(this);

        // buttons
        Button confirm = (Button) findViewById(R.id.confirm_event_activity_confirm_button);
        confirm.setOnClickListener(this);

        Button cancel = (Button) findViewById(R.id.confirm_event_activity_cancel_button);
        cancel.setOnClickListener(this);

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


            getLoc();
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
            /*if (isEmpty(loc)) {
                if (emptyFields) {
                    msg.append(",");
                }
                msg.append(" Location Description");
                emptyFields = true;
            }*/
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
/*
            if (times == 0 && (isEmpty(desc) || isEmpty(tags))) {
                // maybe warn them? it's okay to have an empty event description/tags
                String optionalFields = isEmpty(desc) ? "event descriptions" : "event tags";
                Toast.makeText(this, "Are you sure you want to leave the " + optionalFields + " blank?", Toast.LENGTH_LONG).show();
                times++;
                return;
            }*/


            String titleStr = title.getText().toString();
            String descStr = desc.getText().toString();
            String dateStr = date.getText().toString();
            String startStr = start.getText().toString();
            String endStr = end.getText().toString();
            String locStr = loc.getText().toString();
            String tagsStr = tags.getText().toString();
            String evnAuth = MainActivity.currentUser.getUsername();
            int score = 0;


            LatLng eventLoc = new LatLng(lat, lng);
            LatLng currentLoc = new LatLng(currLat, currLng);

            // add the new event
            ea.add(new Event(titleStr, dateStr, startStr, endStr,
                             descStr, tagsStr, score, evnAuth, locStr, eventLoc, currentLoc));

            setResult(Activity.RESULT_OK, returnIntent); //return 1
            finish();
        }
    }

    private boolean isEmpty(EditText text) {
        return text.getText().toString().trim().length() <= 0;
    }

    /**
     * HTTP GET request UCSD maps
     * Needs lat/lng
     * Returns building names within a radius
     */
    private void getLoc() {

        // convert lat/lng to strings for maps API
        String param1 = "lat=" + Double.toString(lat);
        String param2 = "lng=" + Double.toString(lng);

        LocationSetter rl = new LocationSetter(loc);
        rl.execute(param1 + "&" + param2); //toss in lat/lng as params
    }

}

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

import java.util.Calendar;
import java.util.Date;

/**
 * Class that handles event creation
 * An instance is created when a user has decided to place a pin on the map
 */
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


        getLoc(); // get location of pin
    }

    /**
     * processes the important click events
     */
    @Override
    public void onClick(View v)
    {
        Intent returnIntent = new Intent(); // the return intent

        if (v.getId() == R.id.confirm_event_activity_cancel_button) { // cancel the post

            setResult(Activity.RESULT_CANCELED, returnIntent); // return 0
            finish();
        }
        else if(v.getId() == R.id.event_date_time_date) { // choose the event date

            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getFragmentManager(), "datePicker");
        }
        else if(v.getId() == R.id.event_date_time_startTime) { // choose the event start time

            Bundle b = new Bundle();
            b.putString("startTime", "start");
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.setArguments(b);
            newFragment.show(getFragmentManager(), "timePicker");
        }
        else if(v.getId() == R.id.event_date_time_endTime) { // choose the event end time

            Bundle b = new Bundle();
            b.putString("endTime", "end");
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.setArguments(b);
            newFragment.show(getFragmentManager(), "timePicker");
        }
        else { // finish the add (confirm the post)

            /** BLANK FIELD CHECKS **/
            StringBuffer msg = new StringBuffer("Please fill out these fields:");
            boolean emptyFields = false;
            if (isEmpty(title)) {
                msg.append(" Title");
                emptyFields = true;
            }
            if (isEmpty((EditText)loc)) {
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

            if (times == 0 && (isEmpty(desc) || isEmpty(tags))) {
                // maybe warn them? it's okay to have an empty event description/tags
                String optionalFields = isEmpty(tags) ? "event tags" : "event description";
                Toast.makeText(this, "Are you sure you want to leave the " + optionalFields + " blank?", Toast.LENGTH_LONG).show();
                times++;
                return;
            }

            String titleStr = title.getText().toString();
            String descStr = desc.getText().toString();
            String dateStr = date.getText().toString();
            String startStr = start.getText().toString();
            String endStr = end.getText().toString();
            String locStr = loc.getText().toString();
            String tagsStr = tags.getText().toString();
            String evnAuth = MainActivity.currentUser.getUsername();
            int score = 0;

            /** TIME CHECKS **/
            Date now = Calendar.getInstance().getTime();
            Date startDate = getTimeOf(dateStr, startStr);
            Date endDate = getTimeOf(dateStr, endStr);

            if (startDate.getTime() > endDate.getTime()) {
                Toast.makeText(this, "Your event cannot end before it starts.", Toast.LENGTH_LONG).show();
                return;
            }

            if (now.getTime() > endDate.getTime()) {
                Toast.makeText(this, "Your event cannot end before now.", Toast.LENGTH_LONG).show();
                return;
            }


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

    /**
     * helper function used to determine the time based on the strings provided (for the time checker)
     * @param date is in the format of [DayOfWeek], [Mon.] [Day] [Year]
     * @param time is in the format of [HH:MM] [AM/PM] -- no 0's in front of single digit hour
     * @return a rebuilt date object
     */
    private Date getTimeOf(String date, String time)
    {
        String[] dateSplit = date.split(" ");
        String[] timeSplit = time.split(":|\\s");

        int month, day, year, hour, minute;

        //check month
        switch (dateSplit[1]) {
            case "Jan": month = 0; break;
            case "Feb": month = 1; break;
            case "Mar": month = 2; break;
            case "Apr": month = 3; break;
            case "May": month = 4; break;
            case "Jun": month = 5; break;
            case "Jul": month = 6; break;
            case "Aug": month = 7; break;
            case "Sep": month = 8; break;
            case "Oct": month = 9; break;
            case "Nov": month = 10; break;
            default   : month = 11; break; // DEC
        }

        // set day; 1-31
        day = Integer.parseInt(dateSplit[2]);

        // set year; y-1900
        year = Integer.parseInt(dateSplit[3]) - 1900;

        // set hour; 0-23
        hour = Integer.parseInt(timeSplit[0]);

        // check PM
        if (timeSplit[2].equals("AM") && hour == 12)
            hour = 0;
        else if (timeSplit[2].equals("PM") && hour != 12)
            hour += 12;

        // set minute; 0-59
        minute = Integer.parseInt(timeSplit[1]);

        return new Date(year, month, day, hour, minute);
    }
}

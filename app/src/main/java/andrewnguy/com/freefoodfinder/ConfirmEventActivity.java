package andrewnguy.com.freefoodfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.text.ParseException;

public class ConfirmEventActivity extends Activity implements View.OnClickListener {

    Button confirm, cancel;
    EditText title, eventDesc, locDesc, date, start, end;
    String titleStr, eventDescStr, locDescStr, dateStr, startStr, endStr;

    // should move this to MainActivity so that MapTab and ListTab can use this too
    private static final String DB_NAME = "currentFreeFoodsDB";
    private static final String LAT_COL = "LocationLat";
    private static final String LNG_COL = "LocationLong";
    private static final String DESC_LOC_COL = "DescriptionLocation";
    private static final String DESC_EV_COL= "DescriptionEvent";
    private static final String LATLNGLOCATION = "Location";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_event);

        confirm = (Button) findViewById(R.id.buttonConfirmPost); // confirm post button
        cancel = (Button) findViewById(R.id.buttonCancelPost);   // cancel post button

        title = (EditText) findViewById(R.id.editEventTitle);
        eventDesc = (EditText) findViewById(R.id.editEventDescription);
        locDesc = (EditText) findViewById(R.id.editLocationDescription);
        date = (EditText) findViewById(R.id.editEventDate);
        start = (EditText) findViewById(R.id.editEventStartTime);
        end = (EditText) findViewById(R.id.editEventEndTime);

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
        else { // finish the add
            // have to check empty fields
            titleStr = title.getText().toString();
            eventDescStr = eventDesc.getText().toString();
            locDescStr = locDesc.getText().toString();
            dateStr = date.getText().toString();
            startStr = start.getText().toString();
            endStr = end.getText().toString();

            double lat = 0;
            double lng = 0;
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                lat = extras.getDouble("latitude");
                lng = extras.getDouble("longitude");
            }


            ParseObject newEvent = new ParseObject(DB_NAME);
            ParseGeoPoint latlngPoint = new ParseGeoPoint (lat,lng);
            try {
                newEvent.put(LAT_COL, lat);
                newEvent.put(LNG_COL, lng);
                newEvent.put(LATLNGLOCATION, latlngPoint);
                newEvent.put(DESC_LOC_COL, locDescStr);
                newEvent.put(DESC_EV_COL, eventDescStr);
                newEvent.put("test", 214213);
                newEvent.saveInBackground();

            } catch(Exception e) { }

            /*
            returnIntent.putExtra("title", titleStr);
            returnIntent.putExtra("eventDesc", titleStr);
            returnIntent.putExtra("locDesc", locDescStr);
            returnIntent.putExtra("date", dateStr);
            returnIntent.putExtra("start", startStr);
            returnIntent.putExtra("end", endStr);
            */

            setResult(Activity.RESULT_OK, returnIntent); //return 1
            finish();
        }
    }
}

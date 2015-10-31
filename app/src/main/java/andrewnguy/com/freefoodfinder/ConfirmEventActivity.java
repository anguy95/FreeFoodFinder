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
import java.util.ArrayList;

public class ConfirmEventActivity extends Activity implements View.OnClickListener {

    Button confirm, cancel;
    EditText title, eventDesc, locDesc, date, start, end;
    String titleStr, eventDescStr, locDescStr, dateStr, startStr, endStr;


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

            double lat = -1;
            double lng = -1;
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                lat = extras.getDouble("latitude");
                lng = extras.getDouble("longitude");
            }


            ParseObject newEvent = new ParseObject(getString(R.string.DB));
            ParseGeoPoint latlngPoint = new ParseGeoPoint (lat,lng);
            try {
                newEvent.put(getString(R.string.LAT), lat);
                newEvent.put(getString(R.string.LNG), lng);
                //newEvent.put(getString(R.string.LOC), latlngPoint); // we can build a latlng with just the latlng
                //newEvent.put(DESC_LOC_COL, locDescStr);
                newEvent.put(getString(R.string.DES), eventDescStr);
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

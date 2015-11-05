package andrewnguy.com.freefoodfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

public class ConfirmEventActivity extends Activity implements View.OnClickListener {

    private Button confirm, cancel;
    private EditText title, eventDesc, locDesc, date, start, end;
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
            StringBuffer msg = new StringBuffer("Please enter a value for:");
            boolean emptyFields = false;
            if (isEmpty(title)) {
                msg.append(" Title");
                emptyFields = true;
            }
            if (isEmpty(eventDesc)) {
                if (emptyFields)
                    msg.append(",");
                msg.append(" Event Description");
                emptyFields = true;
            }
            if (isEmpty(locDesc)) {
                if (emptyFields)
                    msg.append(",");
                msg.append(" Location Description");
                emptyFields = true;
            }
            if (isEmpty(date)) {
                if (emptyFields)
                    msg.append(",");
                msg.append(" Date of Event");
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

            if (emptyFields) {
                msg.append(".");
                Toast.makeText(this, msg.toString(), Toast.LENGTH_LONG).show();
                return;
            }

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

            Event newEvent;
            if (eventDescStr == null && eventDescStr.isEmpty()) {
                newEvent = new Event(titleStr, 2015, 11, 3, 3, 30, lat, lng);
            }
            else {
                newEvent = new Event(titleStr, 2015, 11, 3, 3, 30, lat, lng, eventDescStr);
            }

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




}

package andrewnguy.com.freefoodfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        titleStr = title.getText().toString();
        eventDescStr = eventDesc.getText().toString();
        locDescStr = locDesc.getText().toString();
        dateStr = date.getText().toString();
        startStr = start.getText().toString();
        endStr = end.getText().toString();

        Intent returnIntent = new Intent(); // the return intent
        returnIntent.putExtra("SENDER", this.getClass().getSimpleName()); // store sender of intent in here

        // still have to check for empty fields
        returnIntent.putExtra("title", titleStr);
        returnIntent.putExtra("eventDesc", titleStr);
        returnIntent.putExtra("locDesc", locDescStr);
        returnIntent.putExtra("date", dateStr);
        returnIntent.putExtra("start", startStr);
        returnIntent.putExtra("end", endStr);

        if (v.getId() == R.id.buttonCancelPost) { // cancel the post
            setResult(Activity.RESULT_CANCELED, returnIntent); // return 0
            finish();
        }
        else { // finish the add
            setResult(Activity.RESULT_OK, returnIntent); //return 1
            finish();
        }
    }
}

package andrewnguy.com.freefoodfinder;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Class that allows us the see an event in detail
 * This should be called from clicking on an event from the ListTab
 * or clicking on "more information" from the MapsTab
 */
public class EventViewActivity extends AppCompatActivity implements OnClickListener {
    private EditText commentToAdd;
    private TextView viewComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view_main);


        //Stops keyboard from popping up all the time on activity create
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.event_view_main_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        commentToAdd = (EditText) findViewById(R.id.event_view_comment_submit); // Edit text field for comment


        final Bundle extras = getIntent().getExtras();

        TextView viewTitle = (TextView) findViewById(R.id.event_view_title);
        viewTitle.setText(extras.getString("eventTitle"));
        TextView viewDesc = (TextView) findViewById(R.id.event_view_eventDesc);
        viewDesc.setText(extras.getString("eventDesc"));


        TextView viewStartTime = (TextView) findViewById(R.id.event_date_time_startTime);
        viewStartTime.setText(extras.getString("eventStartTime"));
        TextView viewEndTime = (TextView) findViewById(R.id.event_date_time_endTime);
        viewEndTime.setText(extras.getString("eventEndTime"));
        TextView viewDate = (TextView) findViewById(R.id.event_date_time_date);
        viewDate.setText(extras.getString("eventDate"));

        LinearLayout viewTagHolder = (LinearLayout) findViewById(R.id.event_view_tag_display);
        TextView viewTags = new TextView(this);
        viewTags.setText(extras.getString("eventTags"));
        viewTagHolder.addView(viewTags);

        String objid = extras.getString("eventId");
        viewComments = (TextView) findViewById(R.id.event_view_comment_box);

        ParseQuery commentsQuery = new ParseQuery("commentsDB");

        commentsQuery.whereEqualTo("eventObjectId", objid);
        commentsQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> temp, ParseException exception) {

                if (exception == null) {
                    int i = 0;
                    String commentToDisplay = "";
                    while (i < temp.size()) {
                        commentToDisplay += temp.get(i).getString("comment");
                        commentToDisplay += (char) '\n';

                        i++;
                    }
                    viewComments.setText(commentToDisplay);

                }
            }
        });





            /* For commenting */
        // buttons on click gets the eventId and comment to add and sends it to commentsDB class

        Button button = (Button) findViewById(R.id.confirm_comment_button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject eventComments = new ParseObject("commentsDB");
                String commentToParse = commentToAdd.getText().toString();
                String objid = extras.getString("eventId");

                Log.d("comment", objid);

                eventComments.put("eventObjectId", objid);
                eventComments.put("comment", commentToParse);
                Log.d("Comment", commentToParse);
                Toast.makeText(getApplicationContext(), "Your comment has been submitted", Toast.LENGTH_SHORT).show();

                //Immediately adds to show responsive for user combined with toast
                String toAddDispaly = (String) viewComments.getText();
                toAddDispaly = toAddDispaly + commentToParse + '\n';
                viewComments.setText(toAddDispaly);
                eventComments.saveInBackground();

                //Hide keyboard on submit
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

            }
        });


    }


    @Override
    public void onClick(View v) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}

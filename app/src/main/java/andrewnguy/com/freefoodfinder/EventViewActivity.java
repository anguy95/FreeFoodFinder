package andrewnguy.com.freefoodfinder;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.provider.Settings.Secure;
import android.widget.ToggleButton;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class that allows us the see an event in detail
 * This should be called from clicking on an event from the ListTab
 * or clicking on "more information" from the MapsTab
 */
public class EventViewActivity extends AppCompatActivity implements OnClickListener {
    private EditText commentToAdd;
    private TextView viewComments, viewScore;
    private int tempScore;
    private HashMap<String, Event> eventsMap;
    private Event temp;
    private String objid = "";
    private ParseObject eventScore;
    private String eventAuthor = "";

    private boolean hasLikedBefore = false;

    private ListView commentView;
    private CommentListAdapter commentAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        eventsMap = MainActivity.ea.getEventMap();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view_main);


        //Stops keyboard from popping up all the time on activity create
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        //Sets back button on toolbar
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.event_view_main_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        commentToAdd = (EditText) findViewById(R.id.event_view_comment_submit); // Edit text field for comment




        final Bundle extras = getIntent().getExtras();
        objid = extras.getString("eventId");




        temp = eventsMap.get(extras.getString("eventId"));
        eventAuthor = temp.getEventAuthor();
        TextView viewTitle = (TextView) findViewById(R.id.event_view_title);
        viewTitle.setText(temp.getTitle());
        TextView viewDesc = (TextView) findViewById(R.id.event_view_eventDesc);
        viewDesc.setText(temp.getDescription());


        TextView viewStartTime = (TextView) findViewById(R.id.event_date_time_startTime);
        viewStartTime.setText(temp.getStartTime());
        TextView viewEndTime = (TextView) findViewById(R.id.event_date_time_endTime);
        viewEndTime.setText(temp.getEndTime());
        TextView viewDate = (TextView) findViewById(R.id.event_date_time_date);
        viewDate.setText(temp.getDate());

        //TODO Add location description

        LinearLayout viewTagHolder = (LinearLayout) findViewById(R.id.event_view_tag_display);
        TextView viewTags = new TextView(this);
        viewTags.setText(temp.getTags());
        viewTagHolder.addView(viewTags);

        commentView = (ListView) findViewById(R.id.event_view_comment_box);



        viewScore = (TextView) findViewById(R.id.event_view_likes);

        viewScore.setText(String.valueOf(temp.getEventScore()));
        tempScore = temp.getEventScore();

        if(MainActivity.likedEventsList.contains(objid)){

            ((ToggleButton) findViewById(R.id.toggleButton)).setChecked(true);
            hasLikedBefore = true;

        }

        // Pulls the comments
        updateComments();


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
                eventComments.put("postId", MainActivity.currentUser.getUsername());

                Log.d("Comment", commentToParse);
                eventComments.saveInBackground();
                Toast.makeText(getApplicationContext(), "Your comment has been submitted", Toast.LENGTH_SHORT).show();

                //Immediately adds to show responsive for user combined with toast
                //Hide keyboard on submit
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                updateComments();

            }
        });

        Button heartButton = (Button) findViewById(R.id.toggleButton);
        heartButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery query = new ParseQuery("currentFreeFoodsDB");


                try {
                    eventScore = query.get(extras.getString("eventId"));

                    if(hasLikedBefore){
                        tempScore--;
                        viewScore.setText(String.valueOf((tempScore)));
                        eventScore.increment("Score", -1);
                        temp.setEventScore(tempScore);
                        ((ToggleButton) findViewById(R.id.toggleButton)).setChecked(false);
                        hasLikedBefore = false;
                        if(MainActivity.likedEventsListtoRemove.size() > 0){

                        }
                        else
                            MainActivity.likedEventsListtoRemove.add(objid);


                        //TODO CHANGE APPROPRIATLY YA DINFUS

                    }else {
                        tempScore++;
                        viewScore.setText(String.valueOf(tempScore));
                        eventScore.increment("Score", 1);
                        temp.setEventScore(tempScore);
                        ((ToggleButton) findViewById(R.id.toggleButton)).setChecked(true);
                        hasLikedBefore = true;
                        if(MainActivity.likedEventsListtoRemove.size() >0) MainActivity.likedEventsListtoRemove.remove(0);

                        if(!MainActivity.likedEventsList.contains(objid)) MainActivity.likedEventsList.add(objid);




                    }



                }catch(ParseException e){

                }

            }
        });


    }
    public void updateComments(){
        ParseQuery commentsQuery = new ParseQuery("commentsDB");

        commentsQuery.whereEqualTo("eventObjectId", objid);
        commentsQuery.orderByDescending("createdAt");
        commentsQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> temp, ParseException exception) {

                if (exception == null) {
                    commentAdapter = new CommentListAdapter(getApplicationContext(), R.layout.comment_row, (ArrayList<ParseObject>) temp, eventAuthor);
                    commentView.setNestedScrollingEnabled(true);

                    commentView.setAdapter(commentAdapter);



                }
            }
        });
    }


    @Override
    public void onClick(View v) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Should only save score when you exit

        if(MainActivity.likedEventsList.size() > 0) {
            Log.d("Values in array", MainActivity.likedEventsList.get(0));
            MainActivity.currentUser.addAllUnique("likedEvents", MainActivity.likedEventsList);



            MainActivity.currentUser.saveInBackground();
        }
        Log.d("Size toRemove", String.valueOf(MainActivity.likedEventsListtoRemove.size()));

        if(MainActivity.likedEventsListtoRemove.size() > 0){
            MainActivity.currentUser.removeAll("likedEvents", MainActivity.likedEventsListtoRemove);
            MainActivity.currentUser.saveInBackground();

            MainActivity.likedEventsList.remove(objid);

            //TODO REMOVE ALL PLS MAKE NEW ARRAY DINGUS
            MainActivity.likedEventsListtoRemove.remove(0);

        }
        Log.d("Size liked", String.valueOf(MainActivity.likedEventsList.size()));

        Log.d("Size toRemove", String.valueOf(MainActivity.likedEventsListtoRemove.size()));
        if(eventScore != null) {
            eventScore.saveInBackground();
        }
    }


}

package andrewnguy.com.freefoodfinder;

/**
 * Created by anguy95 on 10/27/15.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class ListTab extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener
{
    private static final int LIST_CREATE_EVENT = 11; // give ListTab a requestCode of 11 when trying to make an event

    private ArrayList<Event> events = new ArrayList<>();
    private FloatingActionButton fab;
    private int flag = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.list_tab, container, false);

        fab = (FloatingActionButton) v.findViewById(R.id.list_fab);
        fab.setOnClickListener(this);

        //Adds all the events into the events arrayList (this will need to be moved to database on add instead of here)
        // getEvents();

        ListView listView = (ListView) v.findViewById(R.id.listView);


        // Custom adapter that
        EventListAdapter eventAdapter = new EventListAdapter(getActivity().getApplicationContext(), R.layout.list_row_view, events);

        // Set The Adapter + item listenerA
        listView.setAdapter(eventAdapter);
        listView.setOnItemClickListener(this);


        return v;
    }


    //TODO THIS SHIT AINT WORKING STILL


    void getEvents() //fetch
    {
        ParseQuery<ParseObject> eventQuery = ParseQuery.getQuery(getString(R.string.DB));
        eventQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> locations,
                             ParseException e) {
                if (e == null) {

                    //Iterates through locations and sets up the events
                    for (ParseObject temp : locations) {
                        //Log.d("Parsing ", " Current ObjID:" + temp.getObjectId());

                        // Creates event, need to update as more params come through
                        //Event(String title, int year, int month, int day, int hour, int minute, double lat, double lng,
                        //String description)
                        Date date = temp.getDate("Date");
                        Event workingEvent = new Event(temp.getString("Title"), date.getYear(), date.getMonth(), date.getDay(), date.getHours(), date.getMinutes(), temp.getDouble("Latitude"), temp.getDouble("Longitude"), temp.getString("DescriptionEvent"));
                        Log.d("Parsing", temp.getObjectId());
                        //Adds to maps view arraylist
                        events.add(workingEvent);

                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }

            }
        });
    }

    /**
     * Check click events. Responds to:
     * fab button (create event)
     *
     * @param v View that triggered onClick
     */
    @Override
    public void onClick(View v)
    {
        int id = v.getId(); // get view id

        if (id == R.id.list_fab) { // if fab was pressed
            Intent intent = new Intent(getActivity().getApplicationContext(), ConfirmEventActivity.class);
            startActivityForResult(intent, LIST_CREATE_EVENT); //start up event creation
        }
    }

    /**
     * Respond to a list click event
     * Used to view an event in greater detail
     * @param parent the adapterview click originated from
     * @param view that it came from
     * @param position of where the item was located
     * @param id of the item that was clicked
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        // display detailed event view

        // send intent to the event view class
        Intent intent = new Intent(getActivity().getApplicationContext(), EventViewActivity.class);
        startActivity(intent); // I don't think we need a result

        /*  NEEDS:
            BACK BUTTON?
            event title
            date
            time
            location
            description
            tags
            upvote/downvote
            COMMENTS
         */
    }

    /**
     * Get a result from an activity
     * @param requestCode code of request:
     *                    LIST_CREATE_EVENT
     * @param resultCode Activity.RESULT_OK or Acitivty.RESULT_CANCEL
     * @param data intent that the result came with
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LIST_CREATE_EVENT)
        {
            if (resultCode == Activity.RESULT_OK) { // OK
                // manipulate the Intent data to get simple data
            }
            // else do nothing
        }
    }
}
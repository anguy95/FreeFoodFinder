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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ListTab extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener
{
    private static final int LIST_CREATE_EVENT = 11; // give ListTab a requestCode of 11 when trying to make an event

    private ArrayList<Event> events = new ArrayList<>();
    private FloatingActionButton fab;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.list_tab, container, false);

        fab = (FloatingActionButton) v.findViewById(R.id.list_fab);
        fab.setOnClickListener(this);

        //Adds all the events into the events arrayList (this will need to be moved to database on add instead of here)
        // getEvents();

        getEvents();
        ListView listView = (ListView) v.findViewById(R.id.listView);


        // Custom adapter that
        EventListAdapter eventAdapter = new EventListAdapter(getActivity().getApplicationContext(), R.layout.list_row_view, events);


        // Set The Adapter + item listenerA
        listView.setAdapter(eventAdapter);
        listView.setOnItemClickListener(this);

        return v;
    }

    void getEvents() //fetch
    {
        //We would like to have it so that these events are created when ConfirmEventActivity terminates
        Event one = new Event("h1","w1");
        Event two = new Event("h2","w2");
        Event three = new Event("h3","w3");


        events.add(one);
        events.add(two);
        events.add(three);
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

    /**
     * Respond to a list click event
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        // display detailed event view
        /*  NEEDS:
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
}
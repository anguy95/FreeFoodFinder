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
import android.widget.Toast;

import java.util.ArrayList;


public class ListTab extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final int LIST_CREATE_EVENT = 11; // give ListTab a requestCode of 11 when trying to make an event

    private ArrayList<Event> events = new ArrayList<>(); // initial 0
    private EventListAdapter eventAdapter;
    private ListView listView;
    private View v;
    private FloatingActionButton fab;
    private EventArray ea;
    private int flag = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.list_tab, container, false);
        listView = (ListView) v.findViewById(R.id.list_tab_listview);

        ea = MainActivity.ea;
        events = ea.getEventArray(); // get events array

        fab = (FloatingActionButton) v.findViewById(R.id.list_tab_fab);
        fab.setOnClickListener(this);

        update();

        listView.setOnItemClickListener(this);

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (listView != null)
            listView.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    /**
     * Check click events. Responds to:
     * fab button (create event)
     *
     * @param v View that triggered onClick
     */
    @Override
    public void onClick(View v) {
        int id = v.getId(); // get view id

        if (id == R.id.list_tab_fab) { // if fab was pressed
            Intent intent = new Intent(getActivity().getApplicationContext(), ConfirmEventActivity.class);
            startActivityForResult(intent, LIST_CREATE_EVENT); //start up event creation
        }
    }

    /**
     * Respond to a list click event
     * Used to view an event in greater detail
     *
     * @param parent   the adapterview click originated from
     * @param view     that it came from
     * @param position of where the item was located
     * @param id       of the item that was clicked
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // display detailed event view

        // send intent to the event view class
        Intent intent = new Intent(getActivity().getApplicationContext(), EventViewActivity.class);

        // Gets the event from objID,events hash
        Event toDisplay = events.get(position);

        intent.putExtra("eventId", toDisplay.getEventId());

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
     *
     * @param requestCode code of request:
     *                    LIST_CREATE_EVENT
     * @param resultCode  Activity.RESULT_OK or Acitivty.RESULT_CANCEL
     * @param data        intent that the result came with
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LIST_CREATE_EVENT) {
            if (resultCode == Activity.RESULT_OK) { // OK
                // manipulate the Intent data to get simple data
                Toast.makeText(getContext(), "Your event has been posted", Toast.LENGTH_SHORT).show();

                ea.update(1); // update the list tab
            }
            // else do nothing
        }
    }

    /**
     * update the list view on add or launch or request
     */
    public void update() {
        events = ea.getEventArray();
        filter(MainActivity.getTags());
        eventAdapter = new EventListAdapter(getActivity().getApplicationContext(), R.layout.list_row_view, events);
        listView.setAdapter(eventAdapter);
    }

    public void filter(ArrayList<String> tags) {

        if (tags.isEmpty())
            return;

        ArrayList<Event> temp = new ArrayList<>();

        for (int i = 0; i < events.size(); i++)
            for (int j = 0; j < tags.size(); j++)
                if (events.get(i).getTitle().toLowerCase().contains(tags.get(j).toLowerCase()) || // check if title
                        events.get(i).getTags().toLowerCase().contains(tags.get(j).toLowerCase()))    // or tags match
                    temp.add(events.get(i));

        events = temp;
    }
}
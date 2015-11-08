package andrewnguy.com.freefoodfinder;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ivan on 10/31/2015.
 */
public class EventArray
{
    private HashMap<String, Event> eventsMap; // event map
    private ParseQuery<ParseObject> eq;   //event query
    private String db;                    //the database
    private Context context;
    private Location myLoc;

    /**
     * Construct new event array class
     * @param db the name of the database
     */
    public EventArray(Context context, String db)
    {
        eventsMap = new HashMap<>();
        this.context = context;
        this.db = db;
        eq = ParseQuery.getQuery(db);
        update(MainActivity.EMPTY);
    }

    /**
     * Add an event to the parse
     * @param e
     */
    public void add(Event e)
    {
        ParseObject newEvent = new ParseObject(db);
        ParseGeoPoint pgp = new ParseGeoPoint(e.getLocation().latitude, e.getLocation().longitude);

        newEvent.put(context.getString(R.string.TIT), e.getTitle());              // put title
        newEvent.put(context.getString(R.string.LAT), e.getLocation().latitude);  // put latitude
        newEvent.put(context.getString(R.string.LNG), e.getLocation().longitude); // put longitude
        newEvent.put(context.getString(R.string.SDA), e.getStartDate());          // put start date
        newEvent.put(context.getString(R.string.EDA), e.getEndDate());            // put end date
        newEvent.put(context.getString(R.string.LOC), pgp);                       // put ParseGeoPoint

        if (e.getDescription() != null && !e.getDescription().isEmpty())         // put description if one exists
            newEvent.put(context.getString(R.string.DES), e.getDescription());

        // save the event to parse
        newEvent.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null)
                    Log.d("parse", e.getMessage());
            }
        });

        update(MainActivity.EMPTY); // re-fetch data
    }

    /**
     * Get the event with matching ID
     * @param objectId id of the event
     * @return return Event object matching that objectId
     */
    public Event get(String objectId) { return eventsMap.get(objectId); }

    /**
     * Get most recent array list of all the events
     * @param filter if looking for something specific
     * @return an ArrayList object of all the events
     */
    public ArrayList<Event> getEventArray(ArrayList<String> filter) {
        this.update(filter);
        return new ArrayList<>(eventsMap.values());
    }


    /**
     * setter for your current location
     * @param loc
     */
    public void setMyLoc(Location loc) {
        this.myLoc = loc;
    }

    /**
     * Helper method to update the local db
     * @param filter find correct events
     */
    private void update(ArrayList<String> filter)
    {
        List<ParseObject> temp;
        try {

            // TAG IMPLEMENTATION
            //if (!filter.isEmpty())
            //    eq.whereEqualTo(context.getString(R.string.TAG), )

            temp = eq.find();
            if (temp != null) {
                eventsMap.clear(); // clear
                for (ParseObject e : temp) {
                    LatLng eventLL = new LatLng(e.getParseGeoPoint(context.getString(R.string.LOC)).getLatitude(), e.getParseGeoPoint(context.getString(R.string.LOC)).getLongitude());
                    LatLng currLL;
                    try {
                        currLL = new LatLng(myLoc.getLatitude(), myLoc.getLongitude());
                    } catch (NullPointerException exception) {
                        Log.d("current location", exception.getMessage());
                        currLL = eventLL;
                    }

                    if (e.getString(context.getString(R.string.DES)) == null || e.getString(context.getString(R.string.DES)).isEmpty()) // if event has no description

                        eventsMap.put(e.getObjectId(), //store ParseObject objectId
                                new Event( //make new event to store
                                        e.getString(context.getString(R.string.TIT)),
                                        e.getDate(context.getString(R.string.SDA)),
                                        e.getDate(context.getString(R.string.EDA)),
                                        eventLL,
                                        currLL));
                    else   // if event does have a description
                        eventsMap.put(e.getObjectId(), // store ParseObject objectId
                                new Event( //make new event to store
                                        e.getString(context.getString(R.string.TIT)),
                                        e.getDate(context.getString(R.string.SDA)),
                                        e.getDate(context.getString(R.string.EDA)),
                                        e.getString(context.getString(R.string.DES)),
                                        eventLL,
                                        currLL));
                }
            }
        } catch (ParseException e) {
            Log.d("parse", e.getMessage());
        }
    }
}

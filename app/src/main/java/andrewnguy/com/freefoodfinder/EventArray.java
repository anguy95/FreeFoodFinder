package andrewnguy.com.freefoodfinder;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.google.android.gms.maps.model.Marker;
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
    private HashMap<String, Event> eventsMap; // objectId and event map
    private HashMap<Marker, String> eventsMapMarkers; // marker and objectId map
    private ParseQuery<ParseObject> eq;   //event query
    private String db;                    //the database
    private Context context;
    private Location myLoc;

    /**
     * Construct new event array class
     * @param context of the app
     * @param db the name of the database
     */
    public EventArray(Context context, String db)
    {
        eventsMap = new HashMap<>();
        eventsMapMarkers = new HashMap<>();
        this.context = context;
        this.db = db;
        eq = ParseQuery.getQuery(db);
        update(MainActivity.EMPTY, 2);
    }

    /**
     * Add an event to the parse
     * @param e event to add
     */
    public void add(Event e)
    {
        ParseObject newEvent = new ParseObject(db);
        ParseGeoPoint pgp = new ParseGeoPoint(e.getLocation().latitude, e.getLocation().longitude);

        newEvent.put(context.getString(R.string.TIT), e.getTitle());              // put title
        newEvent.put(context.getString(R.string.LAT), e.getLocation().latitude);  // put latitude
        newEvent.put(context.getString(R.string.LNG), e.getLocation().longitude); // put longitude
        newEvent.put(context.getString(R.string.DAT), e.getDate());          // put start date
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

        
        update(MainActivity.EMPTY, 2); // re-fetch data
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
    public ArrayList<Event> getEventArray(ArrayList<String> filter) { return new ArrayList<>(eventsMap.values()); }

    /**
     * Get the ObjectId/Event Map<K, V>
     * @return a hashmap of objectid/event pairs
     */
    public HashMap<String, Event> getObjectIdEventsMap() { return eventsMap; }

    /**
     * Get the Marker/ObjectId Map<K, V>
     * @return a hashmap of marker/objectId pairs
     */
    public HashMap<Marker, String> getEventMarkersMap() { return eventsMapMarkers; }

    /**
     * setter for your current location
     * @param loc
     */
    public void setMyLoc(Location loc) { this.myLoc = loc; }

    /**
     * Helper method to update the local db
     * @param filter find correct events
     * @param position 0 = map, 1 = list, 2 = both
     */
    public void update(ArrayList<String> filter, final int position)
    {
        /* filter check */

        if (!filter.isEmpty()) // if the filter is NOT empty
        {                      // prep it
            //TODO filter prep
        }

        eq.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> temp, ParseException exception) {
                if (exception == null) {
                    if (temp != null) {
                        eventsMap.clear(); // clear

                        LatLng currLL = null;
                        try {
                            currLL = new LatLng(myLoc.getLatitude(), myLoc.getLongitude());
                        } catch (NullPointerException nullexception) {
                            Log.d("current location", nullexception.getMessage());
                        }

                        for (ParseObject e : temp) {
                            LatLng eventLL = new LatLng(e.getParseGeoPoint(context.getString(R.string.LOC)).getLatitude(),
                                    e.getParseGeoPoint(context.getString(R.string.LOC)).getLongitude());

                            if (currLL == null)
                                currLL = eventLL;

                            if (e.getString(context.getString(R.string.DES)) == null ||
                                    e.getString(context.getString(R.string.DES)).isEmpty()) // if event has no description

                                eventsMap.put(e.getObjectId(), //store ParseObject objectId
                                        new Event( //make new event to store
                                                e.getObjectId(),
                                                e.getString(context.getString(R.string.TIT)),
                                                e.getString(context.getString(R.string.DAT)),
                                                e.getString(context.getString(R.string.TIM)),
                                                eventLL,
                                                currLL
                                        )
                                );
                            else   // if event does have a description
                                eventsMap.put(e.getObjectId(), // store ParseObject objectId
                                        new Event( //make new event to store
                                                e.getObjectId(),
                                                e.getString(context.getString(R.string.TIT)),
                                                e.getString(context.getString(R.string.DAT)),
                                                e.getString(context.getString(R.string.TIM)),
                                                e.getString(context.getString(R.string.DES)),
                                                eventLL,
                                                currLL
                                        )
                                );
                        }
                    } else {
                        Log.d("update", exception.getMessage());
                    }
                }

                /* update the map or the list or both after successful find */
                switch (position) {
                    case 0:
                        MainActivity.adapter.getMapTab().update(MainActivity.EMPTY);
                        break;
                    case 1:
                        MainActivity.adapter.getListTab().update(MainActivity.EMPTY);
                        break;
                    default:
                        MainActivity.adapter.getMapTab().update(MainActivity.EMPTY);
                        MainActivity.adapter.getListTab().update(MainActivity.EMPTY);
                        break;
                }
            }
        });
    }
}
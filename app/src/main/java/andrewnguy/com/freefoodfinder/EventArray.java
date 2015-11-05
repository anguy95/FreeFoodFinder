package andrewnguy.com.freefoodfinder;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Ivan on 10/31/2015.
 */
public class EventArray
{
    private HashMap<String, Event> eventsMap; // event map
    private ParseQuery<ParseObject> eq;   //event query
    private String db;                    //the database
    private Context context;

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
        update();
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
        newEvent.put(context.getString(R.string.DAT), e.getDate());               // put date
        newEvent.put(context.getString(R.string.LOC), pgp);                       // put ParseGeoPoint
        // save the event to parse
        newEvent.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null)
                    Log.d("parse", e.getMessage());
            }
        });

        update(); // re-fetch data
    }

    /**
     * Get the event with matching ID
     * @param objectId id of the event
     * @return return Event object matching that objectId
     */
    public Event get(String objectId) { return eventsMap.get(objectId); }

    /**
     * Get an array list of all the events
     * @return an ArrayList object of all the events
     */
    public ArrayList<Event> getEventArray() { return new ArrayList<>(eventsMap.values()); }

    /**
     * Helper method to update the local db
     */
    private void update()
    {
        List<ParseObject> temp;
        try {
            temp = eq.find();
            if (temp != null) {
                for (ParseObject e : temp) {
                    LatLng tempLL = new LatLng(e.getParseGeoPoint("Location").getLatitude(), e.getParseGeoPoint("Location").getLongitude());
                    eventsMap.put(e.getObjectId(), new Event(e.getString("Title"), e.getDate("Date"), tempLL));
                }
            }
        } catch (ParseException e) {
            Log.d("parse", e.getMessage());
        }
    }
}

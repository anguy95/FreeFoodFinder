package andrewnguy.com.freefoodfinder;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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

        update(); // re-fetch data
    }

    /**
     * Get the event with matching ID
     * @param objectId id of the event
     * @return return Event object matching that objectId
     */
    public Event get(String objectId) { return eventsMap.get(objectId); }

    /**
     * Get most recent array list of all the events
     * @return an ArrayList object of all the events
     */
    public ArrayList<Event> getEventArray() {
        this.update();
        return new ArrayList<>(eventsMap.values());
    }

    public ArrayList<Event> getEventArrayWithFilter(String tag) {
        this.updateWithFilter(tag);
        return new ArrayList<>(eventsMap.values());
    }


    public void updateWithFilter(String tag) {
        eventsMap.clear();
        updatWithFilterHelper("Tag1", tag);
        updatWithFilterHelper("Tag2", tag);
        updatWithFilterHelper("Tag3", tag);
        updatWithFilterHelper("Tag4", tag);
    }

    private void updatWithFilterHelper(String dbCol, String tag)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(db);
        query.whereEqualTo(dbCol, tag);
        List<ParseObject> temp;
        try {
            temp = query.find();
            if (temp != null) {
                //eventsMap.clear(); // clear
                for (ParseObject e : temp) {
                    LatLng tempLL = new LatLng(e.getParseGeoPoint(context.getString(R.string.LOC)).getLatitude(), e.getParseGeoPoint(context.getString(R.string.LOC)).getLongitude());
                    if (e.getString(context.getString(R.string.DES)) == null || e.getString(context.getString(R.string.DES)).isEmpty()) // if event has no description
                        eventsMap.put(e.getObjectId(), new Event(e.getString(context.getString(R.string.TIT)), e.getDate(context.getString(R.string.DAT)), tempLL));
                    else   // if event does have a description
                        eventsMap.put(e.getObjectId(), new Event(e.getString(context.getString(R.string.TIT)), e.getDate(context.getString(R.string.DAT)), tempLL, e.getString(context.getString(R.string.DES))));
                }
            }
        } catch (ParseException e) {
            Log.d("parse", e.getMessage());
        }
    }


    /**
     * Helper method to update the local db
     */
    private void update()
    {
        List<ParseObject> temp;
        try {
            temp = eq.find();
            if (temp != null) {
                eventsMap.clear(); // clear
                for (ParseObject e : temp) {
                    LatLng tempLL = new LatLng(e.getParseGeoPoint(context.getString(R.string.LOC)).getLatitude(), e.getParseGeoPoint(context.getString(R.string.LOC)).getLongitude());
                    if (e.getString(context.getString(R.string.DES)) == null || e.getString(context.getString(R.string.DES)).isEmpty()) // if event has no description
                        eventsMap.put(e.getObjectId(), new Event(e.getString(context.getString(R.string.TIT)), e.getDate(context.getString(R.string.DAT)), tempLL));
                    else   // if event does have a description
                        eventsMap.put(e.getObjectId(), new Event(e.getString(context.getString(R.string.TIT)), e.getDate(context.getString(R.string.DAT)), tempLL, e.getString(context.getString(R.string.DES))));
                }
            }
        } catch (ParseException e) {
            Log.d("parse", e.getMessage());
        }
    }
}

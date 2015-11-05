package andrewnguy.com.freefoodfinder;

import android.content.Context;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Ivan on 10/31/2015.
 */
public class EventArray
{
    private ArrayList<Event> eventsArray; //event array
    private ParseQuery<ParseObject> eq;   //event query
    private String db;                    //the database
    private Context context;

    /**
     * Construct new event array class
     * @param db the name of the database
     */
    public EventArray(Context context, String db)
    {
        eventsArray = new ArrayList<>();
        this.context = context;
        this.db = db;
        eq = ParseQuery.getQuery(db);
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
    }

}

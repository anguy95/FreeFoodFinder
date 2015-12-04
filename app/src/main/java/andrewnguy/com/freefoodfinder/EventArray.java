package andrewnguy.com.freefoodfinder;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Class that handles adding events to parse as well has storing
 * a map of all the events
 */
public class EventArray
{
    private HashMap<String, Event> eventsMap; // objectId and event map
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
        this.context = context;
        this.db = db;
        eq = ParseQuery.getQuery(db);
        update(2);
    }

    /**
     * Add an event to the parse, adds all necessary event information into a ParseObject
     * @param e event to add
     */
    public void add(Event e)
    {

        ParseObject newEvent = new ParseObject(db);
        ParseGeoPoint pgp = new ParseGeoPoint(e.getLocation().latitude, e.getLocation().longitude);

        newEvent.put(context.getString(R.string.TIT), e.getTitle());              // put title
        newEvent.put(context.getString(R.string.DES), e.getDescription());        // put description
        newEvent.put(context.getString(R.string.LOCSTRING), e.getLocationDescription());
        newEvent.put(context.getString(R.string.TAG), e.getTags()); // put tags
        newEvent.put(context.getString(R.string.DAT), e.getDate());               // put date
        newEvent.put(context.getString(R.string.STIM), e.getStartTime());               // put start time
        newEvent.put(context.getString(R.string.ETIM), e.getEndTime());               // put end time
        newEvent.put(context.getString(R.string.LAT), e.getLocation().latitude);  // put latitude
        newEvent.put(context.getString(R.string.LNG), e.getLocation().longitude); // put longitude
        newEvent.put(context.getString(R.string.LOC), pgp);                       // put ParseGeoPoint
        newEvent.put(context.getString(R.string.SCO), e.getEventScore());
        newEvent.put(context.getString(R.string.EAUTH), MainActivity.currentUser.getUsername());

        //rebuild event expiration date
        String[] dateArr = e.getDate().split("\\s"); // String format: [DayOfWeek, Month #Day #Year]; splits based on spaces
        String[] timeArr = e.getEndTime().replace(":", " ").split("\\s"); // String format: [hh:mm AM/PM]; splits based on : and spaces

        int offset = 0; // 0 offset for AM
        if (timeArr[2].equals("PM")) {
            offset = 12;
        }
        // else do nothing

        /*  year - the year plus 1900.                  *
         *  month - the month between 0-11.             *
         *  date - the day of the month between 1-31.   *
         *  hrs - the hours between 0-23.               *
         *  min - the minutes between 0-59              */

        Date date = new Date(Integer.parseInt(dateArr[3]) - 1900, // intYear - 1900
                monthToNum(dateArr[1]),              // intMonth
                Integer.parseInt(dateArr[2]),        // intDay
                Integer.parseInt(timeArr[0]) - 1 + offset, // intHour
                Integer.parseInt(timeArr[1]));       // intMin
        newEvent.put(context.getString(R.string.EXP), date.getTime());

        // save the event to parse
        try {
            newEvent.save();
        } catch (ParseException e1) {
            e1.printStackTrace();
            }

        update(2); // re-fetch data
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
    public ArrayList<Event> getEventArray() { return new ArrayList<>(eventsMap.values()); }

    /**
     * Get the ObjectId/Event Map<K, V>
     * @return a hashmap of objectid/event pairs
     */
    public HashMap<String, Event> getEventMap() { return eventsMap; }

    /**
     * setter for your current location
     * @param loc set a location object of the users current location/position based on GPS
     */
    public void setMyLoc(Location loc) { this.myLoc = loc; }

    /**
     * Helper method to update the local db
     * @param position 0 = map, 1 = list, 2 = both
     */
    public void update(final int position)
    {
        eq.cancel(); // cancel an older request:: MAKE SURE THIS IS GOOD WHEN WE HAVE LOTS OF EVENTS
                    // needs to be heavily stress tested -> lots of runtime exceptions
        eq.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> temp, ParseException exception) {
                if (exception == null) {
                    if (temp != null) { // only do something if temp isn't null
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

                            eventsMap.put(e.getObjectId(), //store ParseObject objectId
                                    new Event( //make new event to store
                                            e.getObjectId(),
                                            e.getString(context.getString(R.string.TIT)),
                                            e.getString(context.getString(R.string.DAT)),
                                            e.getString(context.getString(R.string.STIM)),
                                            e.getString(context.getString(R.string.ETIM)),
                                            e.getString(context.getString(R.string.DES)),
                                            e.getString(context.getString(R.string.TAG)),
                                            e.getInt(context.getString(R.string.SCO)),
                                            e.getString(context.getString(R.string.EAUTH)),
                                            e.getString(context.getString(R.string.LOCSTRING)),
                                            eventLL,
                                            currLL
                                        )
                                );
                        }
                    }
                }
                else {
                    Log.e("update", exception.getMessage());
                }

                /* update the map or the list or both after successful find */
                try {
                    switch (position) {
                        case 0:
                            MainActivity.adapter.getMapTab().update();
                            break;
                        case 1:
                            MainActivity.adapter.getListTab().update();
                            break;
                        default:
                            MainActivity.adapter.getMapTab().update();
                            MainActivity.adapter.getListTab().update();
                            break;
                    }
                } catch (NullPointerException e) {
                    Log.e("Unable to update", e.getMessage());
                }
            }
        });
    }

    private int monthToNum(String month) {
        switch (month) {
            case "Jan": return 0;
            case "Feb": return 1;
            case "Mar": return 2;
            case "Apr": return 3;
            case "May": return 4;
            case "Jun": return 5;
            case "Jul": return 6;
            case "Aug": return 7;
            case "Sep": return 8;
            case "Oct": return 9;
            case "Nov": return 10;
            default:    return 11; // case "Dec":
        }
    }
}
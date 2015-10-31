package andrewnguy.com.freefoodfinder;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Ivan on 10/31/2015.
 */
public class EventArray {

    // i violated dry guys.. i copy pasta'd this from confirmeventactivity..
    private static final String DB_NAME = "currentFreeFoodsDB";
    private static final String LAT_COL = "LocationLat";
    private static final String LNG_COL = "LocationLong";
    private static final String DESC_LOC_COL = "DescriptionLocation";
    private static final String DESC_EV_COL= "DescriptionEvent";
    private static final String LATLNGLOCATION = "Location";
    private static final String TITLE_COL = "EventTitle";

    private ArrayList<Event> eventsArray;

    public EventArray() {
        ParseQuery<ParseObject> evQuery = ParseQuery.getQuery(DB_NAME);
        evQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    while (!list.isEmpty()) {
                        ListIterator<ParseObject> it = list.listIterator();
                        ParseObject currEvent = it.next();
                        String evid = currEvent.getObjectId();
                        double lat = currEvent.getDouble(LAT_COL);
                        double lng = currEvent.getDouble(LNG_COL);
                        String evtit = currEvent.getString(TITLE_COL);
                        String evdesc = currEvent.getString(DESC_EV_COL);
                        String evloc = currEvent.getString(DESC_LOC_COL);

                        Event evobj = new Event(evid, lat, lng, evtit, evdesc, evloc);
                        eventsArray.add(evobj);
                    }
                } else {
                    Log.d("EventArray", e.getMessage());
                }
            }
        });
    }

    public ListIterator<Event> getEventsIterator() {
        return eventsArray.listIterator();
    }

}

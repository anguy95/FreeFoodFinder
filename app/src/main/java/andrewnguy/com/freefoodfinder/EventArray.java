package andrewnguy.com.freefoodfinder;

import android.content.Context;
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
public class EventArray
{

    private ArrayList<Event> eventsArray;

    /**
     * Make sure to include a context when constructing
     * Most likely needs to be EventArray(getContext()) or something
     * @param context
     */
    public EventArray(Context context) {

        final Context C = context;
        eventsArray = new ArrayList<>();

        ParseQuery<ParseObject> evQuery = ParseQuery.getQuery(C.getString(R.string.DB));
        evQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    while (!list.isEmpty()) {
                        ListIterator<ParseObject> it = list.listIterator();
                        ParseObject currEvent = it.next();
                        String evid = currEvent.getObjectId();
                        double lat = currEvent.getDouble(C.getString(R.string.LAT));
                        double lng = currEvent.getDouble(C.getString(R.string.LNG));
                        String evtit = currEvent.getString(C.getString(R.string.TIT));
                        String evdesc = currEvent.getString(C.getString(R.string.DES));
                        String evloc = currEvent.getString(C.getString(R.string.LOC));

                        Event evobj = new Event(evid, evtit, evdesc, lat, lng);
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

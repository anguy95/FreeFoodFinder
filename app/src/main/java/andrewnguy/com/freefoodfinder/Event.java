package andrewnguy.com.freefoodfinder;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by Ivan on 10/30/2015.
 */
public class Event {

    public String id;
    public LatLng latlong;
    public String title;
    public String eventDesc;
    public String locDesc;
    public Date date; // maybe can take this out
    public Date start;
    public Date end;

    // still have to add start and end dates
    public Event(String idin, double lat, double lng, String tit, String event, String location) {
        id = idin;
        latlong = new LatLng(lat, lng);
        title = tit;
        eventDesc = event;
        locDesc = location;
    }

}

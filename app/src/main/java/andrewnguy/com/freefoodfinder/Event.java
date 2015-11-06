package andrewnguy.com.freefoodfinder;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseGeoPoint;

import java.util.Date;

/**
 * Created by anguy95 on 10/31/15.
 */
public class Event {

    private int eventId;        // id # of an event
    private String eventTitle;  // title of an event
    private String eventDesc;   // description of an event
    private Date startDate;     // start date of the event
    private Date endDate;       // end date of the event
    private LatLng latLng;      // location of an event
    private double dist;        // distance from you to event


    //We need to figure out how to store the times and what not, see if there is a scrollable and
    //clickable time selector as well as date selector


    /**
     * Reconstructor w/o event description
     * @param title
     * @param startDate
     * @param endDate
     * @param latlng
     */
    public Event(String title, Date startDate, Date endDate, LatLng latlng, LatLng currLL)
    {
        this.eventTitle = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.latLng = latlng;

        // calc distance from you and the event
        ParseGeoPoint toLocation = new ParseGeoPoint(latlng.latitude,latlng.longitude);
        ParseGeoPoint fromLocation = new ParseGeoPoint(currLL.latitude, currLL.longitude);
        this.dist = fromLocation.distanceInMilesTo(toLocation);
    }

    /**
     * Reconstructor w/ event description
     * @param title
     * @param startDate
     * @param endDate
     * @param description
     * @param latlng
     */
    public Event(String title, Date startDate, Date endDate, String description, LatLng latlng, LatLng currLL)
    {
        this.eventTitle = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.latLng = latlng;
        this.eventDesc = description;

        // calc distance from you and the event
        ParseGeoPoint toLocation = new ParseGeoPoint(latlng.latitude,latlng.longitude);
        ParseGeoPoint fromLocation = new ParseGeoPoint(currLL.latitude, currLL.longitude);
        this.dist = fromLocation.distanceInMilesTo(toLocation);
    }

    /**
     * Constructor w/o event description
     * @param title
     * @param startDate
     * @param endDate
     * @param lng
     * @param currLat
     * @param currLng
     */
    public Event(String title, Date startDate, Date endDate,
                 double lat, double lng, double currLat, double currLng)
    {
        this.eventTitle = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.latLng = new LatLng(lat, lng);

        // calc distance from you and the event
        ParseGeoPoint toLocation = new ParseGeoPoint(lat,lng);
        ParseGeoPoint fromLocation = new ParseGeoPoint(currLat, currLng);
        this.dist = fromLocation.distanceInMilesTo(toLocation);    }

    /**
     * Constructor w/ event descriptions
     * @param title
     * @param startDate
     * @param endDate
     * @param lat
     * @param lng
     * @param currLat
     * @param currLng
     * @param description
     */
    public Event(String title, Date startDate, Date endDate,
                 double lat, double lng, double currLat, double currLng, String description)
    {
        this.eventTitle = title;
        this.eventDesc  = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.latLng = new LatLng(lat, lng);

        // calc distance from you and the event
        ParseGeoPoint toLocation = new ParseGeoPoint(lat,lng);
        ParseGeoPoint fromLocation = new ParseGeoPoint(currLat, currLng);
        this.dist = fromLocation.distanceInMilesTo(toLocation);

    }


    /* ----------  GETTERS ---------- */

    /**
     * Gets the title of the Event
     * @return a String of the Event title
     */
    public String getTitle() { return eventTitle; }

    /**
     * Get the description of the event
     * @return a String of the event description
     */
    public String getDescription() { return eventDesc; }

    /**
     * Get start date of the event
     * @return a Date object of the event
     */
    public Date getStartDate() { return startDate; }

    /**
     * Get end date of the event
     * @return a Date object of the event
     */
    public Date getEndDate() { return endDate; }

    /**
     * Get the location of the event
     * @return a LatLng object of the event
     */
    public LatLng getLocation() { return latLng; }

    /**
     * get the distance from your location to the events
     * @return
     */
    public double getDist() { return dist; }
}

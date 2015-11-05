package andrewnguy.com.freefoodfinder;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by anguy95 on 10/31/15.
 */
public class Event {

    private int eventId;        // id # of an event
    private String eventTitle;  // title of an event
    private String eventDesc;   // description of an event
    private Date eventDate;     // date of an event (int year, int month, int day, int hour, int minute)
    private LatLng latLng;      // location of an event


    //We need to figure out how to store the times and what not, see if there is a scrollable and
    //clickable time selector as well as date selector
    public Event(Event event)
    {

    }

    /**
     * Constructor w/o event description
     * @param title
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param lat
     * @param lng
     */
    public Event(String title, int year, int month, int day, int hour, int minute, double lat, double lng)
    {
        this.eventTitle = title;
        this.eventDate = new Date(year, month, day, hour, minute);
        this.latLng = new LatLng(lat, lng);
    }

    /**
     * Constructor w/ event descriptions
     * @param title
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param lat
     * @param lng
     * @param description
     */
    public Event(String title, int year, int month, int day, int hour, int minute, double lat, double lng,
                 String description)
    {
        this.eventTitle = title;
        this.eventDesc  = description;
        this.eventDate = new Date(year, month, day, hour, minute);
        this.latLng = new LatLng(lat, lng);
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
     * Get date of the event
     * @return a Date object of the event
     */
    public Date getDate() { return eventDate; }

    /**
     * Get the location of the event
     * @return a LatLng object of the event
     */
    public LatLng getLocation() { return latLng; }

}

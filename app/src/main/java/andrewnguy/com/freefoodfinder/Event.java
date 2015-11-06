package andrewnguy.com.freefoodfinder;

import android.location.Location;

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
    private float[] dist;        // distance from you to event


    //We need to figure out how to store the times and what not, see if there is a scrollable and
    //clickable time selector as well as date selector


    /**
     * Reconstructor w/o event description
     * @param title
     * @param date
     * @param latlng
     */
    public Event(String title, Date date, LatLng latlng)
    {
        this.eventTitle = title;
        this.eventDate = date;
        this.latLng = latlng;
    }

    /**
     * Reconstructor w/ event description
     * @param title
     * @param date
     * @param latlng
     */
    public Event(String title, Date date, LatLng latlng, String description)
    {
        this.eventTitle = title;
        this.eventDate = date;
        this.latLng = latlng;
        this.eventDesc = description;
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
     * @param currLat
     * @param currLng
     */
    public Event(String title, int year, int month, int day, int hour, int minute,
                 double lat, double lng, double currLat, double currLng)
    {
        this.eventTitle = title;
        this.eventDate = setDate(year, month, day, hour, minute);
        this.latLng = new LatLng(lat, lng);

        // calc distance from you and the event
        Location.distanceBetween(lat, lng, currLat, currLng, this.dist);
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
     * @param currLat
     * @param currLng
     * @param description
     */
    public Event(String title, int year, int month, int day, int hour, int minute,
                 double lat, double lng, double currLat, double currLng, String description)
    {
        this.eventTitle = title;
        this.eventDesc  = description;
        this.eventDate = setDate(year, month, day, hour, minute);
        this.latLng = new LatLng(lat, lng);

        // calc distance from you and the event
        Location.distanceBetween(lat, lng, currLat, currLng, this.dist);
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


    /* ----------  HELPER FUNCTIONS ---------- */

    private Date setDate(int year, int month, int day, int hour, int minute)
    {
        /*  year - the year plus 1900.                  *
         *  month - the month between 0-11.             *
         *  date - the day of the month between 1-31.   *
         *  hrs - the hours between 0-23.               *
         *  min - the minutes between 0-59              */

        // need to do conversions
        int Y = year - 1900;
        // month in CEA will be be from 0-11
        // everything else is normal

        return new Date(Y, month, day, hour, minute);
    }
}

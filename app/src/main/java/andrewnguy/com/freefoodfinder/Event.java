package andrewnguy.com.freefoodfinder;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseGeoPoint;

import java.util.Date;

/**
 * Created by anguy95 on 10/31/15.
 */
public class Event {

    private String eventId;     // id # of an event
    private String eventTitle;  // title of an event
    private String eventDesc;   // description of an event
    private String eventTags;   // event tags
    private int    eventScore;
    private String dateOfEvent; // start date of the event
    private String startTimeOfEvent; // time of an event
    private String endTimeOfEvent; // time of an event
    private LatLng latLng;      // location of an event

    private double dist;        // distance from you to event


    //We need to figure out how to store the times and what not, see if there is a scrollable and
    //clickable time selector as well as date selector

    /**
     * Constructor without objectId
     * @param title
     * @param date
     * @param startTime
     * @param endTime
     * @param description
     * @param latlng
     * @param currLL
     */
    public Event (String title, String date, String startTime, String endTime,
                  String description, String tags, int score, LatLng latlng, LatLng currLL)
    {
        this.eventTitle = title;
        this.dateOfEvent = date;
        this.startTimeOfEvent = startTime;
        this.endTimeOfEvent = endTime;
        this.eventDesc = description;
        this.eventTags = tags;
        this.latLng = latlng;
        this.eventScore = score;

        // calc distance from you and the event
        ParseGeoPoint toLocation = new ParseGeoPoint(latlng.latitude, latlng.longitude);
        ParseGeoPoint fromLocation = new ParseGeoPoint(currLL.latitude, currLL.longitude);
        this.dist = fromLocation.distanceInMilesTo(toLocation);
    }

    /**
     * Constructor with objectId
     * @param id
     * @param title
     * @param date
     * @param startTime
     * @param endTime
     * @param description
     * @param latlng
     * @param currLL
     */
    public Event(String id, String title, String date, String startTime, String endTime,
                 String description, String tags,int score, LatLng latlng, LatLng currLL )
    {
        this(title, date, startTime, endTime, description, tags, score, latlng, currLL);
        this.eventId = id;
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
     * Get the event tags
     * @return
     */
    public String getTags() { return eventTags; }

    /**
     * Get date of the event
     * @return a string date of the event
     */
    public String getDate() { return dateOfEvent; }

    /**
     * Get start time of the event
     * @return a string start time of the event
     */
    public String getStartTime() { return startTimeOfEvent; }

    /**
     * Get end time of the event
     * @return a string end time of the event
     */
    public String getEndTime() { return endTimeOfEvent; }

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


    /**
     * get the eventID of the event
     * @return
     */
    public String getEventId() { return eventId; }

    /**
     * gets the score of the event
     * @return
     */
    public int getEventScore() { return eventScore;}

    /* ----------  SETTERS ---------- */

    public void setEventID(String objectID){
        this.eventId = objectID;
    }

    public void setEventScore(int score){
        this.eventScore = score;

    }

}

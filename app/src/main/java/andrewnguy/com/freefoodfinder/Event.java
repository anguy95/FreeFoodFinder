package andrewnguy.com.freefoodfinder;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseGeoPoint;

/**
 * Class used to create/hold all necessary data for each event
 */
public class Event {

    private String eventId;     // id # of an event
    private String eventTitle;  // title of an event
    private String eventDesc;   // description of an event
    private String eventTags;   // event tags
    private String eventAuthor; // event creator
    private int    eventScore;  // event score
    private String dateOfEvent; // start date of the event
    private String startTimeOfEvent; // time of an event
    private String endTimeOfEvent; // time of an event
    private String locDesc;     // description of location
    private LatLng latLng;      // location of an event

    private double dist;        // distance from you to event


    /**
     * Constructor WITHOUT objectId
     * @param title title of the event
     * @param date date of the event
     * @param startTime start time of the even
     * @param endTime end time of the event
     * @param description a description of the event
     * @param tags tags associated with the event
     * @param score number of likes the event has
     * @param eventAuthor who created the event
     * @param locDesc a string describing the location (e.g. building name)
     * @param latlng a lat/long coordinate of where the event is located
     * @param currLL a lat/long coordinate of where the user is (used distance between event and user calculations)
     */
    public Event (String title, String date, String startTime, String endTime,
                  String description, String tags, int score, String eventAuthor, String locDesc, LatLng latlng, LatLng currLL)
    {
        this.eventTitle = title;
        this.dateOfEvent = date;
        this.startTimeOfEvent = startTime;
        this.endTimeOfEvent = endTime;
        this.eventDesc = description;
        this.eventTags = tags;
        this.latLng = latlng;
        this.eventAuthor = eventAuthor;
        this.locDesc = locDesc;
        this.eventScore = score;

        // calc distance from you and the event
        ParseGeoPoint toLocation = new ParseGeoPoint(latlng.latitude, latlng.longitude);
        ParseGeoPoint fromLocation = new ParseGeoPoint(currLL.latitude, currLL.longitude);
        this.dist = fromLocation.distanceInMilesTo(toLocation);
    }

    /**
     * Constructor WITH objectId
     * @param id the objectId of the event (based on parse assignment)
     * @param title title of the event
     * @param date date of the event
     * @param startTime start time of the even
     * @param endTime end time of the event
     * @param description a description of the event
     * @param tags tags associated with the event
     * @param score number of likes the event has
     * @param eventAuthor who created the event
     * @param locDesc a string describing the location (e.g. building name)
     * @param latlng a lat/long coordinate of where the event is located
     * @param currLL a lat/long coordinate of where the user is (used distance between event and user calculations)
     */
    public Event(String id, String title, String date, String startTime, String endTime,
                 String description, String tags,int score, String eventAuthor, String locDesc, LatLng latlng, LatLng currLL )
    {
        this(title, date, startTime, endTime, description, tags, score, eventAuthor, locDesc, latlng, currLL);
        this.eventId = id;
    }


    /* ----------  GETTERS ---------- */

    /**
     * @return a String of the Event title
     */
    public String getTitle() { return eventTitle; }

    /**
     * @return a String of the event description
     */
    public String getDescription() { return eventDesc; }

    /**
     * @return a String of all the tags
     */
    public String getTags() { return eventTags; }

    /**
     * @return a string date of the event
     */
    public String getDate() { return dateOfEvent; }

    /**
     * @return a string start time of the event
     */
    public String getStartTime() { return startTimeOfEvent; }

    /**
     * @return a string end time of the event
     */
    public String getEndTime() { return endTimeOfEvent; }

    /**
     * @return a LatLng object of the event
     */
    public LatLng getLocation() { return latLng; }

    /**
     * @return the distance from your location to the event
     */
    public double getDist() { return dist; }

    /**
     * @return a string of the event's id
     */
    public String getEventId() { return eventId; }

    /**
     * @return an int of the number of likes the event has
     */
    public int getEventScore() { return eventScore;}

    /**
     * @return a string describing the event's location (e.g. building name)
     */
    public String getLocationDescription() { return locDesc;}

    /**
     * @return a string of the event author's id
     */
    public String getEventAuthor() { return eventAuthor;}


    /* ----------  SETTERS ---------- */

    /**
     * used users like/unlike an event
     * @param score set the score of an event
     */
    public void setEventScore(int score){
        this.eventScore = score;
    }

}

package andrewnguy.com.freefoodfinder;

/**
 * Created by anguy95 on 10/31/15.
 */
public class Event {

    private String eventTitle;
    private String eventWeekDay;
    private String eventMonth;
    private int eventDay;
    private int eventYear;
    private String locationDescription;
    private String eventDescription;
    public String dist = "22";


    //We need to figure out how to store the times and what not, see if there is a scrollable and
    //clickable time selector as well as date selector

    /**
     *
     * @param title
     * @param description
     */
    public Event(String title, String description) {
        this.eventTitle = title;
        this.eventDescription = description;
    }


    /**
     * Gets the title of the Event
     * @return a String of the Event title
     */
    public String getEventTitle() {
        return eventTitle;
    }

    /**
     * Sets the title of the Event
     * @param eventTitle the title of the Event you want to add
     */
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    /**
     * Gets the day of the week the Event is in. Eg. Mon, Tue, Wed, ....
     * @return a String of the day of the week
     */
    public String getEventWeekDay() {
        return eventWeekDay;
    }

    /**
     *
     * @param eventWeekDay
     */
    public void setEventWeekDay(String eventWeekDay) {
        this.eventWeekDay = eventWeekDay;
    }


    /**
     *
     * @return
     */
    public String getEventMonth() {
        return eventMonth;
    }

    /**
     *
     * @param eventMonth
     */
    public void setEventMonth(String eventMonth) {
        this.eventMonth = eventMonth;
    }


    /**
     *
     * @return
     */
    public int getEventDay() {
        return eventDay;
    }


    /**
     *
     * @param eventDay
     */
    public void setEventDay(int eventDay) {
        this.eventDay = eventDay;
    }


    /**
     *
     * @return
     */
    public int getEventYear() {
        return eventYear;
    }


    /**
     *
     * @param eventYear
     */
    public void setEventYear(int eventYear) {
        this.eventYear = eventYear;
    }


    /**
     *
     * @return
     */
    public String getLocationDescription() {
        return locationDescription;
    }


    /**
     *
     * @param locationDescription
     */
    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    /**
     *
     * @return
     */
    public String getEventDescription() {
        return eventDescription;
    }


    /**
     *
     * @param eventDescription
     */
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }


    /**
     *
     * @return
     */
    public String getDate(){
        String date = eventWeekDay + " , " + eventMonth + " " + eventDay + " " + eventYear;
        return date;
    }





}

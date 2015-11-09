package andrewnguy.com.freefoodfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by anguy95 on 10/31/15.
 */
public class EventListAdapter extends ArrayAdapter<Event> {

    private final Context context;
    private final ArrayList<Event> data;
    private final int layoutResourceId;

    public EventListAdapter(Context context, int layoutResourceId, ArrayList<Event> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //The View that gets the list_row_view layout
        View row = convertView;

        //Class that holds all our types of views
        ViewHolder holder = null;

        if(row == null) {  //If the row of the ViewGroup is empty then add a new item using list_row_view layout

            //Gets the context of the place that calls this EventListAdapter
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);


            //Assigns the Views to its type of view
            holder = new ViewHolder();
            holder.eventTitle = (TextView) row.findViewById(R.id.event_title_row);
            holder.dist = (TextView) row.findViewById(R.id.event_distance_row);
            holder.eventDescription = (TextView) row.findViewById(R.id.event_description_row);
            holder.dateDay = (TextView) row.findViewById(R.id.event_date_row);
            holder.dateTime = (TextView) row.findViewById(R.id.event_time_row);

            row.setTag(holder);
        }
        else {  //If row is not empty then just return that View's tag
            holder = (ViewHolder)row.getTag();
        }

        //Gets Event object in the ArrayList<Event> at that current position
        Event event = data.get(position);

        // To check if display across two days

        String timeOfEvent;

        String dateToDisplay = event.getDate();
        //Sets up string
        timeOfEvent = event.getStartTime() + " - " + event.getEndTime();

        holder.dateDay.setText(dateToDisplay);
        holder.dateTime.setText(timeOfEvent);

        //Sets the views text
        holder.eventTitle.setText(event.getTitle());
        holder.eventDescription.setText(event.getDescription());
        holder.dist.setText(String.format("%.2f mi", event.getDist()));

        return row;

    }


    /**
     * This is a ViewHolder that holds the different types of Views that will be available
     * in the ListView's row
     */
    static class ViewHolder
    {
        TextView eventTitle;
        TextView eventDescription;
        TextView dist;
        TextView dateDay;
        TextView dateTime;
    }

}

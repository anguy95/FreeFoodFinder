package andrewnguy.com.freefoodfinder;

/**
 * Created by anguy95 on 10/27/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class MapTab extends Fragment implements View.OnClickListener
{
    private static final int MAP_CREATE_EVENT = 1; // give MatTab a requestCode of 1 when trying to make an event

    /** map interaction **/
    private GoogleMap map;
    private MapView mapView;
    private FloatingActionButton fab; // FAB to bring up the addPin
    private RelativeLayout addPin;    // the add-an-event-pin
    private Button cancel, confirm, seeMore;   // add event buttons
    private EventArray ea;

    /** parse **/
    private HashMap<String, Marker> eventMarkers = new HashMap<>();
    private HashMap<Marker, Event> events = new HashMap<>();

    /** my location **/
    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            //LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            ea.setMyLoc(location);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.maps_tab,container,false);
        addPin = (RelativeLayout) v.findViewById(R.id.maps_tab_add_pin);

        ea = MainActivity.ea;



        /* BUTTONS */
        fab = (FloatingActionButton) v.findViewById(R.id.maps_tab_fab);
        fab.setOnClickListener(this);

        cancel = (Button) v.findViewById(R.id.add_event_pin_cancel_button);
        cancel.setOnClickListener(this);

        confirm = (Button) v.findViewById(R.id.add_event_pin_confirm_button);
        confirm.setOnClickListener(this);

        /** start up the map -- center map on UCSD **/
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        map = mapView.getMap(); //getMapAsync??

        double lat = 32.8805071;
        double lng = -117.2365000;
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), (float) 14.9);
        map.moveCamera(cu);

        // be able to center the map on yourself
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);
        map.setOnMyLocationChangeListener(myLocationChangeListener);

        /* Adding existing markers */
        update();


        /* set up marker info viewing */
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // the frame
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            // the contents/information
            @Override
            public View getInfoContents(Marker marker) {

                // Gets the event from objID,events hash
                Event toDisplay = events.get(marker);

                View v = getLayoutInflater(savedInstanceState).inflate(R.layout.marker_info_window, null);

                //Sets up Date string
                String dateOfEvent = toDisplay.getDate();

                // Sets up the view fields for bubble
                TextView eventTitle = (TextView) v.findViewById(R.id.marker_info_window_title);
                eventTitle.setText(toDisplay.getTitle());

                TextView eventDate = (TextView) v.findViewById(R.id.marker_info_window_date);
                eventDate.setText(dateOfEvent);


                //TODO SET TIME UPDATE HARD CODED
                TextView eventTime = (TextView) v.findViewById(R.id.marker_info_window_time);
                eventTime.setText((String) (toDisplay.getStartTime() + " - " + toDisplay.getEndTime()));

                return v;
            }

        });

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(getActivity().getApplicationContext(), EventViewActivity.class);

                // Gets the event from objID,events hash
                Event toDisplay = events.get(marker);

                intent.putExtra("eventTitle", toDisplay.getTitle());
                intent.putExtra("eventDesc", toDisplay.getDescription());

                //TODO ADD THE DATE, START AND END TIME, AS WELL AS LOCATION DESC
                intent.putExtra("eventStartTime", toDisplay.getStartTime());
                intent.putExtra("eventEndTime", toDisplay.getEndTime());
                intent.putExtra("eventDate", toDisplay.getDate());

                intent.putExtra("eventDate", toDisplay.getDate());
                intent.putExtra("eventLocDesc", toDisplay.getLocation());

                startActivity(intent);
            }


        });

        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * Check click events. Responds to:
     * fab button (add event)
     * cancel button (creating event)
     * confirm button (creating event)
     *
     * @param v View that triggered onClick
     */
    @Override
    public void onClick(View v)
    {
        int id = v.getId(); // get the view id

        if (id == R.id.maps_tab_fab) { // if fab was hit
            addPin.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
        }
        else if (id == R.id.add_event_pin_cancel_button) { // if cancel button
            addPin.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
        }
        else { // if confirm button
            LatLng eventLoc = map.getCameraPosition().target;
            double lat = eventLoc.latitude;
            double lng = eventLoc.longitude;
            Intent intent = new Intent(getActivity().getApplicationContext(), ConfirmEventActivity.class);
            intent.putExtra("latitude", lat);
            intent.putExtra("longitude", lng);
            try {
                intent.putExtra("currLat", map.getMyLocation().getLatitude());
                intent.putExtra("currLng", map.getMyLocation().getLongitude());
            } catch(NullPointerException npe) { Log.d("mylocation", npe.getMessage()); }
            startActivityForResult(intent, MAP_CREATE_EVENT);
        }
    }


    /**
     * Get a result from an activity
     * @param requestCode code of request:
     *                    MAP_CREATE_EVENT
     * @param resultCode Activity.RESULT_OK or Activity.RESULT_CANCEL
     * @param data intent that the result came with
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // check for correct requestCode
        if (requestCode == MAP_CREATE_EVENT)
        {
            // check the resultCode
            if (resultCode == Activity.RESULT_OK) { // OK
                Toast.makeText(getContext(), "Your event has been posted", Toast.LENGTH_SHORT).show();
                update();
            }

            /* do this stuff if cancelled as well (or after adding the marker) */

            // remove graphic
            addPin.setVisibility(View.GONE);

            // bring back the fab
            fab.setVisibility(View.VISIBLE);
        }
    }

    /**
     * update the map view on add or launch or request
     */
    public void update() {

        ea.setMyLoc(map.getMyLocation()); // set up current location for distance calculations

        HashMap<String, Event> tempMap = ea.getEventMap(); // get some filtered results

        String[] newKeys = new String[tempMap.size()]; // make an array that
        tempMap.keySet().toArray(newKeys);             // contains all the new keys (objectIds)

        String[] oldKeys = new String[eventMarkers.size()]; // also make an array that
        eventMarkers.keySet().toArray(oldKeys);             // contains all the old keys

        /* in with the new */
        for (int i = 0; i < newKeys.length; i++) {

            if ( !eventMarkers.containsKey(newKeys[i]) ) { // if the current event map doesn't have this marker, add
                Event tempEvent = tempMap.get(newKeys[i]); // get the event (reference)
                Marker tempMarker = map.addMarker(new MarkerOptions().position(tempEvent.getLocation())); // add to map
                eventMarkers.put(newKeys[i], tempMarker);  // add marker to hashmap<id, marker>
                events.put(tempMarker, tempEvent);         // add event to hashmap<marker, event>
            }
        }
        /* and out with the old */
        for (int i = 0; i < oldKeys.length; i++) {

            if ( !tempMap.containsKey(oldKeys[i]) ) { // if the current event map has something expired
                Marker tempMarker = eventMarkers.get(oldKeys[i]); // get the marker reference
                tempMarker.remove();                              // and remove it from everything
                eventMarkers.remove(oldKeys[i]);
                events.remove(tempMarker);
            }
        }

        filter(MainActivity.getTags());
    }

    public void filter(ArrayList<String> tags) {
        //private HashMap<String, Marker> eventMarkers = new HashMap<>();
        //private HashMap<Marker, Event> events = new HashMap<>();

        if (tags.isEmpty())
            return;

        //get values (events)
        Collection<Event> eventCollection = events.values();
        List<Event> eventsArray = new ArrayList<>(eventCollection);

        //filter tags
        ArrayList<Event> filteredEvents = new ArrayList<>();
        for (int i = 0; i < eventsArray.size(); i++)
            for (int j = 0; j < tags.size(); j++)
                if (eventsArray.get(i).getTitle().toLowerCase().contains(tags.get(j).toLowerCase()) || // check if title
                    eventsArray.get(i).getTags().toLowerCase().contains(tags.get(j).toLowerCase()))    // or tags match
                    filteredEvents.add(eventsArray.get(i));

        // set all markers invisible
        Collection<Marker> markers = eventMarkers.values();
        List<Marker> markersArray = new ArrayList<>(markers);
        for (int i = 0; i < markersArray.size(); i++)
            markersArray.get(i).setVisible(false);

        // set filtered visible
        for (int i = 0; i < filteredEvents.size(); i++)
            eventMarkers.get(filteredEvents.get(i).getEventId()).setVisible(true);
    }
}




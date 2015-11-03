package andrewnguy.com.freefoodfinder;

/**
 * Created by anguy95 on 10/27/15.
 */

import android.app.Activity;
import android.content.Intent;
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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapTab extends Fragment implements View.OnClickListener
{
    private static final int MAP_CREATE_EVENT = 1; // give MatTab a requestCode of 1 when trying to make an event

    /** map interaction **/
    private GoogleMap map;
    private MapView mapView;
    private FloatingActionButton fab; // FAB to bring up the addPin
    private RelativeLayout addPin;    // the add-an-event-pin
    private Button cancel, confirm;   // add event buttons

    /** parse **/
    private final ArrayList<Double> markers = new ArrayList<>();
    private final ArrayList<Double> tempLat = new ArrayList<>();
    private final ArrayList<Double> tempLong = new ArrayList<>();

    private static final String TAG = "MyActivity";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.maps_tab,container,false);
        addPin = (RelativeLayout) v.findViewById(R.id.add_pin);

        /* BUTTONS */
        fab = (FloatingActionButton) v.findViewById(R.id.maps_fab);
        fab.setOnClickListener(this);

        cancel = (Button) v.findViewById(R.id.buttonCancel);
        cancel.setOnClickListener(this);

        confirm = (Button) v.findViewById(R.id.buttonConfirm);
        confirm.setOnClickListener(this);

        /** start up the map -- center map on UCSD **/
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        map = mapView.getMap();

        double lat = 32.8805071;
        double lng = -117.2365000;
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), (float) 14.9);
        map.moveCamera(cu);

        // be able to center the map on yourself
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);

        /* Adding existing markers */

        // Queries the latitudes and stores it into an Arraylist
        ParseQuery<ParseObject> latitudeQuery = ParseQuery.getQuery("currentFreeFoodsDB");
        latitudeQuery.selectKeys(Arrays.asList("LocationLat"));

        latitudeQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> locations, ParseException e)
            {
                // Log.d("score", locations.get(0).longitude());
                if (e == null) {

                    for (ParseObject temp : locations) {

                        try { // try to get some objects
                            tempLat.add(temp.getDouble("LocationLat"));
                        } catch (Exception ex) { // break out if failure
                            Log.d("score", "Error: " + ex.getMessage());
                            return; // return for now
                        }

                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

        // Queries longitudes and stores it into an array list
        ParseQuery<ParseObject> longitudeQuery = ParseQuery.getQuery("currentFreeFoodsDB");
        longitudeQuery.selectKeys(Arrays.asList("LocationLong"));

        longitudeQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> locations,
                             ParseException e) {
                // Log.d("score", locations.get(0).longitude());
                if (e == null) {

                    for (ParseObject temp : locations) {
                        try {
                            tempLat.add(temp.getDouble("LocationLong"));
                        } catch (Exception ex) { // break out if failure
                            Log.d("score", "Error: " + ex.getMessage());
                            break; // get out!!!
                        }           // an exception means we cannot add any markers
                    }

                    // After getting the longitude, proceeds to add markers, resolves the query background null Parse error
                    int j = 0;
                    while(j < tempLat.size() && j < tempLong.size()) { // need to make sure everything is the same size
                        Log.d("Location", "Retrieved " + locations.size());
                        Log.d("Location", "Here" + tempLat.get(j));
                        map.addMarker(new MarkerOptions().position(new LatLng(tempLat.get(j), tempLong.get(j))));
                        j++;
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });



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

                View v = getLayoutInflater(savedInstanceState).inflate(R.layout.event_view, null);

                ParseObject currentMarker = new ParseObject("currentFreeFoodsDB");


                currentMarker.put("LocationLat", marker.getPosition().latitude);
                currentMarker.put("LocationLong", marker.getPosition().longitude);

                currentMarker.put("DescriptionLocation", "current");


                currentMarker.saveInBackground();
                markers.add(marker.getPosition().latitude);
                markers.add(marker.getPosition().longitude);

                return v;
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

        if (id == R.id.maps_fab) { // if fab was hit
            addPin.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
        }
        else if (id == R.id.buttonCancel) { // if cancel button
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
                LatLng tmpLL = map.getCameraPosition().target;
                map.addMarker(new MarkerOptions().position(tmpLL));
            }

            /* do this stuff if cancelled as well (or after adding the marker) */

            // remove graphic
            addPin.setVisibility(View.GONE);

            // bring back the fab
            fab.setVisibility(View.VISIBLE);
        }
    }
}
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
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.Parse;
import com.parse.ParseObject;

import java.util.ArrayList;

public class MapTab extends Fragment implements View.OnClickListener {

    private MapView mapView;
    private GoogleMap map;
    private FloatingActionButton fab;
    private RelativeLayout addPin;
    private Button cancel, confirm;

    private ArrayList<Double> markerss = new ArrayList<>();
    private static final String TAG = "MyActivity";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.maps_tab,container,false);
        addPin = (RelativeLayout) v.findViewById(R.id.add_pin);

        /* BUTTONS */
        fab = (FloatingActionButton) v.findViewById(R.id.maps_fab);
        cancel = (Button) v.findViewById(R.id.buttonCancel);
        confirm = (Button) v.findViewById(R.id.buttonConfirm);

        fab.setOnClickListener(this);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);

        /** start up the map **/
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        map = mapView.getMap();
        // be able to center the map on yourself
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);

        /** center map on UCSD **/
        double lat = 32.8805071;
        double lng = -117.2365000;
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), (float) 14.9);
        map.moveCamera(cu);


        /* Adding existing markers */
        markerss.add(32.8800000);
        markerss.add(-117.2365000);
        markerss.add(50.0000000);
        markerss.add(50.0000000);
        markerss.add(32.8805000);
        markerss.add(-117.2367000);
        markerss.add(32.8801000);
        markerss.add(-117.2365000);
        int j = 0;
        while(j < markerss.size()){
            Log.d(TAG, "heyyyyy");
            map.addMarker(new MarkerOptions().position(new LatLng(markerss.get(j),
                    markerss.get(j + 1))).title("Melbourne")
                    .snippet("Population: 4,137,400").draggable(false));
            j = j+2;
        }

        /* set up marker dragging */
        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {/* do nothing */}

            @Override
            public void onMarkerDrag(Marker marker) {/* do nothing */}


            /* when done dragging, let's get the information going */
            @Override
            public void onMarkerDragEnd(Marker marker) {
                marker.setDraggable(false); // once you drop it; it isn't going anywhere (anti trolling?)
                // expanded tabs? or just solid window
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
                if (!marker.isDraggable())
                    return null;
                //else
                //View v = getLayoutInflater(savedInstanceState).inflate(R.layout.event_view, null);
                View v = getLayoutInflater(savedInstanceState)
                                .inflate(R.layout.event_view, null);

                //TextView dispText = (TextView) v.findViewById(R.id.event_info);

                TextView dispText = (TextView) v.findViewById(R.id.event_info);
                dispText.setText("Free Food Here!");

                //dispText.setText("Free Food Here!");

                ParseObject currentMarker = new ParseObject("currentFreeFoodsDB");
                currentMarker.put("LocationLat", marker.getPosition().latitude);
                currentMarker.put("LocationLong", marker.getPosition().longitude);
                currentMarker.saveInBackground();
                markerss.add(marker.getPosition().latitude);
                markerss.add(marker.getPosition().longitude);
                marker.setDraggable(false); // Sets draggable to false

                return v;
            }
        });

        return v;
    }

    /**
     * Check click events
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
            Intent intent = new Intent(getActivity().getApplicationContext(), ConfirmEventActivity.class);
            startActivityForResult(intent, 1);
        }
    }

    /**
     * Get a return result (most likely from the post dialog)
     * @param requestCode code of request
     * @param resultCode Activity.RESULT_OK == 1 and Activity.RESULT_CANCEL == 0
     * @param data intent that the result came from
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) // any data?
            return;

        Bundle bundle = data.getExtras();
        if (bundle == null) // check for sender
            return;

        String sender = data.getExtras().getString("SENDER"); //sender string

        if (sender.equals("ConfirmEventActivity"))
        {   // if intent comes from ConfirmEventActivity
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
}
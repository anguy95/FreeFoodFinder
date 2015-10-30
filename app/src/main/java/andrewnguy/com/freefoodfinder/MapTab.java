package andrewnguy.com.freefoodfinder;

/**
 * Created by anguy95 on 10/27/15.
 */

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.Parse;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class MapTab extends Fragment {

    ArrayList<Double> markerss = new ArrayList();

    private static final String TAG = "MyActivity";

    private MapView mapView;
    private GoogleMap map;
    private FloatingActionButton fab;
    private RelativeLayout addPin;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.maps_tab,container,false);
        addPin= (RelativeLayout) v.findViewById(R.id.add_pin);

        //Added FloatingActionButton for database code
        fab = (FloatingActionButton) v.findViewById(R.id.maps_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPin.setVisibility(View.VISIBLE);
                fab.setVisibility(View.GONE);

                //Added for database below
                LatLng tempLL = map.getCameraPosition().target;
                map.addMarker(new MarkerOptions()
                        .position(tempLL)
                        .draggable(true));
            }
        });


        Button cancel = (Button) v.findViewById(R.id.buttonCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
            }
        });



        Button confirm = (Button) v.findViewById(R.id.buttonConfirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);

                LatLng tmpLL = map.getCameraPosition().target;
                map.addMarker(new MarkerOptions().position(tmpLL));

                Intent intent = new Intent(getActivity().getApplicationContext(), ConfirmEventActivity.class);
                startActivity(intent);
            }
        });





        // start up the map
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        map = mapView.getMap();
        // able to center the map on yourself
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);

        // center map on UCSD
        double lat = 32.8805071;
        double lng = -117.2365000;
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), (float) 14.9);
        map.moveCamera(cu);


        /*        Adding existing markers */
        markerss.add(32.8800000);
        markerss.add(-117.2365000);
        markerss.add(50.0000000);
        markerss.add(50.0000000);
        markerss.add(32.8805000);
        markerss.add(-117.2367000);
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

        /* set up marker click listeners */
        /*map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                return true;
            }
        });*/

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
                View v =
                        getLayoutInflater(savedInstanceState)
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

    @Override
    public void onResume(){
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
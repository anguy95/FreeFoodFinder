package andrewnguy.com.freefoodfinder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseSession;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.jar.Manifest;


public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        View.OnClickListener,
        TextView.OnKeyListener {
    private final int DELAY = 10000; // 10 seconds (10,000 milliseconds)
    private int update = 2;          // 0 = updateMap, 1 = updateList, else updateBoth

    static EventArray ea; // the event array; for parse
    private ViewPager pager;
    static ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    private CharSequence Titles[] = {"Map", "List"};
    private int Numboftabs = 2;

    // searching/filtering/tagging
    ImageButton searchBtn, cancelBtn;
    private EditText tags_text;
    private static ArrayList<String> tags = new ArrayList<>();
    /* parse updates */
    private Handler h;
    private boolean stop;

    //UUID for username
    private String androidId;
    static ParseUser currentUser;
    static JSONArray likedEvents;
    static ArrayList<String> likedEventsList = new ArrayList<String>();
    static ArrayList<String> likedEventsListtoRemove = new ArrayList<String>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        ParseUser.enableAutomaticUser();

        // WIFI CHECK
//        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//        if (!wifi.isWifiEnabled()) { // if wifi is not enabled, ask to enable
//            // ask to enable wifi
//            AlertDialog.Builder alertWIFI = new AlertDialog.Builder(this);
//
//            alertWIFI
//                    .setTitle("Enable WIFI?")
//                    .setMessage("This app will not work correctly you are not connected to the internet is not enabled.")
//                    .setCancelable(false)
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent onWIFI = new Intent(Settings.ACTION_WIFI_SETTINGS);
//                            startActivity(onWIFI);
//                        }
//                    })
//                    .setNegativeButton("NO THANKS", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//
//            AlertDialog alert = alertWIFI.create();
//            alert.show();
//        }
        // LOCATION SERVICES CHECK
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { // if location services are not enabled, ask to enable
            AlertDialog.Builder alertGPS = new AlertDialog.Builder(this);

            alertGPS
                    .setTitle("Enable GPS?")
                    .setMessage("This app may not work correctly if location services are not enabled.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(onGPS);
                        }
                    })
                    .setNegativeButton("NO THANKS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertGPS.create();
            alert.show();
        }


        ea = new EventArray(this, getString(R.string.DB));

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // search and cancel button
        searchBtn = (ImageButton) findViewById(R.id.search_button);
        cancelBtn = (ImageButton) findViewById(R.id.search_cancel_button);
        searchBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        tags_text = (EditText) findViewById(R.id.search_bar); // get the edittext
        tags_text.setOnKeyListener(this);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);


            }
        });


        // Gets the current user

        try {
            currentUser = ParseUser.getCurrentUser();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        //If signed in from before Dank
        if (currentUser != null) {

            // This gets the users liked events to compare
            try {
                likedEvents = MainActivity.currentUser.getJSONArray("likedEvents");

            } catch (NullPointerException e) {
                Log.d("ParseError", e.getMessage());
            }
            if (likedEvents != null) {
                for (int i = 0; i < likedEvents.length(); i++) {
                    try {
                        likedEventsList.add(likedEvents.get(i).toString());
                    } catch (JSONException e) {
                        Log.d("JSONArray error", e.getMessage());
                    }
                }
            }
        }
        //Else signs up the user and logs them in
        else {

            currentUser = new ParseUser();
            currentUser.setUsername(androidId);
            currentUser.setPassword("planetext");
            currentUser.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {

                        ParseUser.logInInBackground(androidId, "planetext", new LogInCallback() {
                            public void done(ParseUser user, ParseException e) {
                                if (user != null) {
                                    // Hooray! The user is logged in.
                                } else {
                                    Log.d("Login Error", e.getMessage());
                                    // Signup failed. Look at the ParseException to see what happened.
                                }
                            }
                        });
                    } else {
                        Log.d("Signup Error", e.getMessage());
                        // to figure out what went wrong
                    }
                }
            });
        }

        /* Handler to update every DELAY seconds */
        stop = false;
        h = new Handler(); // the loop timer
        h.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!stop) { // check if we are stopped
                    ea.update(update);
                }
                h.postDelayed(this, DELAY);
            }
        }, DELAY); // activate the updater

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * search and cancel search buttons
     **/
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.search_button) {
            String searchTags = tags_text.getText().toString(); // get the filter parameters
            searchTags = searchTags.replace(":", " ").replace(",", " ").replace(";", " ");  // replace
            searchTags = searchTags.replace("-", " ").replace("_", " ").replace("/", " "); // tags splits
            String[] tagsArr = searchTags.split("\\s"); // get tags array to iterate through

            for (String aTagsArr : tagsArr) {
                tags.clear();
                tags.add(aTagsArr);
            }
        } else if (v.getId() == R.id.search_cancel_button) { // clear all tags
            tags_text.setText("");
            tags.clear();
        } else {
            return;
        }

        // put the keyboard away
        try {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        // update
        adapter.getMapTab().filter(tags);
        adapter.getListTab().update();
    }

    /**
     * press enter on search to send activate a search
     **/
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        // this will always be search bar so no need to check v.getId()
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) { // if enter was pressed,
            this.onClick(searchBtn);                       // search

            return true;
        }

        return false;
    }

    public static ArrayList<String> getTags() {
        return tags;
    }

    @Override
    public void onPause() {
        super.onPause();
        stop = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        stop = false;
    }

    @Override
    public void onDestroy() {
        stop = true;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { /* do nothing */ }

    @Override
    public void onPageSelected(int position) {
        if (position == 1) { // list view
            update = 0;
        } else {
            update = 2;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) { /* do nothing */ }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://andrewnguy.com.freefoodfinder/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://andrewnguy.com.freefoodfinder/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}

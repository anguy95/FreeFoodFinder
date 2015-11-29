package andrewnguy.com.freefoodfinder;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
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

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
                                                               View.OnClickListener,
                                                               TextView.OnKeyListener
{
    private final int DELAY = 10000; // 10 seconds (10,000 milliseconds)
    private int update = 2;          // 0 = updateMap, 1 = updateList, else updateBoth

    static EventArray ea; // the event array; for parse
    private ViewPager pager;
    static ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    private CharSequence Titles[]={"Map","List"};
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

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

        androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        // Gets the current user
        currentUser = ParseUser.getCurrentUser();

        //If signed in from before Dank
        if(currentUser != null){
            // This gets the users liked events to compare
            likedEvents = MainActivity.currentUser.getJSONArray("likedEvents");
            if (likedEvents != null) {
                int len = likedEvents.length();
                for (int i=0;i<len;i++){
                    try {
                        likedEventsList.add(likedEvents.get(i).toString());
                    }catch (JSONException e){
                        Log.d("JSONArray error", e.getMessage());
                    }
                }
            }
        }
        //Else signs up the user and logs them in
        else{
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
                                    // Signup failed. Look at the ParseException to see what happened.
                                }
                            }
                        });
                    } else {
                        // Sign up didn't succeed. Look at the ParseException
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
    }

    /** search and cancel search buttons **/
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
        }
        else if (v.getId() == R.id.search_cancel_button) { // clear all tags
            tags_text.setText("");
            tags.clear();
        }
        else { return; }

        // put the keyboard away
        try {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        catch (NullPointerException e) { e.printStackTrace(); }
        // update
        adapter.getMapTab().filter(tags);
        adapter.getListTab().update();
    }

    /** press enter on search to send activate a search **/
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        // this will always be search bar so no need to check v.getId()
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) { // if enter was pressed,
            this.onClick(searchBtn);                       // search

            return true;
        }

        return false;
    }

    public static ArrayList<String> getTags() { return tags; }

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
        }
        else {
            update = 2;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) { /* do nothing */ }
}

package andrewnguy.com.freefoodfinder;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener
{
    private final int DELAY = 10000; // 10 seconds (10,000 milliseconds)
    private int update = 2;          // 0 = updateMap, 1 = updateList, else updateBoth

    static EventArray ea; // the event array; for parse
    private ViewPager pager;
    static ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    private CharSequence Titles[]={"Map","List"};
    private int Numboftabs = 2;

    private Button searchBtn;
    private static ArrayList<String> tags = new ArrayList<>();

    /* parse updates */
    private Handler h;
    private boolean stop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ea = new EventArray(this, getString(R.string.DB));

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // search button
        searchBtn = (Button) findViewById(R.id.search_button);
        searchBtn.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search_button) {
            EditText editText = (EditText) findViewById(R.id.search_bar); // get the edittext
            String searchTags = editText.getText().toString(); // get the filter parameters
            searchTags = searchTags.replace(":", " ").replace(",", " ").replace(";", " ");  // replace
            searchTags = searchTags.replace("-", " ").replace("_", " ").replace("/", " "); // tags splits
            String[] tagsArr = searchTags.split("\\s"); // get tags array to iterate through

            for (int i = 0; i < tagsArr.length; i++)
            {
                tags.clear();
                tags.add(tagsArr[i]);
            }

            adapter.getMapTab().filter(tags);
            adapter.getListTab().update();
        }
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

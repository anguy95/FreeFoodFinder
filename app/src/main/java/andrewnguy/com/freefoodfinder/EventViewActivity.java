package andrewnguy.com.freefoodfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Class that allows us the see an event in detail
 * This should be called from clicking on an event from the ListTab
 * or clicking on "more information" from the MapsTab
 */
public class EventViewActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view);

        intent = getIntent(); // get intent that this came from: needs to know to go back to the Maps or List
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

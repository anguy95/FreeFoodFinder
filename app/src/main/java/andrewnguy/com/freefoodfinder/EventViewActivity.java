package andrewnguy.com.freefoodfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

/**
 * Class that allows us the see an event in detail
 * This should be called from clicking on an event from the ListTab
 * or clicking on "more information" from the MapsTab
 */
public class EventViewActivity extends AppCompatActivity {
    private Intent intent;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_view);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        intent = getIntent(); // get intent that this came from: needs to know to go back to the Maps or List
        Bundle extras = getIntent().getExtras();

        TextView viewTitle = (TextView) findViewById(R.id.event_title);
        viewTitle.setText(extras.getString("eventTitle"));
        TextView viewDesc = (TextView) findViewById(R.id.event_description);
        viewDesc.setText(extras.getString("eventDesc"));

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

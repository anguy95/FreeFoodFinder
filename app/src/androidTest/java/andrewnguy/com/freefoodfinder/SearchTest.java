package andrewnguy.com.freefoodfinder;

import android.support.test.espresso.core.deps.guava.collect.ArrayListMultimap;
import android.support.test.runner.AndroidJUnit4;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


/** SCENARIO:
 *
 *  Given there are existing events
 *  And the user is in the Map Tab
 *  When the user enters a string into the Search bar
 *  And hits the Search button
 *  Then the map will display events that match the search.
 */

@RunWith(AndroidJUnit4.class)
public class SearchTest {

    @Rule
    //Given there are existing events
    //And the user is in the Map Tab (MainActivity)
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    /**
     * SCENARIO
     **/
    @Test
    public void ensureSearchFunction() {


        //When the user enters a string into the Search bar
        //And hits the Search button
        String tagTest = "Warren";

        onView(withId(R.id.search_bar)).perform(typeText(tagTest), closeSoftKeyboard());
        onView(withId(R.id.search_button)).perform(click());

        //Then the map will display events that match the search
        EventArray temp = MainActivity.ea;
        ArrayList<Event> eventList = temp.getEventArray();

        for (Event e : eventList) {
            String title = e.getTitle();
            String tags = e.getTags();

            if (title.toLowerCase().contains(tagTest.toLowerCase()) || tags.toLowerCase().contains(tagTest.toLowerCase()))
                continue;

            Log.e("FAILURE", "Found an event from search results without a matching tag");
            return;
        }

        Log.i("Success", "Success");
    }

}

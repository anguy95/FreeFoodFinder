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


/** SCENARIO 2:
 *
 *  Given there are existing events
 *  And the user is in the Map Tab
 *  And the user enters a string into the Search bar
 *  And hits the Search button
 *  And the map will display events that match the search
 *  When the user hits the cancel button
 *  Then the map will display all the events.
 */
@RunWith(AndroidJUnit4.class)
public class TestSearchClear {

    @Rule
    //Given there are existing events
    //And the user is in the Map Tab (MainActivity)
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);



    //Given there are existing events
    //And the user is in the Map Tab (MainActivity)
    /** SCENARIO 2 **/
    @Test
    public void testClearSearch(){

        //get full list
        EventArray temp = MainActivity.ea;
        ArrayList<Event> eventList = temp.getEventArray();

        //and the user enters a string into the Search bar
        //and hits the Search button

        String tagTest = "Warren";

        onView(withId(R.id.search_bar)).perform(typeText(tagTest), closeSoftKeyboard());
        onView(withId(R.id.search_button)).perform(click());

        //and the map will display events that match the search
        ArrayList<Event> newEventList = temp.getEventArray(); // assume correct based on earlier test

        //when the user hits the cancel button
        onView(withId(R.id.search_cancel_button)).perform(click());

        //then the map will display all the events.
        ArrayList<Event> finalEventList = temp.getEventArray();

        //check for same lengths
        if (eventList.size() != finalEventList.size()) {
            Log.e("FAILURE", "Search clearing unsuccessful");
            return;
        }

        Log.i("Success", "Success");
    }
}

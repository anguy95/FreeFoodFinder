package andrewnguy.com.freefoodfinder;

import android.support.test.runner.AndroidJUnit4;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


/**
 * Class that checks for a working search function
 */
@RunWith(AndroidJUnit4.class)
public class SearchTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void ensureSearchFunction(){
        //Type a search word into the searchbar
        onView(withId(R.id.search_bar)).perform(typeText("Warren"), closeSoftKeyboard());
        onView(withId(R.id.search_button)).perform(click());
    }


    @Test
    public void testGibberish(){
        onView(withId(R.id.search_bar)).perform(typeText("afskjfa" +
                ""), closeSoftKeyboard());
        onView(withId(R.id.search_button)).perform(click());
    }

    @Test
    public void testClearSearch(){
        onView(withId(R.id.search_bar)).perform(typeText("Track"), closeSoftKeyboard());
        onView(withId(R.id.search_cancel_button)).perform(click());

    }
}

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
 * Class that tests if we can add an event to the map
 */
@RunWith(AndroidJUnit4.class)
public class TestOpenConfirmEvent {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);


    @Test
    public void ensureConfirmEventOpensOnConfirm(){
        onView(withId(R.id.maps_tab_fab)).perform(click());
        onView(withId(R.id.add_event_pin_confirm_button)).perform(click());
    }

    @Test
    public void ensureConfirmEventClosesOnCancel(){
        onView(withId(R.id.maps_tab_fab)).perform(click());
        onView(withId(R.id.add_event_pin_cancel_button)).perform(click());
    }

    @Test
    public void makeSureConfirmWorkingDisplays(){
        onView(withId(R.id.maps_tab_fab)).perform(click());
        onView(withId(R.id.add_event_pin_confirm_button)).perform(click());
        onView(withId(R.id.confirm_event_activity_confirm_button)).perform(click());
    }


}

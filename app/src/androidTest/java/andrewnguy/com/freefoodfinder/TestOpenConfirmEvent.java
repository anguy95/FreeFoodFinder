package andrewnguy.com.freefoodfinder;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;





/** SCENARIO
 * Given that the user is attempting to make an Event
 * and they are on the Event input page and forgets to fill out fields
 * When the user clicks confirm
 * Then an error message will appear saying they missed some inputs
 *
 */

@RunWith(AndroidJUnit4.class)
public class TestOpenConfirmEvent {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);


    @Test
    public void makeSureConfirmWorkingDisplays(){

        //Given that the user is attempting to make an Event
        //and they are on the Event input page and forgets to fill out fields
        onView(withId(R.id.maps_tab_fab)).perform(click());
        onView(withId(R.id.add_event_pin_confirm_button)).perform(click());

        //When the user clicks confirm
        //Then an error message will appear
        onView(withId(R.id.confirm_event_activity_confirm_button)).perform(click());
    }


}

package andrewnguy.com.freefoodfinder;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseCrashReporting;

/**
 * This class is used to initialize the parse database and enable the desired parse related functions
 */
public class ParseApplication extends Application {
    /**
     * Create the Parse
     */
    @Override
    public void onCreate() {
        super.onCreate();
        ParseCrashReporting.enable(this); // parse crash reporting
        Parse.enableLocalDatastore(this); // enable before initialize

        Parse.initialize(this, "mX3zY148IWfdhd3QgDjIETqjG8yMM8D9vgZM5VVN", "mm63b6oFNYRyYdyppHjwkX1hinyXePB97FwlfrCw");
    }
}

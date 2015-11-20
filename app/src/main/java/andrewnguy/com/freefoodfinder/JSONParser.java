package andrewnguy.com.freefoodfinder;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mac on 11/19/15.
 */
public class JSONParser {

    private JSONObject jobj;

    public JSONParser(String stringToParse) throws JSONException {
        jobj = new JSONObject(stringToParse);
    }

    public String parse(String find) throws JSONException {

        String result = "";

        JSONArray jarr = jobj.getJSONArray("markers"); // break down the markers

        Log.e("count", Integer.toString(jobj.length()));
        Log.e("count", Integer.toString(jarr.length()));

        // TODO shuffle through the markers and search for the "find" string (most likely location)

        return result;
    }
}

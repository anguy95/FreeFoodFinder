package andrewnguy.com.freefoodfinder;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that searches through a JSONObject
 */
public class JSONParser {

    private JSONObject jobj;

    public JSONParser(String stringToParse) throws JSONException {
        jobj = new JSONObject(stringToParse);
    }

    /**
     * This class is used for the LocationSetter so just needs to find the best guess location
     * based on where a marker was placed on the map
     * @param find key to find
     * @return value of the find
     * @throws JSONException
     */
    public String parse(String find) throws JSONException {

        JSONArray jarr = jobj.getJSONArray("markers"); // break down the markers

        Log.e("count", jarr.getJSONObject(0).getString(find));

        return jarr.getJSONObject(0).getString(find);
    }
}

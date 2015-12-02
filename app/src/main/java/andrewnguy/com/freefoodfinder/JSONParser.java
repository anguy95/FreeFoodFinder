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

        JSONArray jarr = jobj.getJSONArray("markers"); // break down the markers

        Log.e("count", jarr.getJSONObject(0).getString(find));

        return jarr.getJSONObject(0).getString(find);
    }
}

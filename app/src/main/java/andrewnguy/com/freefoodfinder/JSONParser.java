package andrewnguy.com.freefoodfinder;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mac on 11/19/15.
 */
public class JSONParser {

    private String str = "";

    public JSONParser(String stringToParse) {
        this.str = stringToParse;
    }

    public String parse(String find) throws JSONException {

        String result = "";

        JSONObject jobj = new JSONObject(str); //create the JSON object
        JSONArray jarr = jobj.getJSONArray("markers"); // break down the markers

        Log.e("count", Integer.toString(jobj.length()));
        Log.e("count", Integer.toString(jarr.length()));

        return result;
    }
}

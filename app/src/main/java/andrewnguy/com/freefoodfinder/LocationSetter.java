package andrewnguy.com.freefoodfinder;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mac on 11/18/15.
 */
public class LocationSetter extends AsyncTask<String, String, String> {


    // for ucsd maps API
    private final String APP_KEY = "yA1qdB9d1elQ5rhz12h9Q1ZKxlMa";
    private final String APP_SECRET = "6XcIqzIgbuP8hGhf959MLF8pQPMa";
    private final String BEARER = "9ce2bec3b44235681badd6568bf83a8e";

    private final String BASE_LINK = "https://api-qa.ucsd.edu:8243/location/v1/buildings?";
    private final String BLDGS = "groupId=1241268398815896%2C18%2C60%2C14%2C15%2C16%2C17%2C19"; // all buildings UCSD
    private final int FEET = 200;


    private WeakReference<TextView> textViewWeakReference;
    private String str = "";

    public LocationSetter(TextView textView) {
        textViewWeakReference = new WeakReference<>(textView);
    }

    @Override
    protected String doInBackground(String... link)
    {
        try {
            String requestLink = BASE_LINK + link[0] + "&" + BLDGS + "&feet=" + Integer.toString(FEET);

            Log.e("url", requestLink);

            // try the request
            URL url = new URL(requestLink);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // default is GET
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + BEARER);

            // read response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            // close everything
            in.close();
            connection.disconnect();

            // breakdown the JSON
            JSONParser jp = new JSONParser(response.toString());
            str = jp.parse("location");

        }
        catch (IOException e) {
            Log.e("I/O error", "not run");
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e("JSON error", "not run");
            e.printStackTrace();
        }

        return str;
    }

    @Override
    protected void onPostExecute(String string)
    {
        TextView textView = textViewWeakReference.get();

        textView.setText(str, TextView.BufferType.NORMAL);
    }
}

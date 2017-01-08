package shaked.aviad.aviv.myfinalconverter.internet;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import shaked.aviad.aviv.myfinalconverter.app.AppController;
import shaked.aviad.aviv.myfinalconverter.module.custom_objects.ExchangePlaceObject;
import shaked.aviad.aviv.myfinalconverter.module.listeners.DownloadMapInformationListener;

/**
 * Created by hackeru on 06/04/2016.
 */
public class VolleyReq {


    // Server Key = AIzaSyBNtQ_1WLls3Xi74kqmI7EDonnczISq24M

    private String placeName, placeAddress;
    private double placeLat, placeLng;
    private Context context;
    private DownloadMapInformationListener downloadListener;
    private JSONArray jsonArrayResults;
    private ArrayList<ExchangePlaceObject> listPlaces;

    public VolleyReq(Context context, DownloadMapInformationListener downloadListener) {
        this.context = context;
        this.downloadListener = downloadListener;
    }

    public void listPlacesCreate(double lat, double lng) {

       String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lng + "&radius=6000&type=finance&name=exchange&rankBY=distance&key=AIzaSyCi3vhDlL-oEc1knzAVa6RIMZpdcsghe2g";
        // String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=40.683085,-73.973676&radius=6000&type=finance&name=exchange&rankBY=distance&key=AIzaSyCi3vhDlL-oEc1knzAVa6RIMZpdcsghe2g";


        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("mo", response.toString());
                        try {
                            listPlaces = new ArrayList<>();
                            JSONObject indexJsonObject;
                            JSONObject locationJsonObject;
                            JSONObject geometryJsonObject;
                            ExchangePlaceObject currentPlace;


                            jsonArrayResults = response.getJSONArray("results");

                            for (int i = 0; i < jsonArrayResults.length(); i++) {
                                indexJsonObject = jsonArrayResults.getJSONObject(i);
                                geometryJsonObject = indexJsonObject.getJSONObject("geometry");
                                locationJsonObject = geometryJsonObject.getJSONObject("location");

                                placeLat = locationJsonObject.getDouble("lat");
                                placeLng = locationJsonObject.getDouble("lng");
                                placeName = indexJsonObject.getString("name");
                                placeAddress = indexJsonObject.getString("vicinity");
                                currentPlace = new ExchangePlaceObject(placeName, placeLat, placeLng, placeAddress);

                                listPlaces.add(currentPlace);
                            }

                            Log.d("latlng", placeLat + " lng " + placeLng + "");


                            downloadListener.onDoneDownload(listPlaces);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context,
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(getClass().getName(), "Error: " + error.getMessage());
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        AppController.getInstance().addToRequestQueue(req);
    }


}

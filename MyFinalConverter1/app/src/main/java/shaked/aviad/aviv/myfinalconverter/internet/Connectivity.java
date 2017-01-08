package shaked.aviad.aviv.myfinalconverter.internet;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import shaked.aviad.aviv.myfinalconverter.module.listeners.DownloadInformationListener;


public class Connectivity {
    private Context context;
    private DownloadInformationListener downloadListener;

    public Connectivity(Context context, DownloadInformationListener downloadListener) {
        this.context = context;
        this.downloadListener = downloadListener;
    }

    public void currencyConverter() {

        String url = "http://www.mycurrency.net/service/rates";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray;
                JSONObject jsonObject;
                HashMap hashMap = new HashMap();
                try {
                    jsonArray = new JSONArray(response);

                    Log.d("valueFrom ", response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);

                        hashMap.put(jsonObject.getString("currency_code"),jsonObject.getString("rate"));

                    }

Log.d("realsize", jsonArray.length()-1 + "");
                    downloadListener.onDoneConvert(hashMap);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 4, 2));

        Volley.newRequestQueue(context).add(stringRequest);


    }


}





package shaked.aviad.aviv.myfinalconverter.controller_activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import shaked.aviad.aviv.myfinalconverter.R;
import shaked.aviad.aviv.myfinalconverter.app.Singleton;
import shaked.aviad.aviv.myfinalconverter.internet.Connectivity;
import shaked.aviad.aviv.myfinalconverter.module.custom_objects.Country;
import shaked.aviad.aviv.myfinalconverter.module.listeners.DownloadInformationListener;

/**
 * Created by Aviv on 17/03/2016.
 */
public class SplashScreenActivity extends AppCompatActivity implements DownloadInformationListener{

    private ArrayList<Country> countriesArr = null;
    private final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 2300;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        checkPermission();
        initSingletons();
        initArrCurrenciesValues();
        convertTextToArr();
        Singleton.getInstance().setArrCountries(countriesArr);
        setContentView(R.layout.splash_screen);
        final ImageView imgSplash = (ImageView) findViewById(R.id.imgSplashMarker);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imgSplash.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.Wave).duration(1800).playOn(imgSplash);
                YoYo.with(Techniques.FadeIn).duration(1000).playOn(imgSplash);

            }
        },200);
        runMainActivity();


        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initArrCurrenciesValues() {
        Connectivity connectivity = new Connectivity(this,this);
        connectivity.currencyConverter();
    }

    private boolean runMainActivity() {

        if(isAppReady()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
                    SplashScreenActivity.this.finish();
                }

            }, 2000);
        }else{
            return  runMainActivity();
        }
        return true;
    }

    private void convertTextToArr() {
        try {
            JSONObject jsonObject = new JSONObject(ReadFromFile());
            JSONArray jsonArray = jsonObject.getJSONArray("countries");
            countriesArr = new ArrayList<>();
            Country currentCountry;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonIndex = jsonArray.getJSONObject(i);
                Log.d("Details-->", jsonIndex.getString("countryName") + i);
                String countryName = jsonIndex.getString("countryName");
                String countryCode = jsonIndex.getString("countryCode");
                String currencyCode = jsonIndex.getString("currencyCode");

                //Add your values in your `ArrayList` as below:
                currentCountry = new Country(countryName, currencyCode, countryCode);

                countriesArr.add(currentCountry);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("mo", countriesArr.get(66).getCountryName());
    }


    protected void initSingletons() {
        Singleton.initInstance();
    }


    private String ReadFromFile() {
        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = getApplicationContext().getAssets().open("countries_information.txt");
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line = "";
            while ((line = input.readLine()) != null) {
                returnString.append(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (isr != null)
                    isr.close();
                if (fIn != null)
                    fIn.close();
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return returnString.toString();
    }

    public void checkPermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_ACCESS_COARSE_LOCATION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Good job you said yes to location", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SplashScreenS Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://shaked.aviad.aviv.myfinalconverter/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SplashScreen Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://shaked.aviad.aviv.myfinalconverter/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private boolean isAppReady(){
       if(countriesArr != null  && ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
           return true;
       }
        return false;
    }

    @Override
    public void onDoneConvert(HashMap hmCurrenciesValues) {
        Log.d("nowthis", hmCurrenciesValues.get("AUD")+"");
Singleton.getInstance().setHmCurrenciesValues(hmCurrenciesValues);

     /*   Singleton.getInstance().setArrCurrenciesValues(arrValues);
       Log.d("valueof ", arrValues.get(100).getCurrencyCode());
       Log.d("valueof ", arrValues.size()+"");*/
    }
}
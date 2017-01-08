package shaked.aviad.aviv.myfinalconverter.controller_activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import shaked.aviad.aviv.myfinalconverter.R;
import shaked.aviad.aviv.myfinalconverter.app.Singleton;
import shaked.aviad.aviv.myfinalconverter.module.custom_objects.Country;
import shaked.aviad.aviv.myfinalconverter.module.custom_objects.NonSwipeableViewPager;
import shaked.aviad.aviv.myfinalconverter.module.custom_objects.SampleFragmentPagerAdapter;
import shaked.aviad.aviv.myfinalconverter.module.enums.SIDE;
import shaked.aviad.aviv.myfinalconverter.module.listeners.ListenerClickableBtnsFragCurrency;
import shaked.aviad.aviv.myfinalconverter.module.listeners.ListenerLoadCountries;
import shaked.aviad.aviv.myfinalconverter.module.listeners.OnSwipeListener;
import shaked.aviad.aviv.myfinalconverter.view_fragments.FragmentChart;
import shaked.aviad.aviv.myfinalconverter.view_fragments.FragmentCurrencyConverter;
import shaked.aviad.aviv.myfinalconverter.view_fragments.FragmentMap;

public class MainActivity extends AppCompatActivity implements OnSwipeListener, ListenerClickableBtnsFragCurrency, ListenerLoadCountries {

    private final String FAVORITE_COUNTRY_KEY = "fav_key";
    private final String LAST_COUNTRIES = "countries_key";

    private FragmentChart fragmentChart;
    private FragmentCurrencyConverter fragmentCurrencyConverter;
    private FragmentMap fragmentMap;
    private String leftCurrency;
    private String rightCurrency;
    private Country leftCountry, rightCountry;
    private ArrayList<Country> fullCountries;
    private HashMap hmCurrenciesValues;
    private int screenWidth, screenHeight;
    private ArrayList<String> arrEURCurrencies;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            setFavoritesCountries();
            initAdView();
            initArrCurrenciesValues();
            DisplayMetrics metrics = this.getResources().getDisplayMetrics();
//We get width and height in pixels here
             screenWidth = metrics.widthPixels;
             screenHeight = metrics.heightPixels;
            initFragmentCurrencyConverter();
            try {
                loadCountryFromPreferences();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            initFragmentMap();

            initTabViewPager();
            fragmentChart = new FragmentChart();


            if (isFirstTime()) {
                fragmentCurrencyConverter.setFirstTimeRun(true);
            }
        }

    }

    private void initArrCurrenciesValues() {
        hmCurrenciesValues = Singleton.getInstance().getHmCurrenciesValues();
        arrEURCurrencies = new ArrayList<String>();
        for (int i = 0; i < fullCountries.size(); i++) {
          if(fullCountries.get(i).getCurrencyCode().equalsIgnoreCase("EUR")){
              arrEURCurrencies.add(fullCountries.get(i).getCountryName());
          }
        }
    }

    private void initTabViewPager() {
        NonSwipeableViewPager viewPager = (NonSwipeableViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(), fragmentCurrencyConverter, fragmentMap));
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        Typeface fontFamily = Typeface.createFromAsset(this.getAssets(), "fonts/fontawesome-webfont.ttf");
        tabsStrip.setTextSize(90);
        tabsStrip.setTypeface(fontFamily, 0);
        tabsStrip.setViewPager(viewPager);
        tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    fragmentMap.initMap();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initAdView() {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4666791944006485~5695702252");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        /*AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);*/
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void setFavoritesCountries() {
        Log.d(getClass().getName(), "setFavoritesCountries is running");
        try {
            fullCountries = Singleton.getInstance().getArrCountries();
            ArrayList<Country> favoritesCountries = loadFavorite();


            for (int i = 0; i < favoritesCountries.size(); i++) {

                for (int a = 0; a < fullCountries.size(); a++) {
                    if (favoritesCountries.get(i).getImgCode().equals(fullCountries.get(a).getImgCode())) {
                        fullCountries.get(a).setIsFavorite(true);
                        a = fullCountries.size();
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            Log.d("finallyMain", "sure?0");
        }
        return super.dispatchKeyEvent(event);
    }

    private void initFragmentMap() {
        fragmentMap = new FragmentMap();
    }

    private void initFragmentCurrencyConverter() {
        fragmentCurrencyConverter = new FragmentCurrencyConverter();
        fragmentCurrencyConverter.setSwipeListener(this);
        fragmentCurrencyConverter.setListenerLoadCountries(this);
        fragmentCurrencyConverter.setListenerClickableBtnsFragCurrency(this);

    }

    @Override
    public void showCountryBox(SIDE side, Country country, boolean firstInitialize) {
        if (firstInitialize) {
            fragmentCurrencyConverter.firstInitCountryBox(side, country.getCountryName(), country.getImgCode());
        } else {
            fragmentCurrencyConverter.initCountryBox(side, country.getCountryName(), country.getImgCode());
        }
        if (side == SIDE.LEFT) {

            leftCurrency = country.getCurrencyCode();
            leftCountry = country;
            Log.d("leftcountry ", leftCountry.getCountryName());
        } else if (side == SIDE.RIGHT) {
            rightCurrency = country.getCurrencyCode();
            rightCountry = country;
        }
        if (leftCurrency != null & rightCurrency != null) {
            fragmentChart.setCurrencies(leftCurrency, rightCurrency, leftCountry.getCountryName(), rightCountry.getCountryName());
            double value = getValueOf(leftCurrency,rightCurrency);
            fragmentCurrencyConverter.setNewConvertUnitResults(value);
            fragmentCurrencyConverter.displayResultInOneUnit(leftCurrency,rightCurrency,value);
        }
    }

    private double getValueOf(String leftCurrencyCode, String rightCurrencyCode) {
        for (int i = 0; i < arrEURCurrencies.size(); i++) {
            if(leftCurrencyCode.equalsIgnoreCase(arrEURCurrencies.get(i))){
                leftCurrencyCode = "EUR";
            }
            if(rightCurrencyCode.equalsIgnoreCase(arrEURCurrencies.get(i))){
                rightCurrencyCode = "EUR";
            }
        }
        double leftValue = Double.parseDouble(hmCurrenciesValues.get(leftCurrencyCode)+"");
        double rightValue = Double.parseDouble(hmCurrenciesValues.get(rightCurrencyCode)+"");
        Log.d("leftV", leftValue+"");
       return rightValue/leftValue;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(this.getClass().getName(), "back button pressed");
        }
        return false;

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    @Override
    protected void onStop() {
        saveFavCountriesList();
        saveLastCountries();
        super.onStop();
    }

    @Override
    protected void onResume() {
        //TODO load
        super.onResume();
    }

    private void loadCountryFromPreferences() throws JSONException {
        String countriesStr = getLoadedCountryAsString();
        Log.d("countriesSTR ", countriesStr);
        JSONArray jsonArray;
        if (!countriesStr.equals("")) {
            jsonArray = new JSONArray(countriesStr);
            leftCountry = convertCodeToCountry(jsonArray.getString(0));
            rightCountry = convertCodeToCountry(jsonArray.getString(1));

        } else {

            leftCountry = fullCountries.get(45);
            rightCountry = fullCountries.get(136);
        }

    }

    private Country convertCodeToCountry(String countryCode) {
        for (int i = 0; i < fullCountries.size(); i++) {
            if (countryCode.equals(fullCountries.get(i).getImgCode())) {
                return fullCountries.get(i);
            }
        }
        return null;
    }

    private String getLoadedCountryAsString() {

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        return sharedPreferences.getString(LAST_COUNTRIES, "");
    }

    private ArrayList<Country> loadFavorite() throws JSONException {
        ArrayList<Country> favoriteCountryList = new ArrayList<>();

        String favCountryListSTR = getLoadedFavCountriesListAsString();
        JSONArray jsonArray;
        if (!favCountryListSTR.equals("")) {
            jsonArray = new JSONArray(favCountryListSTR);
        } else {
            jsonArray = new JSONArray();
        }


        for (int i = 0; i < jsonArray.length(); i++) {
            String countryCode = jsonArray.getString(i);
            Country country = new Country(countryCode);
            favoriteCountryList.add(country);
        }

        return favoriteCountryList;
    }

    private String getLoadedFavCountriesListAsString() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        return sharedPreferences.getString(FAVORITE_COUNTRY_KEY, "");
    }


    private void saveFavCountriesList() {

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String favListJSONstr = getFavListAsStringJSON();
        editor.putString(FAVORITE_COUNTRY_KEY, favListJSONstr);
        editor.apply();
    }

    private void saveLastCountries() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String lastCountries = getLastCountriesAsStringJSON();
        editor.putString(LAST_COUNTRIES, lastCountries);
        editor.apply();
    }

    private String getLastCountriesAsStringJSON() {
        JSONArray lastCountriesJSONArray = new JSONArray();
        lastCountriesJSONArray.put(leftCountry.getImgCode());
        lastCountriesJSONArray.put(rightCountry.getImgCode());
        return lastCountriesJSONArray.toString();
    }


    public String getFavListAsStringJSON() {
        ArrayList<Country> fullCountriesList = Singleton.getInstance().getArrCountries();
        JSONArray favCountriesList = new JSONArray();
        for (int i = 0; i < fullCountriesList.size(); i++) {
            if (fullCountriesList.get(i).isFavorite()) {
                favCountriesList.put(fullCountriesList.get(i).getImgCode());
            }
        }
        return favCountriesList.toString();
    }


    @Override
    public void initChartDialogFragment(float xGraphBtn, float yGraphBtn) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentChart.show(fragmentManager, "Dialog");
    }

    @Override
    public void btnSwitchCountriesPressed() {
        fragmentCurrencyConverter.switchCountriesBox(leftCountry, rightCountry);
        if (leftCurrency != null & rightCurrency != null) {
            fragmentChart.setCurrencies(leftCurrency, rightCurrency, leftCountry.getCountryName(), rightCountry.getCountryName());
            double value = getValueOf(rightCurrency,leftCurrency);
            fragmentCurrencyConverter.setNewConvertUnitResults(value);
            fragmentCurrencyConverter.displayResultInOneUnit(rightCurrency,leftCurrency,value);

        }
        String tempLeftCurrency = leftCurrency;
        Country tempLeftCountry = leftCountry;
        leftCurrency = rightCurrency;
        rightCurrency = tempLeftCurrency;
        leftCountry = rightCountry;
        rightCountry = tempLeftCountry;
    }

    @Override
    public void editTextInitNewAmount() {
      fragmentCurrencyConverter.setNewConvertUnitResults(getValueOf(leftCurrency,rightCurrency));
    }

    @Override
    public void OnDoneLoadCountries() {
        showCountryBox(SIDE.LEFT, leftCountry, true);
        showCountryBox(SIDE.RIGHT, rightCountry, true);
    }

    private boolean isFirstTime() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.apply();
        }
        return !ranBefore;
    }

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 22) {
            Log.d("sdk", version + "");
            return ContextCompat.getColor(context, id);
        } else {
            Log.d("sdk", version + "");
            return context.getResources().getColor(id, null); // TODO may cause problam
        }
    }


}


package shaked.aviad.aviv.myfinalconverter.app;

import java.util.ArrayList;
import java.util.HashMap;

import shaked.aviad.aviv.myfinalconverter.module.custom_objects.Country;

/**
 * Created by Aviv on 29/03/2016.
 */
public class Singleton {

    private static Singleton instance = null;

    private ArrayList<Country> arrCountries;
    private HashMap hmCurrenciesValues;

    private Singleton() {

    }
    public static Singleton initInstance()
    {
        if (instance == null)
        {
            // Create the instance
            instance = new Singleton();
        }
        return instance;
    }

    public static Singleton getInstance()
    {
        return instance;
    }

    public ArrayList<Country> getArrCountries() {
        return this.arrCountries;
    }

    public void setArrCountries(ArrayList<Country> arrCountries) {
        this.arrCountries = arrCountries;
    }

    public HashMap getHmCurrenciesValues() {
        return hmCurrenciesValues;
    }

    public void setHmCurrenciesValues(HashMap hmCurrenciesValues) {
        this.hmCurrenciesValues = hmCurrenciesValues;
    }
}

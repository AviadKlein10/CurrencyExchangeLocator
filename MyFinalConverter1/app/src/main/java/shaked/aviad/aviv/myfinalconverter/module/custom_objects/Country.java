package shaked.aviad.aviv.myfinalconverter.module.custom_objects;

/**
 * Created by Aviv on 15/03/2016.
 */
public class Country {

    private String countryName;
    private String currencyCode;
    private String imgCode;
    private boolean isFavorite;

    public Country(String countryName, String currencyCode, String imgCode) {
        this.countryName = countryName;
        this.currencyCode = currencyCode;
        this.imgCode = imgCode;
        this.isFavorite = false;
    }
    public Country(String imgCode) {
        this.imgCode = imgCode;
        this.isFavorite = true;
    }


    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getImgCode() {
        return imgCode;
    }

    public void setImgCode(String imgCode) {
        this.imgCode = imgCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}


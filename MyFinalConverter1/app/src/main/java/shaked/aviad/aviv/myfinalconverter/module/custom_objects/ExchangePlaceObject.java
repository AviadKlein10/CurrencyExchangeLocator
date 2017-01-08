package shaked.aviad.aviv.myfinalconverter.module.custom_objects;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by hackeru on 06/04/2016.
 */
public class ExchangePlaceObject {

     private LatLng placeLatLng;
    private String placeName, placeAddress , isOpen;


    public ExchangePlaceObject(String placeName, double lat, double lng, String placeAddress) {
        this.placeName = placeName;
        this.placeLatLng = new LatLng(lat,lng);
        this.placeAddress = placeAddress;
    }

    public ExchangePlaceObject(String placeName, double lat, double lng) {
        this.placeLatLng = new LatLng(lat,lng);
        this.placeName = placeName;
    }

    public ExchangePlaceObject(String placeName, double placeLat, double placeLng, String placeAddress, String isOpen) {
        this.placeName = placeName;
        this.placeLatLng = new LatLng(placeLat,placeLng);
        this.placeAddress = placeAddress;
        if(isOpen.equals("true")){
            this.isOpen = "Open Now";
        }else{
            this.isOpen = "Isn't Open Now";
        }
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public LatLng getPlaceLatLng() {
        return placeLatLng;
    }

    public void setPlaceLatLng(LatLng placeLatLng) {
        this.placeLatLng = placeLatLng;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }
}

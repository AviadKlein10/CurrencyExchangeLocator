package shaked.aviad.aviv.myfinalconverter.module.listeners;

import java.util.ArrayList;

import shaked.aviad.aviv.myfinalconverter.module.custom_objects.ExchangePlaceObject;

/**
 * Created by hackeru on 06/04/2016.
 */
public interface DownloadMapInformationListener {
   void onDoneDownload(ArrayList<ExchangePlaceObject> listPlaces);
}

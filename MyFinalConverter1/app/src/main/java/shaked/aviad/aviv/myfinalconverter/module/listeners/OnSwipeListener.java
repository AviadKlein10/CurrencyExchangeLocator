package shaked.aviad.aviv.myfinalconverter.module.listeners;

import shaked.aviad.aviv.myfinalconverter.module.custom_objects.Country;
import shaked.aviad.aviv.myfinalconverter.module.enums.SIDE;

/**
 * Created by Aviv on 29/03/2016.
 */
public interface OnSwipeListener {
    void showCountryBox(SIDE side, Country country,boolean firstInitialize);
}

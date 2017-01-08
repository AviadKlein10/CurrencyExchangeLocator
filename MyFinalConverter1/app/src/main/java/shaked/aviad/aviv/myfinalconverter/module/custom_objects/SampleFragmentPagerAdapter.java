package shaked.aviad.aviv.myfinalconverter.module.custom_objects;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Aviv on 02/05/2016.
 */
public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private CharSequence tabTitles[] = {"\uf0ac","\uf041"};
    private Fragment fragmentCurrencyConverter, fragmentMap;

    public SampleFragmentPagerAdapter(FragmentManager fm, Fragment fragmentCurrencyConverter, Fragment fragmentMap) {
        super(fm);
        this.fragmentCurrencyConverter = fragmentCurrencyConverter;
        this.fragmentMap = fragmentMap;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return fragmentCurrencyConverter;

            case 1:
                return fragmentMap;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
            return tabTitles[position];
    }

}

package shaked.aviad.aviv.myfinalconverter.module.custom_objects;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import shaked.aviad.aviv.myfinalconverter.R;
import shaked.aviad.aviv.myfinalconverter.app.Singleton;

/**
 * Created by Aviv on 15/03/2016.
 */
public class CountryListAdapter2 extends BaseAdapter {

    private ArrayList<Country> fullCountryList;
    private ArrayList<Country> customCountryList;
    private Context context;
    private boolean doAnimation;

    public CountryListAdapter2(Context context, ArrayList<Country> fullCountryList) {
        this.fullCountryList = fullCountryList;
        this.customCountryList = fullCountryList;
        this.context = context;

    }

    public class ViewHolder {

        private TextView txtCountryName, starFav;
        private String txtCurrencyCode;
        private ImageView imgCountry;
        private boolean isFavorite;
    }

    @Override
    public int getCount() {
        return customCountryList.size();
    }

    @Override
    public Country getItem(int position) {
        return customCountryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        Typeface fontFamily = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome-webfont.ttf");
        if (convertView == null) {

            Typeface fontFamilyK = Typeface.createFromAsset(context.getAssets(), "fonts/bubble.otf");



            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.countries_row_item2, parent, false);

            viewHolder.txtCountryName = (TextView) convertView.findViewById(R.id.txtCountryName);
            viewHolder.txtCountryName.setTypeface(fontFamilyK);
            viewHolder.imgCountry = (ImageView) convertView.findViewById(R.id.imgCountry);
            viewHolder.starFav = (TextView) convertView.findViewById(R.id.starFav);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Data item = getItem(position);  ???
        if (customCountryList.size() != 0) {

            viewHolder.txtCountryName.setText(customCountryList.get(position).getCountryName());
            viewHolder.txtCurrencyCode = (customCountryList.get(position).getCurrencyCode());
            viewHolder.isFavorite = customCountryList.get(position).isFavorite();
            viewHolder.starFav.setTypeface(fontFamily);
            if (viewHolder.isFavorite) {
                viewHolder.starFav.setText("\uf005");
            } else {
                viewHolder.starFav.setText("\uf006");
            }
            viewHolder.starFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doAnimation = false;
                    final String selectedCountry = viewHolder.txtCountryName.getText().toString();
                    if (viewHolder.isFavorite) {

                        viewHolder.starFav.setText("\uf006");

                        // returnCountryIfNotFavorite(selectedCountry);
                    } else {

                        viewHolder.starFav.setText("\uf005");
                    }
                    changeFavoriteInFullCountryList(selectedCountry);

                    // viewHolder.isFavorite = !viewHolder.isFavorite;
                    //  fullCountryList.get(position).setIsFavorite(!viewHolder.isFavorite);
                    Log.d("doAnimation1", doAnimation +"");

                   notifyDataSetChanged();

                }
            });

            String imageName = getItem(position).getImgCode().toLowerCase();
            int id = context.getResources().getIdentifier
                    ("shaked.aviad.aviv.myfinalconverter:drawable/" + imageName, null, null);
            viewHolder.imgCountry.setImageResource(id);

        }

        Log.d("doAnimation2", doAnimation +"");
        return convertView;
    }

    public void notifyDataByFavorite() {
        ArrayList<Country> favoriteCountryList = new ArrayList<>();
        ArrayList<Country> unFavoriteCountryList = new ArrayList<>();
        for (int i = 0; i < fullCountryList.size(); i++) {
            if (fullCountryList.get(i).isFavorite()) {
                favoriteCountryList.add(fullCountryList.get(i));
            } else {
                unFavoriteCountryList.add(fullCountryList.get(i));
            }
        }
        Collections.sort(unFavoriteCountryList, new Comparator<Country>() {
            @Override
            public int compare(Country country1, Country country2) {
                return country1.getCountryName().compareTo(country2.getCountryName());
            }
        });
        favoriteCountryList.addAll(unFavoriteCountryList);
        customCountryList = favoriteCountryList;
        notifyDataSetChanged();
    }

    private void changeFavoriteInFullCountryList(String selectedCountryName) {
        Country tempCountry;
        for (int i = 0; i < fullCountryList.size(); i++) {
            if (fullCountryList.get(i).getCountryName().equals(selectedCountryName)) {
                boolean fav = fullCountryList.get(i).isFavorite();
                tempCountry = fullCountryList.get(i);
                Log.d("first if", fullCountryList.get(i).getCountryName());
                fullCountryList.get(i).setIsFavorite(!fav);
                // fav = tempCountry.isFavorite();
                if (tempCountry.isFavorite()) {
                    Log.d("second if", fullCountryList.get(i).getCountryName());
                    fullCountryList.get(i).setIsFavorite(true);
                    fullCountryList.remove(i);
                    fullCountryList.add(0, tempCountry);
                    Toast.makeText(context, selectedCountryName + " is now 1st", Toast.LENGTH_SHORT).show();
                } else {
                    fullCountryList.get(i).setIsFavorite(false);
                }
                i = fullCountryList.size();
            }
        }
        notifyDataByFavorite();
        notifyDataSetChanged();
        Singleton.getInstance().setArrCountries(fullCountryList);
    }


    public void changeCustomList(String str) {
        customCountryList = new ArrayList<>();
        for (int i = 0; i < fullCountryList.size(); i++) {
            String countryName = fullCountryList.get(i).getCountryName();
            ArrayList<String> words = new ArrayList<>();
            int spaceIndex = countryName.indexOf(" ");

            if (spaceIndex == -1) {
                words.add(countryName);
            } else {
                String firstWord = countryName.substring(0, spaceIndex);
                String secondWord = countryName.substring(spaceIndex + 1, countryName.length());
                words.add(firstWord);
                words.add(secondWord);
            }
            for (int j = 0; j < words.size(); j++) {
                if (words.get(j).length() >= str.length()) {
                    String basicChars = words.get(j).substring(0, str.length());
                    Log.e("basicChars", basicChars);
                    if (basicChars.equalsIgnoreCase(str)) {
                        Log.e("equals?", str);
                        customCountryList.add(fullCountryList.get(i));
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

}

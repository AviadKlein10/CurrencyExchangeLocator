package shaked.aviad.aviv.myfinalconverter.view_fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import it.sephiroth.android.library.tooltip.Tooltip;
import me.grantland.widget.AutofitHelper;
import shaked.aviad.aviv.myfinalconverter.R;
import shaked.aviad.aviv.myfinalconverter.app.Singleton;
import shaked.aviad.aviv.myfinalconverter.module.custom_objects.Country;
import shaked.aviad.aviv.myfinalconverter.module.custom_objects.CountryListAdapter2;
import shaked.aviad.aviv.myfinalconverter.module.custom_objects.CustomEditText;
import shaked.aviad.aviv.myfinalconverter.module.enums.SIDE;
import shaked.aviad.aviv.myfinalconverter.module.listeners.ListenerClickableBtnsFragCurrency;
import shaked.aviad.aviv.myfinalconverter.module.listeners.ListenerLoadCountries;
import shaked.aviad.aviv.myfinalconverter.module.listeners.OnSwipeListener;

import static shaked.aviad.aviv.myfinalconverter.controller_activity.MainActivity.getColor;
import static shaked.aviad.aviv.myfinalconverter.module.enums.SIDE.LEFT;
import static shaked.aviad.aviv.myfinalconverter.module.enums.SIDE.RIGHT;

/**
 * Created by Aviv on 18/03/2016.
 */
public class FragmentCurrencyConverter extends Fragment implements View.OnClickListener {
    private ListenerClickableBtnsFragCurrency listenerClickableBtnsFragCurrency;
    private Context context;
    private TextView txtLastUpdate, txtCountryNameFrom, txtCountryNameTo,
            txtAmountTo, txtCurrencyInOne, graphBtn, btnSwitchCountries;
    private ImageView imgFlagFrom, imgFlagTo;
    private ListView countriesListView;
    private CustomEditText inputSearch;
    private int imageHeight, imageWidth;
    private EditText editAmountFrom;

    private LinearLayout layoutA;
    private LinearLayout layoutB;
    private RelativeLayout layoutLeftImage;
    private ArrayList<Country> arrCountries, initArrCountries;
    private OnSwipeListener onSwipeListener;
    private CountryListAdapter2 countryListAdapter;
    private SwipeActionAdapter swipeActionAdapter;
    private double result;
    private float xGraphBtn, yGraphBtn;

    private ListenerLoadCountries listenerLoadCountries;
    private boolean firstTimeRun = false;

    public void setListenerLoadCountries(ListenerLoadCountries listenerLoadCountries) {
        this.listenerLoadCountries = listenerLoadCountries;
    }


    public void setSwipeListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }

    private void editTextAmountFromListener() {
        editAmountFrom.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    Log.d("enter press:", "actionid: " + keyCode + " event " + event);
                    listenerClickableBtnsFragCurrency.editTextInitNewAmount();

                    //  txtAmountTo.setText(String.format("%.2f", result));
                    return true;
                }
                return false;
            }
        });


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();


        final View view = inflater.inflate(R.layout.fragment_currency_converter, container, false);

        Typeface fontFamilyIcons = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome-webfont.ttf");
        Typeface fontFamily = Typeface.createFromAsset(context.getAssets(), "fonts/bubble.otf");


        txtLastUpdate = (TextView) view.findViewById(R.id.txtLastUpdate);
        txtCountryNameFrom = (TextView) view.findViewById(R.id.txtCountryNameFrom);
        txtCountryNameTo = (TextView) view.findViewById(R.id.txtCountryNameTo);
        AutofitHelper.create(txtCountryNameFrom);
        AutofitHelper.create(txtCountryNameTo);
        editAmountFrom = (EditText) view.findViewById(R.id.editAmountFrom);

        txtAmountTo = (TextView) view.findViewById(R.id.txtAmountTo);
        txtCurrencyInOne = (TextView) view.findViewById(R.id.txtCurrencyInOne);
        imgFlagFrom = (ImageView) view.findViewById(R.id.imgFlagFrom);
        imgFlagTo = (ImageView) view.findViewById(R.id.imgFlagTo);
        graphBtn = (TextView) view.findViewById(R.id.graphBtn);
        xGraphBtn = graphBtn.getPivotX();
        yGraphBtn = graphBtn.getPivotY();
        btnSwitchCountries = (TextView) view.findViewById(R.id.btnSwitchCountries);
        graphBtn.setTypeface(fontFamilyIcons);
        btnSwitchCountries.setTypeface(fontFamilyIcons);
        graphBtn.setText("\uf201");
        btnSwitchCountries.setText("\uf0ec");

        txtAmountTo.setTypeface(fontFamily);
        txtCountryNameFrom.setTypeface(fontFamily);
        txtCountryNameTo.setTypeface(fontFamily);
        txtCurrencyInOne.setTypeface(fontFamily);
        txtLastUpdate.setTypeface(fontFamily);
        editAmountFrom.setTypeface(fontFamily);

        graphBtn.setTextColor(getColor(context, R.color.colorGreenIcon));
        btnSwitchCountries.setTextColor(getColor(context, R.color.colorGreenIcon));


        graphBtn.setTextSize(22);
        btnSwitchCountries.setTextSize(22);
        graphBtn.setOnClickListener(this);
        btnSwitchCountries.setOnClickListener(this);
        txtLastUpdate.setOnClickListener(this);
        txtCurrencyInOne.setOnClickListener(this);
        listenerLoadCountries.OnDoneLoadCountries();

        inputSearch = (CustomEditText) view.findViewById(R.id.inputSearch);
        layoutA = (LinearLayout) view.findViewById(R.id.layoutA);
        layoutB = (LinearLayout) view.findViewById(R.id.layoutB);
        layoutLeftImage = (RelativeLayout) view.findViewById(R.id.layoutLeftImage);


        countriesListView = (ListView) view.findViewById(R.id.countriesList);

        arrCountries = new ArrayList<>();
        initArrCountries = new ArrayList<>();
        initArrCountries = Singleton.getInstance().getArrCountries();
        initList();
        editTextListeners();
        displayCurrentDate();
        onChangeTextListener();
        editTextAmountFromListener();





       /* while(imgFlagFrom.getDrawable() == null || imgFlagTo.getDrawable() == null ){
            listenerLoadCountries.OnDoneLoadCountries();
        }*/

        if (firstTimeRun) {
            guideForFirstTime();
        }


        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageHeight = (int) (layoutLeftImage.getHeight() / 2.5);
                imageWidth = layoutLeftImage.getWidth() / 2;
                Log.d("New Sizes , Height : ", imageHeight + " Width : " + imageWidth);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        return view;
    }


    private void editTextListeners() {
        inputSearch.setKeyImeChangeListener(new CustomEditText.KeyImeChange() {
            @Override
            public void onKeyIme(int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    showLayouts();
                    Log.d("keyCode", keyCode + "");

                    // layoutA.startAnimation(animShow);
                    // layoutB.startAnimation(animShow);


                }
            }
        });


        inputSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    YoYo.with(Techniques.SlideOutUp).playOn(layoutA);
                    YoYo.with(Techniques.SlideOutUp).playOn(layoutB);
                     layoutA.setVisibility(View.GONE);
                     layoutB.setVisibility(View.GONE);

                } else {
                    layoutA.setVisibility(View.VISIBLE);
                    layoutB.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
    }


    private void onChangeTextListener() {
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    initList();
                } else {
                    searchItem(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void searchItem(String textToSearch) {
        countryListAdapter.changeCustomList(textToSearch);
        countryListAdapter.notifyDataSetChanged();
    }

    public void initCountryBox(SIDE side, final String countryName, final String countryImgCode) {

        if (side == RIGHT) {
            YoYo.with(Techniques.SlideOutRight)
                    .duration(700)
                    .playOn(imgFlagTo);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(context).
                            load("http://flags.fmcdn.net/data/flags/small/" + countryImgCode.toLowerCase() + ".png")
                            .resize(imageWidth, imageHeight)
                            .into(createTarget(imgFlagTo, txtCountryNameTo, countryName));
                }
            }, 600);
            // load("http://www.geonames.org/flags/x/" + countryImgCode.toLowerCase() + ".gif")

        }

        if (side == LEFT) {

            YoYo.with(Techniques.SlideOutLeft)
                    .duration(700)
                    .playOn(imgFlagFrom);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(context).
                            load("http://flags.fmcdn.net/data/flags/small/" + countryImgCode.toLowerCase() + ".png")
                            .resize(imageWidth, imageHeight)
                            .into(createTarget(imgFlagFrom, txtCountryNameFrom, countryName));
                    Log.d("imageWidth ", imageWidth + " imageHeight: " + imageHeight);
                }
            }, 600);


            Log.d("picasso ", "indexo");
        }
    }

    public void firstInitCountryBox(SIDE side, final String countryName, final String countryImgCode) {
        if (side == RIGHT) {
            Log.d("how the hell", "cmon");
            Picasso.with(context).
                    load("http://flags.fmcdn.net/data/flags/small/" + countryImgCode.toLowerCase() + ".png")
                    .resize(300, 150)
                    .into(createTarget(imgFlagTo, txtCountryNameTo, countryName));

            YoYo.with(Techniques.FadeIn).duration(1300).playOn(imgFlagTo);
            YoYo.with(Techniques.FadeIn).duration(1300).playOn(txtCountryNameTo);


        }
        if (side == LEFT) {

            Picasso.with(context).
                    // load("http://www.geonames.org/flags/x/" + countryImgCode.toLowerCase() + ".gif")
                            load("http://flags.fmcdn.net/data/flags/small/" + countryImgCode.toLowerCase() + ".png")
                    .resize(300, 150)
                    .into(createTarget(imgFlagFrom, txtCountryNameFrom, countryName));


            YoYo.with(Techniques.FadeIn).duration(1300).playOn(imgFlagFrom);
            YoYo.with(Techniques.FadeIn).duration(1300).playOn(txtCountryNameFrom);

        }
    }

    private Target createTarget(final ImageView imgFlag, final TextView txtCountryName, final String countryName) {

        return new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imgFlag.setImageBitmap(bitmap);
                txtCountryName.setText(countryName);
                txtAmountTo.setText(String.format("%.2f", result));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
    }

    public void setNewConvertUnitResults(double value) {

        double inputAmount = Double.parseDouble(editAmountFrom.getText().toString());
        result = inputAmount * value;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                txtAmountTo.setText(String.format("%.2f", result));

            }
        }, 200);
    }

    public void displayResultInOneUnit(String currencyCodeFrom, String currencyCodeTo, double currencyValue) {
        txtCurrencyInOne.setText("1 " + currencyCodeFrom + " = " + String.format("%.3f", currencyValue) + " " + currencyCodeTo);
    }

    private boolean checkSDKLowerThenM() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
    }

    private void initList() {
        arrCountries = initArrCountries;
        countryListAdapter = new CountryListAdapter2(context, arrCountries);
        Log.d("sizesizee", arrCountries.size() + "");
        countryListAdapter.notifyDataByFavorite();
        swipeActionAdapter = new SwipeActionAdapter(countryListAdapter);
        swipeActionAdapter.setListView(countriesListView);
        countriesListView.setAdapter(swipeActionAdapter);
        swipeActionAdapter.setSwipeActionListener(new SwipeActionAdapter.SwipeActionListener() {

            @Override
            public boolean hasActions(int position, SwipeDirection direction) {
                if (direction.isLeft()) return true; // Change this to false to disable left swipes
                if (direction.isRight()) return true; // Change this to false to disable left swipes
                return false;
            }

            @Override
            public boolean shouldDismiss(int position, SwipeDirection direction) {
                return false;
            }

            @Override
            public void onSwipe(int[] positionList, SwipeDirection[] directionList) {
                for (int i = 0; i < positionList.length; i++) {
                    SwipeDirection direction = directionList[i];
                    int position = positionList[i];

                    switch (direction) {
                        case DIRECTION_FAR_LEFT:
                        case DIRECTION_NORMAL_LEFT:
                            onSwipeListener.showCountryBox(LEFT, countryListAdapter.getItem(position), false);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    YoYo.with(Techniques.SlideInLeft)
                                            .duration(700)
                                            .playOn(imgFlagFrom);
                                }
                            }, 800);

                            Log.d("Far", "");

                            break;
                        case DIRECTION_FAR_RIGHT:
                        case DIRECTION_NORMAL_RIGHT:
                            onSwipeListener.showCountryBox(RIGHT, countryListAdapter.getItem(position), false);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    YoYo.with(Techniques.SlideInRight)
                                            .duration(700)
                                            .playOn(imgFlagTo);
                                }
                            }, 800);
                            break;
                    }
                }
            }
        });
    }

    public void setListenerClickableBtnsFragCurrency(ListenerClickableBtnsFragCurrency listenerClickableBtnsFragCurrency) {
        this.listenerClickableBtnsFragCurrency = listenerClickableBtnsFragCurrency;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.graphBtn:
                listenerClickableBtnsFragCurrency.initChartDialogFragment(xGraphBtn, yGraphBtn);
                break;
            case R.id.btnSwitchCountries:
                YoYo.with(Techniques.RubberBand).playOn(btnSwitchCountries);
                listenerClickableBtnsFragCurrency.btnSwitchCountriesPressed();
                break;
            case R.id.txtLastUpdate:
            case R.id.txtCurrencyInOne:

                showLayouts();
                break;
        }
    }

    private void showLayouts() {
        layoutA.setVisibility(View.VISIBLE);
        layoutB.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.SlideInDown).playOn(layoutA);
        YoYo.with(Techniques.SlideInDown).playOn(layoutB);
        inputSearch.clearFocus();
    }


    public void switchCountriesBox(final Country leftCountry, final Country rightCountry) {
        YoYo.with(Techniques.SlideOutRight)
                .duration(700)
                .playOn(imgFlagFrom);
        Log.d("switch", "me");
        YoYo.with(Techniques.SlideOutLeft)
                .duration(700)
                .playOn(imgFlagTo);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Picasso.with(context).
                        load("http://flags.fmcdn.net/data/flags/small/" + leftCountry.getImgCode().toLowerCase() + ".png")
                        .resize(imageWidth, imageHeight)
                        .into(createTarget(imgFlagTo, txtCountryNameTo, leftCountry.getCountryName()));
                Picasso.with(context).
                        // load("http://www.crwflags.com/fotw/images/" + rightCountry.getCurrencyCode().charAt(0) + "/" + rightCountry.getCurrencyCode() + ".gif")
                                load("http://flags.fmcdn.net/data/flags/small/" + rightCountry.getImgCode().toLowerCase() + ".png")
                        .resize(imageWidth, imageHeight)
                        .into(createTarget(imgFlagFrom, txtCountryNameFrom, rightCountry.getCountryName()));
            }
        }, 700);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.SlideInRight)
                        .duration(700)
                        .playOn(imgFlagFrom);
                Log.d("switch", "me");
                YoYo.with(Techniques.SlideInLeft)
                        .duration(700)
                        .playOn(imgFlagTo);
            }
        }, 700);

    }

    private void displayCurrentDate() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        txtLastUpdate.setText("  Last update: " + df.format(date));
    }

    public void guideForFirstTime() {
        Log.d("firstTime", "here");
        Tooltip.make(context,
                new Tooltip.Builder(101)
                        .anchor(countriesListView, Tooltip.Gravity.BOTTOM)
                        .closePolicy(new Tooltip.ClosePolicy()
                                .insidePolicy(true, false)
                                .outsidePolicy(true, false), 10000000)
                        .activateDelay(800)
                        .showDelay(300)
                        .text("Choose countries by Swiping LEFT or RIGHT")
                        .maxWidth(1000)
                        .withArrow(true)
                        .withOverlay(true)
                        .floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
                        .build()
        ).show();
    }

    public void setFirstTimeRun(boolean firstTimeRun) {
        this.firstTimeRun = firstTimeRun;
    }
}


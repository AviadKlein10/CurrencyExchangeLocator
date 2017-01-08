package shaked.aviad.aviv.myfinalconverter.view_fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import shaked.aviad.aviv.myfinalconverter.R;
import shaked.aviad.aviv.myfinalconverter.internet.ChartConnectivity;
import shaked.aviad.aviv.myfinalconverter.module.enums.DatesForChart;
import shaked.aviad.aviv.myfinalconverter.module.listeners.DownloadChartPointsListener;

import static shaked.aviad.aviv.myfinalconverter.controller_activity.MainActivity.getColor;

/**
 * Created by Aviad on 24/04/2016.
 */
public class FragmentChart extends DialogFragment implements DownloadChartPointsListener, View.OnClickListener {
    private LineChart chart;
    private ChartConnectivity connectivity;
    private String leftCurrency, rightCurrency;
    private String leftCountryName, rightCountryName;
    private String description = " ";
    private LinearLayout linearLayoutChart;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart_layout, container, false);

        linearLayoutChart = (LinearLayout)view.findViewById(R.id.linearLyaoutChart);
/*

        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(800);                        //     TODO
     //   animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
     //   animationSet.addAnimation(alphaAnimation);
     //   linearLayoutChart.setAnimation(animationSet);

*/



        Button btnDaily = (Button) view.findViewById(R.id.btnDaily);
        Button btnMonthly = (Button) view.findViewById(R.id.btnMonthly);
        Button btnYearly = (Button) view.findViewById(R.id.btnYearly);
        connectivity = new ChartConnectivity(getActivity(), this);

        btnDaily.setOnClickListener(this);
        btnMonthly.setOnClickListener(this);
        btnYearly.setOnClickListener(this);


        chart = (LineChart) view.findViewById(R.id.chart);
        chart.setNoDataText("  ");
        TextView textView = (TextView) view.findViewById(R.id.txtChartTitle);
        textView.setText(" " + leftCountryName + "/" + rightCountryName);
        connectivity.downloadInfo(leftCurrency, rightCurrency, DatesForChart.SixDaysAgo);
        return view;
    }


    @Override
    public void onDoneDownloadChartPoints(ArrayList<Entry> entries, ArrayList<String> labels) {


        LineDataSet dataSet = new LineDataSet(entries, " values");
        dataSet.setDrawCubic(false);
        dataSet.setValueTextSize(11);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setLineWidth(3);
        dataSet.setCircleRadius(5);
        dataSet.setDrawFilled(true);

        LineData data = new LineData(labels, dataSet);

        chart.setDescription(description);


        data.setValueTextColor(Color.YELLOW);

        chart.setDrawGridBackground(true);


        XAxis xAxis = chart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawLabels(true);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setTextColor(Color.WHITE);


        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setDrawGridLines(false);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        chart.setData(data);

        chart.setGridBackgroundColor(getColor(getContext(),R.color.colorDarkBlue));
        chart.setDrawingCacheBackgroundColor(Color.YELLOW);


        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                chart.invalidate();
                chart.animateX(1700, Easing.EasingOption.EaseInOutCirc);
            }
        });

    }

    @Override
    public void onClick(View v) {
        chart.clear();
        DatesForChart datesForChart = null;

        switch (v.getId()) {
            case R.id.btnDaily:
                datesForChart = DatesForChart.SixDaysAgo;
                description = "Daily";
                break;
            case R.id.btnMonthly:
                datesForChart = DatesForChart.SixMonthAgo;
                description = "Monthly";
                break;
            case R.id.btnYearly:
                datesForChart = DatesForChart.SixYearsAgo;
                description = "Yearly";
                break;
        }

       // chart.clearAnimation();
       // chart.removeAllViews();
      //  chart.setNoDataText("  ");

        connectivity.downloadInfo(leftCurrency, rightCurrency, datesForChart);


    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null) {
            return;
        }

      /*  final View decorView = getDialog()
                .getWindow()
                .getDecorView();

        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(decorView,
                PropertyValuesHolder.ofFloat("scaleX", 0.0f, 1.0f),
                PropertyValuesHolder.ofFloat("scaleY", 0.0f, 1.0f),
                PropertyValuesHolder.ofFloat("alpha", 0.0f, 1.0f));

//scaleDown.ofFloat(decorView,this,10,this,10,) TODO

        scaleDown.setDuration(1300);
        scaleDown.start();*/


        ScaleAnimation fade_in =  new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
        fade_in.setDuration(1000);     // animation duration in milliseconds
        fade_in.setFillAfter(true);    // If fillAfter is true, the transformation that this animation performed will persist when it is finished.
        linearLayoutChart.startAnimation(fade_in);

      /*  getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


     */   DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
       getDialog().getWindow().setLayout(width, height / 2);
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);



    }

    public void setCurrencies(String newLeftCurrencyCode, String newRightCurrencyCode, String newLeftCountryName, String newRightCountryName) {
        leftCurrency = newLeftCurrencyCode;
        rightCurrency = newRightCurrencyCode;
        leftCountryName = newLeftCountryName;
        rightCountryName = newRightCountryName;

    }
}


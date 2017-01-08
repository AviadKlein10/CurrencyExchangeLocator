package shaked.aviad.aviv.myfinalconverter.internet;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import shaked.aviad.aviv.myfinalconverter.module.enums.DatesForChart;
import shaked.aviad.aviv.myfinalconverter.module.listeners.DownloadChartPointsListener;

/**
 * Created by Aviv on 20/04/2016.
 */
public class ChartConnectivity {

    private Context context;
    private DownloadChartPointsListener chartPointsListener;
    private ArrayList<Point> chartPoints;
    private String todayDate, requestedDate;
    private int subDay, demandedLines;

    public ChartConnectivity(Context context, DownloadChartPointsListener chartPointsListener) {
        this.context = context;
        this.chartPointsListener = chartPointsListener;
    }

    public void downloadInfo(final String currencyFrom, final String currencyTo, final DatesForChart dateFrom) {

        switch (dateFrom){
            case SixDaysAgo:
                subDay = -6;
                demandedLines = 1;
                break;
            case SixMonthAgo:
                subDay = -180;
                demandedLines = 17;
                break;
            case SixYearsAgo:
                subDay = -1825;
                demandedLines = 20;
                break;
        }

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        todayDate = df.format(date);
        calendar.add(Calendar.DATE, subDay);
        date = calendar.getTime();
        requestedDate = df.format(date);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    URL url = new URL("http://currencies.apps.grandtrunk.net/getrange/" + requestedDate + "/" + todayDate + "/" + currencyFrom + "/" + currencyTo);
                    InputStreamReader in = new InputStreamReader(url.openStream(), "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(in);
                    String line = "";
                    ArrayList<Entry> entries = new ArrayList<>();
                    ArrayList<String> labels = new ArrayList<String>();
                    int lineIndex = 0;
                    int enteryIndex = 0;
                    int x = 0;
                    while ((line = bufferedReader.readLine()) != null) {
                        Log.d("line :",x++ +"");
                        if (lineIndex % demandedLines == 0) {
                            Log.d("line", line);
                            String date = line.substring(0, line.indexOf(" "));
                            Log.d("date", date);
                            String rate = line.substring(line.indexOf(" ") + 1, line.length());
                            Log.d("rate", rate);

                            Entry entry = new Entry(Float.valueOf(rate), enteryIndex++);
                            entries.add(entry);
                            labels.add(date);
                        }
                        lineIndex++;


                    }
                    chartPointsListener.onDoneDownloadChartPoints(entries, labels);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//Log.d("chart poins",chartPoints.get(0).getLabel());

            }
        });
        thread.start();
    }
}

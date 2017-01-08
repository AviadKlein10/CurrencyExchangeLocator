package shaked.aviad.aviv.myfinalconverter.module.listeners;



import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

/**
 * Created by Aviad on 11/05/2016.
 */
public interface DownloadChartPointsListener {
    void onDoneDownloadChartPoints(ArrayList<Entry> entries, ArrayList<String> labels);


}

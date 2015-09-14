package ie.mu.jos.acceleplot.chartos;

import android.content.Context;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to create MPACharts with data
 */
public class Chartos {

    private final Context context;
    private final ChartType chartType;

    //TODO (jos) do something with types of charts or delete the Enum
    public enum ChartType {
        BAR
    }

    public Chartos(Context context, ChartType chartType){
        this.context = context;
        this.chartType = chartType;
    }

    private BarDataSet generateDataForChart(){

        // Creating data for a chart
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(4f, 0));
        entries.add(new BarEntry(8f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(12f, 3));
        entries.add(new BarEntry(18f, 4));
        entries.add(new BarEntry(9f, 5));
        entries.add(new BarEntry(29f, 6));

        return new BarDataSet(entries, "# of Calls");
    }

    private ArrayList getLabels(){
        ArrayList<String> labels = new ArrayList<>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        labels.add("July");

        return labels;
    }

    public BarChart getBarChar(){
        BarChart barChart = new BarChart(this.context);
        ArrayList labels = getLabels();
        BarDataSet dataset = generateDataForChart();
        BarData data = new BarData(labels, dataset);
        barChart.setData(data);
        barChart.setDescription("# of times NoOne called Bob");
        return barChart;
    }
}

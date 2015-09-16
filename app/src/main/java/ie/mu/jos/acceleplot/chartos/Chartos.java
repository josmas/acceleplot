package ie.mu.jos.acceleplot.chartos;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to create MPACharts with data
 */
public class Chartos {

    private final Context context;
    private final ChartType chartType;

    //TODO (jos) do something with types of charts or delete the Enum
    // Could subclass Chartos (or make it an interface)?
    public enum ChartType {
        BAR,
        LINE
    }

    public Chartos(Context context, ChartType chartType){
        this.context = context;
        this.chartType = chartType;
    }

    private BarDataSet generateDataForBarChart(){

        // Creating data for a chart
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(4f, 0));
        entries.add(new BarEntry(8f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(12f, 3));
        entries.add(new BarEntry(18f, 4));
        entries.add(new BarEntry(9f, 5));
        entries.add(new BarEntry(29f, 6));
        entries.add(new BarEntry(22f, 7));

        return new BarDataSet(entries, "# of Calls");
    }

    private ArrayList getBarChartLabels(){
        ArrayList<String> labels = new ArrayList<>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        labels.add("July");
        labels.add("August");

        return labels;
    }

    public BarChart getBarChar(){
        BarChart barChart = new BarChart(this.context);
        BarData data = new BarData(getBarChartLabels(), generateDataForBarChart());
        barChart.setData(data);
        barChart.setDescription("# of times NoOne called Bob");

        return barChart;
    }

    // Line Chart
    public LineChart getLineChart(){
        LineChart lineChart = new LineChart(this.context);
        lineChart.setNoDataTextDescription("You need to provide data for the chart.");
        lineChart.setData(setLineChartData(20, 100));

        return lineChart;
    }

    private LineData setLineChartData(int count, float range) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add((i) + "");
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult) + 3;// + (float)((mult * 0.1) / 10);
            yVals.add(new Entry(val, i));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "DataSet 1");

        set1.setColor(Color.DKGRAY);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(3f);
        set1.setCircleSize(5f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.BLACK);

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        return data;
    }
}

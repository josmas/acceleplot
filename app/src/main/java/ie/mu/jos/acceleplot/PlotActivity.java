package ie.mu.jos.acceleplot;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import ie.mu.jos.acceleplot.accelo.AccelReading;
import ie.mu.jos.acceleplot.chartos.Chartos;

public class PlotActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.plot, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // JOS: the idea of recreating the Fragment from the Drawer when the screen contains things as
    // charts is a bit heavy going. Would be better to have different fragments and create the
    // layouts for the charts in XML. Next time do not start the project with a fragment activity!
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static int plotNumber;
        private AccelReading arCopy;
        private TextView readingsCopy;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            plotNumber = sectionNumber;
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_plot, container, false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            RelativeLayout rl = (RelativeLayout)getActivity().findViewById(R.id.fragmentRelLayout);
            // TODO (jos) creating this guy here means we are creating several ones and we lose the
            // reference when we change fragments (we cannot stop it other than from the button,
            // and at the time that it is created). (A singleton won't work on Async code)
            final AccelReading ar = new AccelReading(this);
            arCopy = ar; // Bit of a hack to facilitate unregistering onStop
            final TextView readings = new TextView(getContext());
            readingsCopy = readings; // Bit of a hack to receive data from AccelReadings

            // Forcing this fragment to Landscape to avoid redrawing big Plots
            getActivity().setRequestedOrientation(
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            if (plotNumber == 1){
                //TODO (jos) inject in constructor
                Chartos chartos = new Chartos(getContext(), Chartos.ChartType.BAR);
                BarChart chart = chartos.getBarChar();

                chart.setLayoutParams(rl.getLayoutParams());
                rl.addView(chart);
            }
            else if (plotNumber == 2){

                // Creating a linear layout here : It's getting too busy!
                LinearLayout.LayoutParams layoutParamsVert = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                layoutParamsVert.setMargins(10, 1, 10, 1);
                final LinearLayout containerLayoutVert = new LinearLayout(getContext());
                containerLayoutVert.setOrientation(LinearLayout.VERTICAL);
                containerLayoutVert.setLayoutParams(layoutParamsVert);

                LinearLayout.LayoutParams layoutParamsHor = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParamsHor.setMargins(10, 1, 10, 1);
                LinearLayout containerLayoutHor = new LinearLayout(getContext());
                containerLayoutHor.setOrientation(LinearLayout.HORIZONTAL);
                containerLayoutHor.setLayoutParams(layoutParamsHor);

                ToggleButton toggleAccelReadings = new ToggleButton(getContext());
                containerLayoutHor.addView(toggleAccelReadings);
                toggleAccelReadings.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    Chartos chartos = new Chartos(getContext(), Chartos.ChartType.LINE);
                    LineChart lineChart;
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) { // The toggle is enabled
                            counter = 0;
                            xValuesList.clear();
                            yValuesList.clear();
                            zValuesList.clear();

                            containerLayoutVert.removeViewInLayout(lineChart);
                            lineChart = null;
                            ar.registerSensorListener();
                            readings.setText("Starting readings...");
                        } else { // The toggle is disabled
                            ar.unregisterSensorListener();
                            readings.setText("no readings...");
                            lineChart = chartos.getLineChart(false);
                            //TODO (jos) A lot of messy code here. Abstract it out.
                            LineDataSet setX = new LineDataSet(xValuesList, "DataSet X");
                            setX.setColor(Color.DKGRAY);
                            setX.setLineWidth(3f);
                            setX.setDrawCircles(false);
                            setX.setDrawCubic(true);
                            setX.setValueTextSize(5f);

                            LineDataSet setY = new LineDataSet(yValuesList, "DataSet Y");
                            setY.setColor(Color.RED);
                            setY.setDrawCircles(false);
                            setY.setDrawCubic(true);
                            setY.setLineWidth(3f);
                            setY.setValueTextSize(5f);

                            LineDataSet setZ = new LineDataSet(zValuesList, "DataSet Z");
                            setZ.setColor(Color.BLUE);
                            setZ.setLineWidth(3f);
                            setZ.setDrawCircles(false);
                            setZ.setDrawCubic(true);
                            setZ.setValueTextSize(5f);

                            ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
                            dataSets.add(setX);
                            dataSets.add(setY);
                            dataSets.add(setZ);

                            ArrayList<String> xAxisVals = new ArrayList<String>();
                            for (int i = 0; i < counter; i++) {
                                xAxisVals.add((i) + "");
                            }
                            LineData data = new LineData(xAxisVals, dataSets);
                            lineChart.setData(data);

                            lineChart.setLayoutParams(containerLayoutVert.getLayoutParams());
                            containerLayoutVert.addView(lineChart);
                            lineChart.invalidate();
                        }
                    }
                });
                containerLayoutHor.addView(readings);
                containerLayoutVert.addView(containerLayoutHor);
                rl.addView(containerLayoutVert);
            }
            else {
                Chartos chartos = new Chartos(getContext(), Chartos.ChartType.LINE);
                LineChart lineChart = chartos.getLineChart(true);
                lineChart.setLayoutParams(rl.getLayoutParams());
                rl.addView(lineChart);
            }
        }

        private int counter = 0;
        ArrayList<Entry> xValuesList = new ArrayList<Entry>();
        ArrayList<Entry> yValuesList = new ArrayList<Entry>();
        ArrayList<Entry> zValuesList = new ArrayList<Entry>();
        public void writeReading(String reading, float xValue, float yValue, float zValue){
            if (readingsCopy != null){
                readingsCopy.setText(reading);
                counter++;
                xValuesList.add(new Entry(xValue, counter));
                yValuesList.add(new Entry(yValue, counter));
                zValuesList.add(new Entry(zValue, counter));
            }
        }

        @Override
        public void onStop() {
            super.onStop();
            if (arCopy != null)
                arCopy.unregisterSensorListener();
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((PlotActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}

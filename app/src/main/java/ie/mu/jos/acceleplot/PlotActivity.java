package ie.mu.jos.acceleplot;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

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
    private String buttonTitle;

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
                buttonTitle = "Graph " + mTitle;
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                buttonTitle = "Graph " + mTitle;
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                buttonTitle = "Graph " + mTitle;
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

            if (plotNumber == 1){
                //TODO (jos) inject in constructor
                Chartos chartos = new Chartos(getContext(), Chartos.ChartType.BAR);
                BarChart chart = chartos.getBarChar();

                chart.setLayoutParams(rl.getLayoutParams());
                rl.addView(chart);
            }
            else if (plotNumber == 2){

                // Creating a linear layout here : It's getting too busy!
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(10, 1, 10, 1);

                LinearLayout containerLayout = new LinearLayout(getContext());
                containerLayout.setOrientation(LinearLayout.HORIZONTAL);
                containerLayout.setLayoutParams(layoutParams);

                ToggleButton toggleAccelReadings = new ToggleButton(getContext());
                containerLayout.addView(toggleAccelReadings);
                toggleAccelReadings.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) { // The toggle is enabled
                            ar.registerSensorListener();
                            readings.setText("Starting readings...");
                        } else { // The toggle is disabled
                            ar.unregisterSensorListener();
                            readings.setText("no readings...");
                        }
                    }
                });
                containerLayout.addView(readings);
                rl.addView(containerLayout);
            }
            else {
                TextView tv = new TextView(getContext());
                tv.setText("Nothing to see here for now...");
                rl.addView(tv);
            }
        }

        public void writeReading(String reading){
            if (readingsCopy != null)
                readingsCopy.setText(reading);
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

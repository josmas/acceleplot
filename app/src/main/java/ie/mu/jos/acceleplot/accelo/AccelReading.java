package ie.mu.jos.acceleplot.accelo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import ie.mu.jos.acceleplot.PlotActivity;

public class AccelReading implements SensorEventListener {

    private final PlotActivity.PlaceholderFragment fragment;
    private SensorManager senSensorManager;

    public AccelReading(PlotActivity.PlaceholderFragment fragment){
        this.fragment = fragment;
        senSensorManager = (SensorManager) fragment.getContext().getSystemService(Context.SENSOR_SERVICE);
    }

    public void registerSensorListener() {
        senSensorManager.registerListener(
                this,
                senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    public void unregisterSensorListener() {
        senSensorManager.unregisterListener(
                this,
                senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d("ACCELREADING", "X is: " + sensorEvent.values[0] + " and Y is: " + sensorEvent.values[1]);
        fragment.writeReading("X is: " + sensorEvent.values[0] + " and Y is: " + sensorEvent.values[1]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("ACCELREADING", "Nothing registering here so far - ACC CHANGED");
    }
}

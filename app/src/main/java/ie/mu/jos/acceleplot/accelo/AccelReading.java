package ie.mu.jos.acceleplot.accelo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.Fragment;
import android.util.Log;

public class AccelReading implements SensorEventListener {

    private SensorManager senSensorManager;

    public AccelReading(Fragment fragment){
        senSensorManager = (SensorManager) fragment.getContext().getSystemService(Context.SENSOR_SERVICE);
    }

    public void registerSensorListener() {
        senSensorManager.registerListener(
                this,
                senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    public void unregisterSensorListener() {
        Log.d("ACCELREADING", "Calling unregistering but don't really caring about it");
        Log.d("ACCELREADING", "Calling unregistering but don't really caring about it");
        senSensorManager.unregisterListener(
                this,
                senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d("ACCELREADING", "X is: " + sensorEvent.values[0] + " and Y is: " + sensorEvent.values[1]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("ACCELREADING", "Nothing registering here so far - ACC CHANGED");
    }
}

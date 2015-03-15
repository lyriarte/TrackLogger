package org.yriarte.tracklogger;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import java.sql.Timestamp;


public class TrackLogger extends ActionBarActivity implements LocationListener {

    static int minTimeUpdateSeconds = 30;
    static float minDistanceUpdateMeters = 5;

    LocationManager mLocationManager;
    Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_logger);
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (mLocation == null)
            return;
        updateTrackPoint(mLocation.getLatitude(),mLocation.getLongitude(),mLocation.getAltitude(),mLocation.getTime());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_track_logger, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_log_start) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    minTimeUpdateSeconds * 1000,
                    minDistanceUpdateMeters,
                    this);
            return true;
        }
        if (id == R.id.action_log_stop) {
            mLocationManager.removeUpdates(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateTrackPoint(double lat, double lon, double ele, long time) {
        ((TextView)findViewById(R.id.value_lat)).setText(Double.valueOf(lat).toString());
        ((TextView)findViewById(R.id.value_lon)).setText(Double.valueOf(lon).toString());
        ((TextView)findViewById(R.id.value_ele)).setText(Double.valueOf(ele).toString());
        ((TextView)findViewById(R.id.value_time)).setText(new Timestamp(time).toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        updateTrackPoint(mLocation.getLatitude(),mLocation.getLongitude(),mLocation.getAltitude(),mLocation.getTime());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

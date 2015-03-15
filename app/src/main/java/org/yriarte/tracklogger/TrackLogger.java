package org.yriarte.tracklogger;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;


public class TrackLogger extends ActionBarActivity implements LocationListener {

    static int minTimeUpdateSeconds = 30;
    static float minDistanceUpdateMeters = 5;

    static String xmlHeader = "<?xml version='1.0' encoding='Utf-8' standalone='yes' ?>";
    static String gpxTrackHeader = "<gpx xmlns=\"http://www.topografix.com/GPX/1/0\" version=\"1.0\" creator=\"org.yriarte.tracklogger\">\n<trk>\n<trkseg>\n";
    static String gpxTrackFooter = "\n</trkseg>\n</trk>\n</gpx>\n";

    LocationManager mLocationManager;
    Location mLocation;

    FileWriter gpxLogWriter;

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
            if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
                return true;
            try {
                gpxLogWriter = new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                        + getString(R.string.app_name) + "_" + String.valueOf(Calendar.getInstance().getTime().getTime())
                        + ".gpx"
                );
                gpxLogWriter.write(xmlHeader + gpxTrackHeader);
            } catch (Exception e) {
                gpxLogWriter = null;
            }
            return true;
        }
        if (id == R.id.action_log_stop) {
            mLocationManager.removeUpdates(this);
            if (gpxLogWriter == null)
                return true;
            try {
                gpxLogWriter.append(gpxTrackFooter);
                gpxLogWriter.close();
            } catch (IOException e)
            {
                gpxLogWriter = null;
            }
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

    public String gpxTrackPoint(double lat, double lon, double ele, long time) {
        String trkpt = "<trkpt";
        trkpt += " lon=\"" + Double.valueOf(lon).toString() + "\"";
        trkpt += " lat=\"" + Double.valueOf(lat).toString() + "\"";
        trkpt += ">\n  <ele>" + Double.valueOf(ele).toString() + "</ele>\n";
        byte timebytes[] = new Timestamp(time).toString().getBytes();
        timebytes[10]='T'; timebytes[19]='Z';
        trkpt += "  <time>" + new String(timebytes).substring(0,20) + "</time>\n";
        trkpt += "</trkpt>\n";
        return trkpt;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        updateTrackPoint(mLocation.getLatitude(), mLocation.getLongitude(), mLocation.getAltitude(), mLocation.getTime());
        if (gpxLogWriter == null)
            return;
        try {
            gpxLogWriter.append(gpxTrackPoint(mLocation.getLatitude(), mLocation.getLongitude(), mLocation.getAltitude(), mLocation.getTime()));
        } catch (IOException e)
        {
            gpxLogWriter = null;
        }
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

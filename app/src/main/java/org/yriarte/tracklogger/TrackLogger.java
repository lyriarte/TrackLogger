package org.yriarte.tracklogger;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import java.sql.Timestamp;
import java.util.Calendar;


public class TrackLogger extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_logger);
        updateTrackPoint(43.611769,4.010320,3,Calendar.getInstance().getTimeInMillis());
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
            return true;
        }
        if (id == R.id.action_log_stop) {
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

}

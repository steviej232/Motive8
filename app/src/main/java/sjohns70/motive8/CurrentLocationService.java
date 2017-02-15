package sjohns70.motive8;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Steven on 2/14/2017.
 */

public class CurrentLocationService extends Service {
    // constant
    public static final long NOTIFY_INTERVAL = 10 * 1000; // 10 seconds

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
    int count;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }

    @Override
    public void onDestroy(){
        mTimer.cancel();
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {

                    TrackGPS gps = new TrackGPS(getApplicationContext());
                    if(gps.canGetLocation()){
                        Intent in = new Intent("CurrentLocation");
                        in.putExtra("latitude",gps.getLatitude());
                        in.putExtra("longitude",gps.getLongitude());
                        //Toast.makeText(getApplicationContext(),"latitude:"+gps.getLatitude()+" longitude:"+gps.getLongitude(), Toast.LENGTH_SHORT).show();
                    //sendBroadcast(in);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(in);
                    }
                }

            });
        }

    }


}

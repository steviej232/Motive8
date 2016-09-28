package sjohns70.motive8;

/**
 * Created by Steven on 9/27/2016.
 */

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class CircleActivity extends Activity
{
    CircleFillView circleFill;
    Timer _t,timer;
    int _count=93;
    private TextView tv_points;
    private int points;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_activity);

        circleFill = (CircleFillView) findViewById(R.id.circleFillView);
        tv_points = (TextView)findViewById(R.id.points_tv);
        _t = new Timer();
        _t.scheduleAtFixedRate( new TimerTask() {
            @Override
            public void run() {

                _count++;

                runOnUiThread(new Runnable() //run on ui threa
                {
                    public void run()
                    {
                        if(_count == 100) {
                            _count = 0;
                            points++;
                        }
                        tv_points.setText(""+points);
                        circleFill.setValue(_count);
                    }
                });
            }
        }, 1000, 1000 );
    }
}

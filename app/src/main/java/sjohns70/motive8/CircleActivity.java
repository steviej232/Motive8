package sjohns70.motive8;

/**
 * Created by Steven on 9/27/2016.
 */

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;


public class CircleActivity extends Activity
{
    CircleFillView circleFill;
    SeekBar seekBar;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_activity);

        circleFill = (CircleFillView) findViewById(R.id.circleFillView);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setProgress(circleFill.getValue());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if (fromUser)
                    circleFill.setValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        }
}

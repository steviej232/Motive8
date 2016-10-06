package sjohns70.motive8;

/**
 * Created by Steven on 9/27/2016.
 */

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;


public class CircleActivity extends Activity
{
    CircleFillView circleFill;
    Timer _t,timer;
    int _count;
    private TextView tv_points;
    private int points;
    private UserData userData;
    private DatabaseReference myRef;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_activity);

        circleFill = (CircleFillView) findViewById(R.id.circleFillView);
        tv_points = (TextView)findViewById(R.id.points_tv);
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("points");
        userData = new UserData();

        startTimer();
        HomeScreen hs = new HomeScreen();
        hs.createBottomBar(this,savedInstanceState,CircleActivity.this);
    }

    void startTimer(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userData = dataSnapshot.getValue(UserData.class);
                tv_points.setText(""+userData.getPoints_earned());
                points = userData.getPoints_earned();
                _count = userData.getCount_remainder();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
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
                            userData.setPoints_earned(++points);
                            userData.setCount_remainder(_count);
                            myRef.setValue(userData);
                        }
                        //tv_points.setText(""+points);
                        circleFill.setValue(_count);
                    }
                });
            }
        }, 1000, 1000 );
    }

    @Override
    protected void onPause() {
        super.onPause();
        userData.setCount_remainder(_count);
        myRef.setValue(userData);
        _t.cancel();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startTimer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        userData.setCount_remainder(_count);
        myRef.setValue(userData);
        _t.cancel();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        userData.setCount_remainder(_count);
        myRef.setValue(userData);
        _t.cancel();
    }
}

/* Copyright statement */

package sjohns70.motive8;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;

import java.util.Timer;

import sjohns70.motive8.data.UserData;

/**
 * CircleActivity.java
 *
 * This class
 */
public class CircleActivity extends Activity {
    Timer _t;
    public static int _count;
    private TextView tv_points;
    private static int points;
    private static UserData userData;
    private static DatabaseReference myRef;
    private BottomBar bottomBar;
    private TextView logo;
    private static ImageView progress_heart;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private double curLongitude;
    private double curLatitude;
    private double gym_latitude;
    private double gym_longitude;
    private Intent timerIntent;
    private Intent locationIntent;

    private LocationManager mLocationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_activity);

        tv_points = (TextView) findViewById(R.id.points_tv);
        progress_heart = (ImageView) findViewById(R.id.progress_heart);
        Intent intent = getIntent();
        gym_latitude = intent.getDoubleExtra("gym_latitude", 0);
        gym_longitude = intent.getDoubleExtra("gym_longitude", 0);


        getUser();
        setupLogo();
        BottomBarActivity bottomBarActivity = new BottomBarActivity();
        bottomBar = bottomBarActivity.createBottomBar(this, savedInstanceState, CircleActivity.this, 1);

        //Need sleep for database timing
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timerIntent = new Intent(this, TimerService.class);
        locationIntent = new Intent(this,CurrentLocationService.class);
        startService(timerIntent);
        startService(locationIntent);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                broadcastReceiver, new IntentFilter("CurrentLocation"));

        //Toast.makeText(getApplicationContext(),""+_count,Toast.LENGTH_SHORT).show();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            curLatitude = intent.getDoubleExtra("latitude",0);
            curLongitude = intent.getDoubleExtra("longitude",0);
            double[] location = new double[2];
            location[0] = curLatitude;
            location[1] = curLongitude;
            //Toast.makeText(getApplicationContext(),"latitude:"+location[0]+" longitude:"+location[1], Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(),"gym latitude:"+gym_latitude+" gym longitude:"+gym_longitude,Toast.LENGTH_SHORT).show();
            if(!is_inside_circle(location,gym_latitude,gym_longitude)) {
                stopService(timerIntent);
                stopService(locationIntent);
                finish();
            }
        }
    };

    public boolean is_inside_circle(double[] location, double latitude, double longitude){
        float[] distance = new float[2];

        Location.distanceBetween( location[0], location[1],
                latitude, longitude, distance);

        if( distance[0] > MapsActivity.CIRCLE_RADIUS ){
            return false;
        } else {
            return true;
        }
    }

    private void setupLogo(){
        logo = (TextView)findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(CircleActivity.this, HomeScreen.class);
                CircleActivity.this.startActivity(myIntent);
            }
        });
        TextView title = (TextView)findViewById(R.id.Title);
        String titleText = "<font color=#404040>Motiv</font><font color=#FFA03E>8</font>";
        Typeface bebasFont = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue Bold.otf");

        logo.setTypeface(bebasFont);
        title.setText(Html.fromHtml(titleText));
        title.setTypeface(bebasFont);
        title.setTextSize(42);
    }

    public void getUser(){
        FirebaseApp.initializeApp(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("USERS").child(mAuth.getCurrentUser().getUid());
        userData = new UserData();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userData = dataSnapshot.getValue(UserData.class);
                points = userData.getPoints_earned();
                _count = userData.getCount_remainder();
                tv_points.setText(""+points);
                logo.setText(""+points);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void incrementCount(){

        _count++;
        if(_count >= 100) {
            _count = 0;
            userData.setPoints_earned(++points);
        }
        progress_heart.getBackground().setLevel(_count*100);
        userData.setCount_remainder(_count);
        myRef.setValue(userData);
    }

}

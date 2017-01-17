package sjohns70.motive8;

/**
 * Created by Steven on 9/27/2016.
 * test edit : Zach
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;

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
    private BottomBar bottomBar;
    private TextView logo;
    private ImageView progress_heart;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_activity);
        Intent intent = getIntent();
        int curTab=0;
        intent.getIntExtra("currentTab",curTab);

        tv_points = (TextView)findViewById(R.id.points_tv);
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        //myRef = database.getReference("points");
        myRef = database.getReference("USERS").child(mAuth.getCurrentUser().getUid());
        userData = new UserData();

        startTimer();
        setupLogo();
        BottomBarActivity bottomBarActivity = new BottomBarActivity();
        bottomBar = bottomBarActivity.createBottomBar(this,savedInstanceState,CircleActivity.this,1);
        progress_heart = (ImageView)findViewById(R.id.progress_heart);
        Toast.makeText(getApplicationContext(),""+mAuth.getCurrentUser().getUid(),Toast.LENGTH_SHORT).show();
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

    void startTimer(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userData = dataSnapshot.getValue(UserData.class);
                points = userData.getPoints_earned();
                tv_points.setText(""+points);
                logo.setText(""+points);
                myRef.child("points_earned").setValue(points);
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
                            myRef.child("point_remainder").setValue(_count);
                            userData.setCount_remainder(_count);
                            myRef.setValue(userData);
                        }
                        progress_heart.getBackground().setLevel(_count*100);
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

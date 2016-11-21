package sjohns70.motive8;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by Steven on 9/29/2016.
 */

public class TestActivity extends BaseGameActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private TextView messageView;
    private GoogleApiClient mGoogleApiClient;
    // Are we currently resolving a connection failure?
    private boolean mResolvingConnectionFailure = false;

    // Has the user clicked the sign-in button?
    private boolean mSignInClicked = false;

    // Automatically start the sign-in flow when the Activity starts
    private boolean mAutoStartSignInFlow = true;
    private DatabaseReference myRef;
    private TextView logo;
    private UserData userData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        Button button = (Button)findViewById(R.id.Leaderboard);
        Button button2 = (Button)findViewById(R.id.submit_score);
        setupLogo();
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("points");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userData = dataSnapshot.getValue(UserData.class);
                logo.setText(""+userData.getPoints_earned());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        mGoogleApiClient = getApiClient();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(getApiClient()),1);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getApiClient().isConnected()) {
                    Games.Leaderboards.submitScore(mGoogleApiClient, getString(R.string.leaderboard_points), 2);
                }
            }
        });
        BottomBarActivity bottomBarActivity = new BottomBarActivity();
        bottomBarActivity.createBottomBar(this,savedInstanceState,TestActivity.this,3);

    }

    private void setupLogo(){
        logo = (TextView)findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(TestActivity.this, HomeScreen.class);
                TestActivity.this.startActivity(myIntent);
            }
        });
        TextView title = (TextView)findViewById(R.id.Title);
        String titleText = "<font color=#404040>Motiv</font><font color=#FFA03E>8</font>";
        Typeface bebasFont = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue Bold.otf");

        title.setText(Html.fromHtml(titleText));
        title.setTypeface(bebasFont);
        title.setTextSize(42);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {

    }
}

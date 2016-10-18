package sjohns70.motive8;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;


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

    // request codes we use when invoking an external activity
    private static final int RC_RESOLVE = 5000;
    private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        messageView = (TextView) findViewById(R.id.messageView);
        Button button = (Button)findViewById(R.id.Leaderboard);
        Button button2 = (Button)findViewById(R.id.submit_score);

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

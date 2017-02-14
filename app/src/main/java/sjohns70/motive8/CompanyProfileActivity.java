/* Copyright statement */

package sjohns70.motive8;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import android.support.v4.app.FragmentActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Locale;

import sjohns70.motive8.data.BusinessData;
import sjohns70.motive8.data.RewardsData;
import sjohns70.motive8.data.UserData;

/**
 * CompanyProfileActivity.java
 *
 * This activity is used to display a company's profile and available coupons. Users should
 * be able to purchasee coupons with their points from this activity.
 */
public class CompanyProfileActivity extends FragmentActivity
        implements OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private ArrayList<RewardsData> rewards;
    private RewardsAdapter adapter;
    private int points;

    private FirebaseDatabase database;
    private DatabaseReference myBusinessRef;
    private FirebaseAuth mAuth;
    private DatabaseReference myUserRef;
    private BusinessData business;
    private UserData userData;
    private GoogleApiClient mGoogleApiClient;

    private TextView name;
    private TextView phone;
    private TextView rating;
    private TextView location;
    private ListView list;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.company_profile);
        name = (TextView) findViewById(R.id.companyName);
        list = (ListView) findViewById(R.id.rewardsList);
        phone = (TextView) findViewById(R.id.business_phone);
        location = (TextView) findViewById(R.id.business_location);
        rating = (TextView) findViewById(R.id.business_rating);
        ratingBar = (RatingBar) findViewById(R.id.business_ratingBar);


        FirebaseApp.initializeApp(getApplicationContext());
        database = FirebaseDatabase.getInstance();
        business = (BusinessData) getIntent().getSerializableExtra("Business");
        rewards = new ArrayList<RewardsData>();

         mGoogleApiClient = new GoogleApiClient.Builder(this)
                 .addApi(Places.GEO_DATA_API)
                 .addApi(Places.PLACE_DETECTION_API)
                 .enableAutoManage(this, 0, this)
                 .addConnectionCallbacks(this)
                 .addOnConnectionFailedListener( this )
                 .build();

        setFonts();
        getPoints();

        if (business.getCompany_name() != null) {
            name.setText(business.getCompany_name());
            getRewards(business.getId());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                phone.setText("Phone " + PhoneNumberUtils.formatNumber(business.getPhone(), Locale.getDefault().getCountry()));
            }
            else {
                phone.setText("Phone " + PhoneNumberUtils.formatNumber(business.getPhone())); //Deprecated method
            }
            
        }

        adapter = new RewardsAdapter(
                rewards,
                Typeface.createFromAsset(getAssets(), "fonts/BebasNeue Regular.otf"),
                Typeface.createFromAsset(getAssets(), "fonts/BebasNeue Bold.otf"));

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int couponCost = ((RewardsData) parent.getItemAtPosition(position)).getValue();
                showSimplePopUp(parent.getItemAtPosition(position).toString(), couponCost);
            }
        });

        //create an instance of the bottom bar
        BottomBarActivity bottomBarActivity = new BottomBarActivity();
        bottomBarActivity.createBottomBar(this, savedInstanceState, CompanyProfileActivity.this, 2);

    }

    /*
    * Here we are gathering all of the rewards from the database
    */
    protected void getRewards(final String businessId){
        myBusinessRef = database.getReference("REWARDS");
        myBusinessRef.keepSynced(true);

        myBusinessRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot data : dataSnapshot.getChildren()) {
                    RewardsData reward = data.getValue(RewardsData.class);
                    if(businessId.equals(reward.getBusiness_id()))
                       rewards.add(reward);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Failed");
            }
        });
    }

    protected void getPoints(){

        mAuth = FirebaseAuth.getInstance();
        myUserRef = database.getReference("USERS").child(mAuth.getCurrentUser().getUid());
        userData = new UserData();

        myUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userData = dataSnapshot.getValue(UserData.class);
                points = userData.getPoints_earned();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
    }
    /*
    * this function creates a popup to show up when a user select one
    * of the companies coupons
    * */
    private void showSimplePopUp(String name, final int CouponsPoint) {
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle(name);
        helpBuilder.setMessage(name + "description");
        helpBuilder.setPositiveButton("Accept",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    if(points >= CouponsPoint){
                        myUserRef.child("points_earned")
                                .setValue(points - CouponsPoint);

                        showDialog();

                        //expanded pop-up with coupon <code></code>
                    }
                    else{
                        //not enough funds
                    }
                    }
                });
        helpBuilder.setNegativeButton("back",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                    }
                });
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }

    /*
    * this function allows us to use custom fonts
    */
    private void setFonts(){
        Typeface custom_font = Typeface
                .createFromAsset(getAssets(),  "fonts/BebasNeue Bold.otf");
        name.setTypeface(custom_font);
    }


    private void showDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.rewards_popup);

        // set the custom dialog components - text, image and button
        ImageButton close = (ImageButton) dialog.findViewById(R.id.btnClose);

        // Close Button
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //TODO Close button action
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }


    private void GoogleInformation() {
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            return;
        }
        System.out.println("OUT here " + business.getPlaceId());

        if (business.getPlaceId().length() < 1 ) {
            return;
        }

        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, business.getPlaceId());
        placeResult.setResultCallback( new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(PlaceBuffer places) {
                if (places.getStatus().isSuccess() && places.getCount() > 0) {
                    System.out.println("places found");
                    final Place myPlace = places.get(0);
                    location.setText(myPlace.getAddress());
                    phone.setText("Phone " + myPlace.getPhoneNumber());
                    rating.setText(Float.toString( myPlace.getRating()) + " Stars");
                    ratingBar.setRating(myPlace.getRating());

                } else {
                    System.out.println("Place not found");
                }
                places.release();
            }
        });
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("connection failed");

    }

    @Override
    protected void onStart() {
        super.onStart();
        if( mGoogleApiClient != null ){
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        System.out.println("connected");
        GoogleInformation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("Not connected");
    }
}


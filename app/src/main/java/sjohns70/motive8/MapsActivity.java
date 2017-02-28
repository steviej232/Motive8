package sjohns70.motive8;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.roughike.bottombar.BottomBar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;


    public static final String TAG = MapsActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final int CIRCLE_RADIUS = 193;
    private BottomBar bottomBar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private Location currentLocation;
    private TrackGPS gps;
    double longitude;
    double latitude;
    public static Circle circle;
    private Intent locationIntent;
    private MarkerOptions markerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        setContentView(R.layout.activity_maps);
        checkPlayServices();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(60 * 1000)        // 60 seconds, in milliseconds
                .setFastestInterval(10 * 1000); // 10 second, in milliseconds

        BottomBarActivity bottomBarActivity = new BottomBarActivity();
        bottomBar = bottomBarActivity.createBottomBar(this, savedInstanceState, MapsActivity.this, 1);
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (currentLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            try {
                handleNewLocation(currentLocation);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleNewLocation(Location location) throws InterruptedException {
        Log.d(TAG, location.toString());
        gps = new TrackGPS(MapsActivity.this);

        if(gps.canGetLocation()) {
            longitude = gps.getLongitude();
            latitude = gps.getLatitude();
            //Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
        }
        LatLng latLng = new LatLng(latitude,longitude);
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here! "+latLng);
        mMap.addMarker(options);
        float zoomLevel = 14; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        findNearbyGyms();
    }

    private void findNearbyGyms(){
        StringBuilder sbValue = new StringBuilder(sbMethod(longitude,latitude));
        PlacesTask placesTask = new PlacesTask();
        placesTask.execute(sbValue.toString());
    }

    public boolean is_inside_circle(MarkerOptions marker, Circle circle){
        float[] distance = new float[2];

        Location.distanceBetween( marker.getPosition().latitude, marker.getPosition().longitude,
                circle.getCenter().latitude, circle.getCenter().longitude, distance);

        if( distance[0] > circle.getRadius()  ){
            return false;
        } else {
            return true;
        }
    }




    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            handleNewLocation(location);
            //Toast.makeText(getApplicationContext(),"Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude(),Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public StringBuilder sbMethod(double longitude, double latitude) {

        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location=" + latitude + "," + longitude);
        sb.append("&radius=3000");
        sb.append("&types=" + "gym");
        sb.append("&sensor=true");
        sb.append("&key=AIzaSyBqvkSKWrYGMGnSWagnZvxXvhqChsBSma8");

        Log.d("Map", "api: " + sb.toString());
        //example string
        //sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-34,151&radius=5000&types=restaurant&sensor=true&key=AIzaSyBqvkSKWrYGMGnSWagnZvxXvhqChsBSma8");
        return  sb;

    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class PlacesTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {
            ParserTask parserTask = new ParserTask();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParserTask
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            PlaceJSON placeJson = new PlaceJSON();

            try {
                jObject = new JSONObject(jsonData[0]);

                places = placeJson.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {
            boolean in_circle = false;
            Log.d("Map", "list size: " + list.size());
            // Clears all the existing markers;
            //mMap.clear();

            for (int i = 0; i < list.size(); i++) {

                // Creating a marker
                markerOptions = new MarkerOptions();

                // Getting a place from the places list
                HashMap<String, String> hmPlace = list.get(i);

                // Getting latitude of the place
                double lat = Double.parseDouble(hmPlace.get("lat"));

                // Getting longitude of the place
                double lng = Double.parseDouble(hmPlace.get("lng"));

                // Getting name
                String name = hmPlace.get("place_name");

                Log.d("Map", "place: " + name);

                // Getting vicinity
                String vicinity = hmPlace.get("vicinity");

                LatLng latLng = new LatLng(lat, lng);

                // Setting the position for the marker
                markerOptions.position(latLng);

                markerOptions.title(name + " : " + vicinity);

                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);

                //Create a circle with radius 200ft around the users current location
                //If the user is inside a gym radius they will begin accumluating points
                circle = mMap.addCircle(new CircleOptions()
                        .center(new LatLng(latitude,longitude))
                        .radius(CIRCLE_RADIUS)
                        .strokeColor(Color.RED)
                        .fillColor(Color.BLUE));
                in_circle = is_inside_circle(markerOptions,circle);
                if(in_circle) {
                    Intent myIntent = new Intent(MapsActivity.this, CircleActivity.class);
                    myIntent.putExtra("gym_latitude",markerOptions.getPosition().latitude);
                    myIntent.putExtra("gym_longitude",markerOptions.getPosition().longitude);
//                    stopService(locationIntent);
                    startActivity(myIntent);
                }
            }
            if(!in_circle) {
                //showSimplePopUp();
                LayoutInflater inflater = getLayoutInflater();
                final View settingsView = inflater.inflate(R.layout.tutorial_dialog, null);

                // Create builders for each dialog
                final AlertDialog.Builder settingsBuilder = new AlertDialog.Builder(MapsActivity.this);
                setupCustomDialog(settingsBuilder,settingsView,R.drawable.logo,R.string.refresh_maps_string_title,R.string.refresh_maps_string,null);
                settingsBuilder.show();
            }
        }


    }
    private void setupCustomDialog(AlertDialog.Builder builder, View customView, int imgResource,
                                   int titleResource, int msgResource, final AlertDialog next) {
        ImageView iv;
        TextView title;
        TextView msg;
        Typeface title_font = Typeface.createFromAsset(getAssets(), "fonts/Caviar_Dreams_Bold.ttf");
        Typeface message_font = Typeface.createFromAsset(getAssets(), "fonts/CaviarDreams.ttf");

        builder.setView(customView);

            builder.setPositiveButton("Refresh Map", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Show Map", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });


        // Obtain refereces to xml objects
        iv = (ImageView) customView.findViewById(R.id.tutorial_dialogImg);
        title = (TextView) customView.findViewById(R.id.tutorial_dialogTitle);
        msg = (TextView) customView.findViewById(R.id.tutorial_dialogText);

        // Set xml object contents
        iv.setImageResource(imgResource);
        title.setText(titleResource);
        title.setTypeface(title_font);
        msg.setText(msgResource);
        msg.setTypeface(message_font);

    }

    public Circle getCircle(){
        return circle;
    }


}

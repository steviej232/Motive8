package sjohns70.motive8;

/**
 * Created by Steven on 9/14/2016.
 *
 * This activity is used to display all of the companies that have coupons
 * available on our app
 *
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CompanyListActivity extends Activity{
    private CustomListAdapter adapter;
    private ArrayList<BusinessData> businesses;
    private DatabaseReference myRef;
    private FirebaseDatabase database;

    private TextView logo;
    private UserData userData;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView list;

        setContentView(R.layout.company_list);
        list = (ListView) findViewById(R.id.list);

        setupLogo();

        FirebaseApp.initializeApp(getApplicationContext());
        database = FirebaseDatabase.getInstance();

        getCompanies();
        getPoints();

        adapter = new CustomListAdapter(businesses, Typeface.createFromAsset(
                getAssets(),  "fonts/Montserrat-Bold.otf"),
                Typeface.createFromAsset(getAssets(),  "fonts/Montserrat-Light.otf"));

        list.setAdapter(adapter);

        //TODO: find a better way to pass data
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(CompanyListActivity.this,
                        CompanyProfileActivity.class);
                Bundle bun = new Bundle();
                bun.putSerializable("Business", businesses.get(position));
                myIntent.putExtras(bun);
                parent.getContext().startActivity(myIntent);
            }
        });

        //create bottom bar instance
        BottomBarActivity bottomBarActivity = new BottomBarActivity();
        bottomBarActivity.createBottomBar(this, savedInstanceState,
                CompanyListActivity.this, 2);

    }


    /*
    *
    * */
    private void setupLogo(){
        logo = (TextView)findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(CompanyListActivity.this, HomeScreen.class);
                CompanyListActivity.this.startActivity(myIntent);
            }
        });
        TextView title = (TextView)findViewById(R.id.Title);
        String titleText = "<font color=#404040>Motiv</font><font color=#FFA03E>8</font>";
        Typeface bebasFont = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue Bold.otf");

        title.setText(Html.fromHtml(titleText));
        title.setTypeface(bebasFont);
        title.setTextSize(42);
    }

    private void getCompanies() {
        myRef = database.getReference("BUSINESSES");
        myRef.keepSynced(true);
        businesses = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot data : dataSnapshot.getChildren()) {
                    BusinessData bus = data.getValue(BusinessData.class);
                    businesses.add(bus);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Failed");
            }
        });

    }
    
    private void getPoints(){
        myRef = database.getReference("points");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userData = dataSnapshot.getValue(UserData.class);
                logo.setText("" + userData.getPoints_earned());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}


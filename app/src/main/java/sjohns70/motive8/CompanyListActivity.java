package sjohns70.motive8;

/**
 * Created by Steven on 9/14/2016.
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
    private DatabaseReference myRef2;
    private TextView logo;
    private UserData userData;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListView list;
        DatabaseReference myRef;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_list);
        setupLogo();

        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("BUSINESSES");
        myRef.keepSynced(true);
        myRef2 = database.getReference("points");

        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userData = dataSnapshot.getValue(UserData.class);
                logo.setText(""+userData.getPoints_earned());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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

        adapter = new CustomListAdapter(businesses, Typeface.createFromAsset(getAssets(),  "fonts/Montserrat-Bold.otf"),
                                          Typeface.createFromAsset(getAssets(),  "fonts/Montserrat-Light.otf"));
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(CompanyListActivity.this, CompanyProfileActivity.class);
                Bundle bun = new Bundle();
                bun.putSerializable("Business", businesses.get(position));
                myIntent.putExtras(bun);
                parent.getContext().startActivity(myIntent);
            }
        });

        BottomBarActivity bottomBarActivity = new BottomBarActivity();
        bottomBarActivity.createBottomBar(this,savedInstanceState,CompanyListActivity.this, 2);

    }

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

}


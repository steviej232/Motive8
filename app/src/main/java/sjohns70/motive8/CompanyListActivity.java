package sjohns70.motive8;

/**
 * Created by Steven on 9/14/2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.roughike.bottombar.BottomBar;


public class CompanyListActivity extends Activity{

    ListView list;
    String[] itemname ={
            "Adidas",
            "Amazon",
            "Apple",
            "Baskin Robbins",
            "Dell",
            "Money",
            "Dominos",
            "Intel",
            "Jamba Juice",
            "Nasa",
            "Pepsi",
            "Starbucks",
            "Target"
    };

    Integer[] imgid={
            R.drawable.adidas,
            R.drawable.amazon,
            R.drawable.apple,
            R.drawable.baskin_robbins,
            R.drawable.dell,
            R.drawable.dollar,
            R.drawable.dominos,
            R.drawable.intel,
            R.drawable.jamba_juice,
            R.drawable.nasa,
            R.drawable.pepsi,
            R.drawable.starbucks,
            R.drawable.target,
    };
    BottomBar bottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_list);
        Intent intent = getIntent();
        int curTab=0;
        intent.getIntExtra("currentTab",curTab);

        CustomListAdapter adapter=new CustomListAdapter(this, itemname, imgid);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent myIntent = new Intent(CompanyListActivity.this, CompanyProfileActivity.class);
                System.out.println(parent.getItemAtPosition(position).toString());
                myIntent.putExtra("companyName", parent.getItemAtPosition(position).toString());
                myIntent.putExtra("position", position);
                startActivity(myIntent);

            }
        });
        BottomBarActivity bottomBarActivity = new BottomBarActivity();
        bottomBar = bottomBarActivity.createBottomBar(this,savedInstanceState,CompanyListActivity.this,2);


    }

}


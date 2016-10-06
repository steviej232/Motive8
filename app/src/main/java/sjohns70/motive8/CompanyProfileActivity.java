package sjohns70.motive8;

/**
 * Created by Kendall on 10/2/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class CompanyProfileActivity extends AppCompatActivity {

    private String companyName;
    private ListView list;

    private Point p;
    private String[] rewardsName;

    private Integer[] imgid={
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);

        companyName = getIntent().getStringExtra("companyName");
        int position = getIntent().getIntExtra("position", 0);

        if (companyName != null) { //change all values to database values
            TextView name = (TextView) findViewById(R.id.companyName);
            name.setText(companyName);
            TextView description = (TextView) findViewById(R.id.businessDescription);
            description.setText("description for " + companyName);
            ImageView imageView = (ImageView) findViewById(R.id.imageView2);
            imageView.setImageResource(imgid[position]);
            rewardsName = getRewards(position);

        }

        CustomListAdapter adapter = new CustomListAdapter(this, rewardsName, imgid); //need to replace images with database value
        list = (ListView) findViewById(R.id.rewardsList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showSimplePopUp(parent.getItemAtPosition(position).toString());
            }
        });

    }

    protected String[] getRewards(int position){
        //this will eventually get values out of the database
        String[] rewards = {"discount1", "discount2", "discount3"};
        return rewards;
    }


    private void showSimplePopUp(String name) {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle(name);
        helpBuilder.setMessage(name + "description");
        helpBuilder.setPositiveButton("Accept",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //we will need to update the database
                    }
                });
        helpBuilder.setNegativeButton("back",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                    }
                });
        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }

}


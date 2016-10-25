package sjohns70.motive8;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectedListener;

/**
 * Created by Steven on 10/6/2016.
 */

public class BottomBarActivity {

    public BottomBar bottomBar;


    public BottomBar createBottomBar(Activity activity, final Bundle savedInstanceState, final Context className,int tab_position){
        bottomBar = BottomBar.attach(activity, savedInstanceState);
        bottomBar.setItems(
                new BottomBarTab(R.drawable.ic_home_black_24dp, "Home"),
                new BottomBarTab(R.drawable.ic_nearby, "Location"),
                new BottomBarTab(R.drawable.ic_card_giftcard_black_24dp, "Coupons"),
                new BottomBarTab(R.drawable.ic_library_add_black_24dp, "More")
        );
        bottomBar.setDefaultTabPosition(tab_position);
        bottomBar.setOnItemSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                Intent myIntent = null;
                switch (position) {
                    case 0:
                        myIntent = new Intent(className, HomeScreen.class);
                        className.startActivity(myIntent);
                        break;
                    case 1:
                        myIntent = new Intent(className, MapsActivity.class);
                        //myIntent = new Intent(className, CircleActivity.class);
                        className.startActivity(myIntent);
                        break;
                    case 2:
                        myIntent = new Intent(className,CompanyListActivity.class);
                        className.startActivity(myIntent);
                        break;
                    case 3:
                        myIntent = new Intent(className, TestActivity.class);
                        break;
                }
                className.startActivity(myIntent);
            }
        });
        bottomBar.mapColorForTab(0, "#3B494C");
        bottomBar.mapColorForTab(1, "#00796B");
        bottomBar.mapColorForTab(2, "#7B1FA2");
        bottomBar.mapColorForTab(3, "#FF5252");

        // Make a Badge for the first tab, with red background color and a value of "4".
        BottomBarBadge unreadMessages = bottomBar.makeBadgeForTabAt(1, "#E91E63", 4);

        // Control the badge's visibility
        unreadMessages.show();
        //unreadMessages.hide();

        // Change the displayed count for this badge.
        //unreadMessages.setCount(4);

        // Change the show / hide animation duration.
        unreadMessages.setAnimationDuration(200);

        // If you want the badge be shown always after unselecting the tab that contains it.
        unreadMessages.setAutoShowAfterUnSelection(true);


        // Set the color for the active tab. Ignored on mobile when there are more than three tabs.
        bottomBar.setActiveTabColor("#C2185B");


        // Use custom text appearance in tab titles.
        bottomBar.setTextAppearance(R.style.BB_BottomBarItem_Fixed);

        return bottomBar;
    }

}

package sjohns70.motive8;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Steven on 9/29/2016.
 */

public class TestActivity extends AppCompatActivity{


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        HomeScreen hs = new HomeScreen();
        hs.createBottomBar(this,savedInstanceState, TestActivity.this);

    }


}

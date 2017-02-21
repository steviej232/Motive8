package sjohns70.motive8;

/**
 * Created by Steven on 2/15/2017.
 */

import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstIdService extends FirebaseInstanceIdService{

    private static final String TAG = "MyAndroidFCMIIDService";

    @Override
    public void onTokenRefresh() {
        //Get hold of the registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log the token
        Toast.makeText(getApplicationContext(),"refreshed:"+refreshedToken,Toast.LENGTH_SHORT).show();
    }
    private void sendRegistrationToServer(String token) {
        //Implement this method if you want to store the token on your server
    }
}
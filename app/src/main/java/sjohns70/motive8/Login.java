package sjohns70.motive8;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by KendallGassner on 10/18/16.
 */

public class Login  extends AppCompatActivity {

    private Button submit;
    private TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        submit = (Button) findViewById(R.id.submit_login);
        signup = (TextView) findViewById(R.id.sign_up);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(Login.this, HomeScreen.class);
                startActivity(home);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(Login.this, SignUp.class);
                startActivity(signup);
            }
        });
    }
}

/* Copyright statement */

package sjohns70.motive8;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


//TODO not doing anything with signup name

/**
 * SignUp.java
 *
 * This class handles creating new user accounts.
 */
public class SignUp extends AppCompatActivity {

    private String TAG = "SignUp page";
    private EditText sign_up_email;
    private EditText sign_up_password;
    private EditText   sign_up_name;
    private Button   sign_up_submit;
    private String email;
    private String password;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        FirebaseApp.initializeApp(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();

        sign_up_email = (EditText) findViewById(R.id.sign_up_email);
        sign_up_password = (EditText) findViewById(R.id.sign_up_password);
        sign_up_name = (EditText) findViewById(R.id.sign_up_name);
        sign_up_submit = (Button) findViewById(R.id.sign_up_submit);

        setupFont();

        sign_up_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

    }



    private void createAccount() {
        email = sign_up_email.getText().toString();
        password = sign_up_password.getText().toString();
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            myRef = database.getReference("USERS");
                            myRef.child(mAuth.getCurrentUser().getUid()).child("points_earned").setValue(0);
                            myRef.child(mAuth.getCurrentUser().getUid()).child("count_remainder").setValue(0);
                            myRef.child(mAuth.getCurrentUser().getUid()).child("email").setValue(
                                    mAuth.getCurrentUser().getEmail().toString());
                            Intent home = new Intent(SignUp.this, HomeScreen.class);
                            startActivity(home);
                        }
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUp.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = sign_up_email.getText().toString();
        if (TextUtils.isEmpty(email)) {
            sign_up_email.setError("Required.");
            valid = false;
        } else {
            sign_up_email.setError(null);
        }

        String password = sign_up_password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            sign_up_password.setError("Required.");
            valid = false;
        } else {
            sign_up_password.setError(null);
        }

        return valid;
    }

    /**
     * This method sets up the fonts on the signup page
     */
    private void setupFont() {
        TextView title = (TextView)findViewById(R.id.sign_up_title);
        String titleText = "<font color=#404040>Motiv</font><font color=#FFA03E>8</font>";

        Typeface bebasFont_bold = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue Bold.otf");
        Typeface bebasFont = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue Regular.otf");

        title.setText(Html.fromHtml(titleText));
        title.setTypeface(bebasFont_bold);
        sign_up_name.setTypeface(bebasFont);
        sign_up_email.setTypeface(bebasFont);
        sign_up_password.setTypeface(bebasFont);
        sign_up_submit.setTypeface(bebasFont_bold);
        title.setTextSize(100);

    }

}

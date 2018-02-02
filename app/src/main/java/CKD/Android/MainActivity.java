package CKD.Android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser logged_In_User= AppData.firebaseUser;

        // TODO Compare FireBase User to AppData.cur_User as well
       /// Check to see if the User is logged in. If they are bring them to the home page
        if(logged_In_User != null ) {
            Intent launchActivity1 = new Intent(MainActivity.this, HomePage.class);
            startActivity(launchActivity1);
        }

        Button Login = findViewById(R.id.Main_Btn_Login);

        Button Register = findViewById(R.id.Main_Btn_Register);

        // OnClick Listener redirects to Login Page
        Login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent launchActivity1 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(launchActivity1);
            }
        });

        // OnClick Listener that redirects to Register Page
        Register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent launchActivity1 = new Intent(MainActivity.this, registerNewUser.class);
                startActivity(launchActivity1);
            }
        });

        }
    }



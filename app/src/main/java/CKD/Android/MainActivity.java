package CKD.Android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button Login = findViewById(R.id.Main_Btn_Login);

        Button Register = findViewById(R.id.Main_Btn_Register);

        // OnClick Listener redirects to Login Page
        Login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Check to see if the User is logged in. If they are bring them to the home page
                // TODO AppData.getInstance().mAuth is null when the Firebase says the user is still logged
                // TODO in. We need to make sure that mAuth is receiving the something other than null here.
                if(FirebaseAuth.getInstance().getCurrentUser() != null)
                // if(FirebaseAuth.getInstance().getCurrentUser() != null && AppData.getInstance().mAuth !=null)
                {
                    Intent launchActivity1 = new Intent(MainActivity.this, HomePage.class);
                    startActivity(launchActivity1);
                }
                else {
                    Intent launchActivity1 = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(launchActivity1);
                }

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



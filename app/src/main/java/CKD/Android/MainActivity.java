package CKD.Android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    // Initialize variables
    private Button Login;
    private Button Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Grab Login Button by ID
        Button Login = findViewById(R.id.Main_Btn_Login);

        // OnClick Listener redirects to Login Page
        Login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent launchActivity1= new Intent(MainActivity.this,LoginActivity.class);
                startActivity(launchActivity1);
            }
        });

        // Grab Register Button By ID
        Button Register = findViewById(R.id.Main_Btn_Register);

        // OnClick Listener that redirects to Register Page
        Register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent launchActivity1= new Intent(MainActivity.this,registerNewUser.class);
                startActivity(launchActivity1);
            }
        });

    }
}

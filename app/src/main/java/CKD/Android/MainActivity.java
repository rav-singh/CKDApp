package CKD.Android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    FirebaseAuth Authenticator = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button Login = findViewById(R.id.Main_Btn_Login);

        Button Register = findViewById(R.id.Main_Btn_Register);

        ImageView image = findViewById(R.id.Main_IV_logo);
        image.setImageResource(R.drawable.logo_transparent);

        isUserLoggedIn();

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

    private void isUserLoggedIn()
    {
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            updateFirebaseInAppData();
            loadUserClass();

            Intent launchActivity1 = new Intent(MainActivity.this, HomePage.class);
            startActivity(launchActivity1);
        }
    }

    private void loadUserClass()
    {
        AppData.setCur_User( new UserClass(null,null,
                null,null,null,null));

        String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        AppData.cur_user.setUID(userUID);

        DatabaseReference users_node = AppData.db.getReference("Users");
        DatabaseReference cur_user_node = users_node.child(userUID);
   //     DatabaseReference add_node = users_node.child(userUID).child("Additional");
        DatabaseReference schedule_node = cur_user_node.child("Schedule");

        AppData.readFromDatabaseAndWriteToAppData(cur_user_node,"Name");
        AppData.readFromDatabaseAndWriteToAppData(cur_user_node,"Email");
        AppData.readFromDatabaseAndWriteToAppData(cur_user_node,"Phone Number");
        AppData.WriteDaysToAppData(schedule_node);

        // The App Never refers to these values in the code. These are values are only written
        // to the database but never read from in the app or from the AppData.Cur_user class.
        // Commented these out to increase efficiency until they are needed to be read

    /*    readFromDatabaseAndWriteToAppData(add_node,"Activity Level");
        readFromDatabaseAndWriteToAppData(add_node,"Age");
        readFromDatabaseAndWriteToAppData(add_node,"Education");
        readFromDatabaseAndWriteToAppData(add_node,"Gender");
        readFromDatabaseAndWriteToAppData(add_node,"Health");
        readFromDatabaseAndWriteToAppData(add_node,"Marital Status");
        readFromDatabaseAndWriteToAppData(add_node,"Work");
       */
    }


    private void updateFirebaseInAppData()
    {
        AppData.setmAuth(Authenticator);
        AppData.setFirebaseUser(Authenticator.getCurrentUser());
        AppData.setDb(FirebaseDatabase.getInstance());
    }
    }



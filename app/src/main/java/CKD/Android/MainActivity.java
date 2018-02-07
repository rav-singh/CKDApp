package CKD.Android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.util.Log;

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

        // OnClick Listener redirects to Login Page
        Login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Check to see if the User is logged in. If they are bring them to the home page
                if(FirebaseAuth.getInstance().getCurrentUser() != null)
                {
                    updateFirebaseInAppData();
                    loadUserClass();

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

    private void loadUserClass()
    {
        AppData.setCur_User( new UserClass(null,null,
                null,null,null,null));

        String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        AppData.cur_user.setUID(userUID);

        DatabaseReference users_node = AppData.db.getReference("Users");
        DatabaseReference cur_user_node = users_node.child(userUID);
        DatabaseReference add_node = users_node.child(userUID).child("Additional");

        readFromDatabaseAndWriteToAppData(cur_user_node,"Name");
        readFromDatabaseAndWriteToAppData(cur_user_node,"Email");
        readFromDatabaseAndWriteToAppData(cur_user_node,"Phone Number");
        readFromDatabaseAndWriteToAppData(add_node,"Activity Level");
        readFromDatabaseAndWriteToAppData(add_node,"Age");
        readFromDatabaseAndWriteToAppData(add_node,"Education");
        readFromDatabaseAndWriteToAppData(add_node,"Gender");
        readFromDatabaseAndWriteToAppData(add_node,"Health");
        readFromDatabaseAndWriteToAppData(add_node,"Marital Status");
        readFromDatabaseAndWriteToAppData(add_node,"Work");
    }

    private void readFromDatabaseAndWriteToAppData(DatabaseReference node, final String key)
    {
        node.child(key).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                // Returns the value under the node passed in at the key passed in
                String value = dataSnapshot.getValue(String.class);

                if(Objects.equals(key, "Name"))
                    AppData.cur_user.setName(value);

                else if(Objects.equals(key, "Email"))
                    AppData.cur_user.setEmail(value);

                else if (Objects.equals(key, "Phone Number"))
                    AppData.cur_user.setPhone(value);

                else if (Objects.equals(key, "Age"))
                    AppData.cur_user.setAge(value);

                else if (Objects.equals(key, "Activity Level"))
                    AppData.cur_user.setActivityLevel(value);

                else if (Objects.equals(key, "Marital Status"))
                    AppData.cur_user.setMarital(value);

                else if (Objects.equals(key, "Gender"))
                    AppData.cur_user.setGender(value);

                else if (Objects.equals(key, "Education"))
                    AppData.cur_user.setEducation(value);

                else if (Objects.equals(key, "Work"))
                    AppData.cur_user.setWork(value);

                else if (Objects.equals(key, "Health"))
                    AppData.cur_user.setHealth(value);

            }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {
            //TODO What happens when theres an Error
            Log.i("DATABASE READ ERROR",
                    databaseError.getMessage().toString());
            Log.i("DATABASE READ ERROR",
                    databaseError.getMessage().toString());
            Log.i("DATABASE READ ERROR",
                    databaseError.getMessage().toString());
        }});
    }

    private void updateFirebaseInAppData()
    {
        AppData.setmAuth(Authenticator);
        AppData.setFirebaseUser(Authenticator.getCurrentUser());
        AppData.setDb(FirebaseDatabase.getInstance());
    }
    }



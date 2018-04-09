package CKD.Android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.net.Authenticator;
import java.util.ArrayList;
import java.util.List;

public class Demographics_2 extends AppCompatActivity
{

   FirebaseAuth Authenticator = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demographics_2);

        final Spinner userAge = findViewById(R.id.Demo_SPN_Age);
        final Spinner userMarital = findViewById(R.id.Demo_SPN_Marital);
        final EditText userGender = findViewById(R.id.Demo_PT_Gender);
        final Button next = findViewById(R.id.Demo_BTN_Next_2);
        final CheckBox race1 = findViewById(R.id.Demo_CB_Race_1);
        final CheckBox race2 = findViewById(R.id.Demo_CB_Race_2);
        final CheckBox race3 = findViewById(R.id.Demo_CB_Race_3);
        final CheckBox race4 = findViewById(R.id.Demo_CB_Race_4);
        final CheckBox race5 = findViewById(R.id.Demo_CB_Race_5);
        final CheckBox race6 = findViewById(R.id.Demo_CB_Race_6);
        final List<String> raceList = new ArrayList<>();

        next.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Grabs all user input into strings
                String _Age = userAge.getSelectedItem().toString();
                String _Marital = userMarital.getSelectedItem().toString();
                String _Gender = userGender.getText().toString();

                collectSelectedRaces();

                if(_Gender.contains("my"))
                {
                    _Gender = "User did not enter gender";
                }
                // Adds users input into Current_user Class in AppData
                addUserClassValues(_Age, _Marital, _Gender, raceList);

                //TODO Clear password in User Class once its used
                // Create User in Firebase Authentication
                createUserAuth(AppData.cur_user.getEmail(),
                        AppData.cur_user.getPassword());



                // Directs User to HomePage
                switchPages();
            }

            private void collectSelectedRaces()
            {
                // After User clicks register the races selected are placed into a temporary ArrayList
                // containing all of the races
                if (race1.isChecked()) {raceList.add(race1.getText().toString());}
                if (race2.isChecked()){raceList.add(race2.getText().toString());}
                if (race3.isChecked()){raceList.add(race3.getText().toString());}
                if (race4.isChecked()){raceList.add(race4.getText().toString());}
                if (race5.isChecked()){raceList.add(race5.getText().toString());}
                if (race6.isChecked()) {raceList.add(race6.getText().toString());}

            }
        });
    }

    protected void addCurrentUserToDatabase(UserClass cur_user)
    {
        //Grabs references to the Root node and Users Node
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        //Creates a UID Node under Users
        DatabaseReference User_node = db.getReference("Users");

        //Grabs the UID Created by Firebase Auth and stored in Current user class
        String UID = cur_user.getUID();

        //References Nodes that will have children
        DatabaseReference UID_node = User_node.child(UID);
        DatabaseReference Add_node = User_node.child(UID).child("Additional");
        DatabaseReference Race_node = Add_node.child("Races");
        DatabaseReference morbid_node = Add_node.child("Diseases");

        //Adds values to above defined nodes
        UID_node.child("Name").setValue(AppData.cur_user.getName());
        UID_node.child("Email").setValue(AppData.cur_user.getEmail());
        UID_node.child("Phone Number").setValue(AppData.cur_user.getPhone());
        Add_node.child("Activity Level").setValue(AppData.cur_user.getActivityLevel());
        Add_node.child("Marital Status").setValue(AppData.cur_user.getMarital());
        Add_node.child("Gender").setValue(AppData.cur_user.getGender());
        Add_node.child("Age").setValue(AppData.cur_user.getAge());
        Add_node.child("Work").setValue(AppData.cur_user.getWorkStatus());
        Add_node.child("Health").setValue(AppData.cur_user.getHealthRating());
        Add_node.child("Education").setValue(AppData.cur_user.getEducation());

        for(int i = 0; i< AppData.cur_user.getCoMorbs().size(); i++)
        {
            morbid_node.child(String.valueOf(i)).setValue(AppData.cur_user.getCoMorbs().get(i));
        }

        for(int i = 0; i< AppData.cur_user.getRace().size(); i++)
        {
            Race_node.child(String.valueOf(i)).setValue(AppData.cur_user.getRace().get(i));
        }

        DatabaseReference Schedule_node = User_node.child(UID).child("Schedule");

        for(String s : AppData.cur_user.getScheduledDays())
        {
            DatabaseReference Day_node = Schedule_node.child(s);

            String startTime = AppData.cur_user.getScheduledStartTime().get(s);
            String endTime = AppData.cur_user.getScheduledEndTime().get(s);

            Day_node.child("StartTime").setValue(startTime);
            Day_node.child("EndTime").setValue(endTime);
        }


    }

    private void createUserAuth(String newEmail, String newPassword)
    {
        Task<AuthResult> Auth =
                    Authenticator.createUserWithEmailAndPassword(newEmail, newPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                             {
                                    @Override
                                public void onComplete(@NonNull Task<AuthResult> task)
                                {
                            // User Successfully Registered to database
                            if (task.isSuccessful())
                            {
                                AuthResult result = task.getResult();

                                AppData.setmAuth(Authenticator);
                                AppData.setDb(FirebaseDatabase.getInstance());
                                AppData.setFirebaseUser(Authenticator.getCurrentUser());
                                AppData.cur_user.setUID(AppData.firebaseUser.getUid());
                                AppData.cur_user.clearPassword();

                                addCurrentUserToDatabase(AppData.cur_user);
                            }
                            // User unable to register_class user to database
                            else
                            {
                                // print out error when we can't register user
                                String error_Message = task.getException().toString();
                                if(error_Message.contains("password"))
                                {
                                    Toast.makeText(Demographics_2.this,
                                            "Sorry but your password must be atleast 6 characters long",
                                            Toast.LENGTH_LONG).show();
                                }
                                else if(error_Message.contains("email"))
                                {
                                    Toast.makeText(Demographics_2.this,
                                            "Invalid Email!", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(Demographics_2.this,
                                                    error_Message,
                                                    Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });

    }

    private void switchPages()
    {
        AppData.isUserRegistering = true;
        Intent launchActivity1 = new Intent(CKD.Android.Demographics_2.this, Mood.class);
        startActivity(launchActivity1);
    }

    private void addUserClassValues(String userAge, String userMarital, String userGender, List<String> races)
    {
        AppData.cur_user.setAge(userAge);
        AppData.cur_user.setMarital(userMarital);
        AppData.cur_user.setGender(userGender);
        AppData.cur_user.setRace(races);
    }

}


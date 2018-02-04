package CKD.Android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
        final List<String> raceList = new ArrayList<String>();

        next.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // checked makes sure that each race node gets a unique value associated with it,
                // so that we can insert multiple races.

                // Grabs all user input into strings
                String _Age = userAge.getSelectedItem().toString();
                String _Marital = userMarital.getSelectedItem().toString();
                String _Gender = userGender.getText().toString();
                String _Race = "";

                collectSelectedRaces();

                // Adds users input into Current_user Class in AppData
                addUserClassValues(_Age, _Marital, _Gender, raceList);

                //TODO Clear password in User Class once its used
                // Create User in Firebase Authentication
                // Email and Password were set to this class in registerNewUserClass
                createUserAuth(AppData.cur_user.getEmail(), AppData.cur_user.getPassword());

                //Update AppData mAuth Reference
                AppData.getInstance().setmAuth(FirebaseAuth.getInstance());

                // Adds User to RealTime Database under Users Node
                addCurrentUserToDatabase();
                // Directs User to HomePage
                switchPages();
            }

            private void collectSelectedRaces()
            {
                // After User clicks register the races selected are placed into a temporary ArrayList
                // containing all of the races
                if (race1.isChecked()) {
                    raceList.add(race1.getText().toString());
                    //_Race += race1.getText().toString();
                    // addRaceToDatabase(race1.getText().toString(), checked, AppData.cur_user.getUID());
                    //checked++;
                }
                if (race2.isChecked()){
                    raceList.add(race2.getText().toString());
//                    if(checked > 0)
                    //                      _Race += ",";
                    //                _Race += race2.getText().toString();
                    // addRaceToDatabase(race2.getText().toString(), checked, AppData.cur_user.getUID());
                   // checked++;
                }
                if (race3.isChecked()){
                    raceList.add(race3.getText().toString());
                    //                 if(checked > 0)
                    //                 _Race += ",";
                    //           _Race += race3.getText().toString();
                    // addRaceToDatabase(race3.getText().toString(), checked, AppData.cur_user.getUID());
                  //  checked++;
                }
                if (race4.isChecked()){
                    raceList.add(race4.getText().toString());

                    // if(checked > 0)
                    //   _Race += ",";
                    // _Race += race4.getText().toString();
                    //  addRaceToDatabase(race4.getText().toString(),checked, AppData.cur_user.getUID());
                  //  checked++;
                }
                if (race5.isChecked()){
                    raceList.add(race5.getText().toString());
                    //if(checked > 0)
                    //  _Race += ",";
                    //_Race += race5.getText().toString();
                    // addRaceToDatabase(race5.getText().toString(), checked, AppData.cur_user.getUID());
                    //checked++;
                }
                if (race6.isChecked()) {
                    raceList.add(race6.getText().toString());
                    //if(checked > 0)
                    //  _Race += ",";
                    //_Race += race6.getText().toString();
                    // addRaceToDatabase(race6.getText().toString(), checked, AppData.cur_user.getUID());
                }

            }
        });
    }

    private void addCurrentUserToDatabase()
    {
        //Grabs references to the Root node and Users Node
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference User_node = db.getReference("Users");

        // Saves the newly generated UID from Firebase into UserClass
        AppData.cur_user.setUID(AppData.getInstance().getmAuth().getCurrentUser().getUid());

        //Creates a copy of the current user in AppData Class
        UserClass tempUser = AppData.cur_user;

        String UID = tempUser.getUID();

        //Creates a UID Node under Users
        DatabaseReference UID_node = User_node.child(UID);
        //Creates a node under UID
        DatabaseReference Add_node = User_node.child(UID).child("Additional");
        DatabaseReference Race_node = User_node.child(UID).child("Race");

        UID_node.child("Name").setValue(tempUser.getName());
        UID_node.child("Email").setValue(tempUser.getEmail());
        UID_node.child("Phone Number").setValue(tempUser.getPhone());
        Add_node.child("Activity Level").setValue(tempUser.getActivityLevel());
        Add_node.child("Marital Status").setValue(tempUser.getMarital());
        Add_node.child("Gender").setValue(tempUser.getGender());

        for(int i = 0; i< tempUser.getRace().size(); i++)
        {
            Race_node.child(String.valueOf(i)).setValue(tempUser.getRace().get(i));
        }
    }


    private void addCurrentUserToDatabasePrevious()
    {


/*
        DatabaseReference UID_node = User_node.child(UID);
        DatabaseReference Add_node = User_node.child(UID).child("Additional");
        DatabaseReference Race_node = User_node.child(UID).child("Race");


        UID_node.child("Name").setValue("Just Checking");
        UID_node.child("Email").setValue("If working");
        UID_node.child("Phone Number").setValue("Properly");
        Add_node.child("Activity Level").setValue("Don't mind");
        Add_node.child("Marital Status").setValue("me");
        Add_node.child("Gender").setValue("Testing");

        for(int i = 0; i< currentUser.getRace().size(); i++)
        {
            Race_node.child(String.valueOf(i)).setValue(raceList.get(i));
        }
*/
    }


    private void createUserAuth(String newEmail, String newPassword)
    {
        //TODO Change back to AppData Reference
        Task<AuthResult> Auth =
                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(newEmail, newPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                             {
                                    @Override
                                public void onComplete(@NonNull Task<AuthResult> task)
                                {
                                    // User Successfully Registered to database
                                    if (task.isSuccessful())
                                    {
                                        AuthResult result = task.getResult();
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

    private void addRaceToDatabase(String userRace, int i, String UID)
    {
        FirebaseDatabase db = AppData.db;
        DatabaseReference User_node = db.getReference("Users");
        DatabaseReference Race_node = User_node.child(UID).child("Race");
        Race_node.child(String.valueOf(i)).setValue(userRace);
    }

    private void addUserToDatabase(String userAge, String userMarital, String userGender, String UID)
    {
        FirebaseDatabase db = AppData.db;
        DatabaseReference User_node = db.getReference("Users");
        DatabaseReference Add_node = User_node.child(UID).child("Additional");

        User_node.child(UID).child("Age").setValue(userAge);
        Add_node.child("Marital Status").setValue(userMarital);
        Add_node.child("Gender").setValue(userGender);
    }
}


package CKD.Android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        next.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // checked makes sure that each race node gets a unique value associated with it,
                // so that we can insert multiple races.
                // TODO put this race thing in it's own function
                int checked = 0;
                // Grabs all user input into strings
                String _Age = userAge.getSelectedItem().toString();
                String _Marital = userMarital.getSelectedItem().toString();
                String _Gender = userGender.getText().toString();
                String _Race = "";

                if (race1.isChecked()) {
                    _Race += race1.getText().toString();
                    addRaceToDatabase(race1.getText().toString(), checked, AppData.cur_user.getUID());
                    checked++;
                }
                if (race2.isChecked()){
                    if(checked > 0)
                        _Race += ",";
                    _Race += race2.getText().toString();
                    addRaceToDatabase(race2.getText().toString(), checked, AppData.cur_user.getUID());
                    checked++;
                }
                if (race3.isChecked()){
                    if(checked > 0)
                        _Race += ",";
                    _Race += race3.getText().toString();
                    addRaceToDatabase(race3.getText().toString(), checked, AppData.cur_user.getUID());
                    checked++;
                }
                if (race4.isChecked()){
                    if(checked > 0)
                        _Race += ",";
                    _Race += race4.getText().toString();
                    addRaceToDatabase(race4.getText().toString(),checked, AppData.cur_user.getUID());
                    checked++;
                }
                if (race5.isChecked()){
                    if(checked > 0)
                        _Race += ",";
                    _Race += race5.getText().toString();
                    addRaceToDatabase(race5.getText().toString(), checked, AppData.cur_user.getUID());
                    checked++;
                }
                if (race6.isChecked()) {
                    if(checked > 0)
                        _Race += ",";
                    _Race += race6.getText().toString();
                    addRaceToDatabase(race6.getText().toString(), checked, AppData.cur_user.getUID());
                }

                // Adds users input into Current_user Class in AppData
                addUserClassValues(_Age, _Marital, _Gender);

                // Adds User to RealTime Database under Users Node
                addUserToDatabase(_Age,_Marital, _Gender, AppData.cur_user.getUID());
                // Directs User to HomePage
                switchPages();
            }
        });
    }

    private void switchPages()
    {
        Intent launchActivity1 = new Intent(CKD.Android.Demographics_2.this, Mood.class);
        startActivity(launchActivity1);
    }

    private void addUserClassValues(String userAge, String userMarital, String userGender)
    {
        AppData.cur_user.setAge(userAge);
        AppData.cur_user.setMarital(userMarital);
        AppData.cur_user.setGender(userGender);
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


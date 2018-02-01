package CKD.Android;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Demographics extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demographics);

        // UI Components
        final EditText Name = findViewById(R.id.Demo_TF_Name);
        final EditText Email =  findViewById(R.id.Demo_TF_Email);
        final EditText Phone = findViewById(R.id.Demo_TF_Phone);
        final EditText Age = findViewById(R.id.Demo_TF_Age);
        final EditText ActivityLevel = findViewById(R.id.Demo_TF_ActivityL);
        final EditText UID = findViewById(R.id.Demo_TF_UID);
        final Button Register = findViewById(R.id.Demo_Btn_Register);

        //  Fills in Name and Email Data for profile
        // May be excessive and might simplify by removing name from Register Page
        Email.setText(AppData.cur_user.getEmail());
        UID.setText(AppData.cur_user.getUID());

        // OnClick Listener that redirects to homePage
        Register.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Grabs all user input into strings
                String userName = Name.getText().toString();
                String userEmail = Email.getText().toString();
                String phoneNumber = Phone.getText().toString();
                String userAge = Age.getText().toString();
                String activityLevel = ActivityLevel.getText().toString();
                String userUID = UID.getText().toString();

                // Adds users input into Current_user Class in AppData
                addUserClassValues(userName,phoneNumber,userAge,activityLevel);

                // Adds User to RealTime Database under Users Node
                addUserToDatabase(userName, userEmail, phoneNumber, userAge, activityLevel, userUID);
                // Directs User to HomePage
                switchPages();
            }
        });

    }

    private void addUserClassValues(String  userName, String phoneNumber, String userAge, String activityLevel)
    {
        AppData.cur_user.setName(userName);
        AppData.cur_user.setPhone(phoneNumber);
        AppData.cur_user.setAge(userAge);
        AppData.cur_user.setActivityLevel(activityLevel);
    }

    private void switchPages()
    {
        Intent launchActivity1= new Intent(
                CKD.Android.Demographics.this,Mood.class);
        startActivity(launchActivity1);
    }

    private void addUserToDatabase(String userName, String userEmail, String phone, String age, String activity, String UID)
    {

       FirebaseDatabase db = AppData.db;

        DatabaseReference User_node = db.getReference("Users");

        User_node.child(userName).child("Email").setValue(userEmail);
        User_node.child(userName).child("Phone Number").setValue(phone);
        User_node.child(userName).child("Age").setValue(age);
        User_node.child(userName).child("Activity Level").setValue(activity);
        User_node.child(userName).child("UID").setValue(UID);
    }


}






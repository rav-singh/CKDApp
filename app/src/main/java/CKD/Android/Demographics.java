package CKD.Android;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class Demographics extends AppCompatActivity
{
    private FirebaseUser mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demographics);

        mAuth = FirebaseAuth.getInstance().getCurrentUser();

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
        Email.setText(mAuth.getEmail());
        UID.setText(mAuth.getUid());

        // OnClick Listener that redirects to homePage
        Register.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String userName = Name.getText().toString();
                String userEmail = Email.getText().toString();
                String phoneNumber = Phone.getText().toString();
                String userAge = Age.getText().toString();
                String activityLevel = ActivityLevel.getText().toString();
                String userUID = UID.getText().toString();

                addUserToDatabase(userName, userEmail, phoneNumber, userAge, activityLevel, userUID);

                switchPages();
            }
        });

    }

    private void switchPages()
    {
        Intent launchActivity1= new Intent(
                CKD.Android.Demographics.this,HomePage.class);
        startActivity(launchActivity1);
    }

    private void addUserToDatabase(String userName, String userEmail, String phone, String age, String activity, String UID)
    {

        final FirebaseDatabase db = FirebaseDatabase.getInstance();

        DatabaseReference User_node = db.getReference("Users");

        User_node.child(userName).child("Email").setValue(userEmail);
        User_node.child(userName).child("Phone Number").setValue(phone);
        User_node.child(userName).child("Age").setValue(age);
        User_node.child(userName).child("Activity Level").setValue(activity);
        User_node.child(userName).child("UID").setValue(UID);
    }


}






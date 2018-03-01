package CKD.Android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Demographics_1 extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demographics_1);

        // UI Components
        final EditText Name = findViewById(R.id.Demo_TF_Name);
        final EditText Email =  findViewById(R.id.Demo_TF_Email);
        final EditText ConfirmEmail = findViewById(R.id.Demo_TF_Email_Confirm);
        final EditText Phone = findViewById(R.id.Demo_TF_Phone);
        final EditText ActivityLevel = findViewById(R.id.Demo_TF_ActivityL);
        final Button Register = findViewById(R.id.Demo_Btn_Next_1);


        //TODO Verify that the email matches the general convention of [a-zA-Z0-9]@[a-z].[a-z]
        //TODO Make user do Email Verification
        //TODO Push Activity Level onto a different page. It's placement here is awkward.

        // May be excessive and might simplify by removing name from Register Page
         Email.setText(AppData.cur_user.getEmail());

        // OnClick Listener that redirects to homePage
        Register.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Grabs all user input into strings
                String userName = Name.getText().toString();
                String userEmail = Email.getText().toString();
                String phoneNumber = Phone.getText().toString();
                String activityLevel = ActivityLevel.getText().toString();
                String confirmEmail = ConfirmEmail.getText().toString();

                // If user does not match the email typed on the previous page
                // they are prompted with an error message and the textView
                // becomes editable
                if(!emailsMatch(userEmail, confirmEmail))
                {
                    Toast.makeText(Demographics_1.this,
                            "Emails do not Match!",
                            Toast.LENGTH_LONG).show();
                    Email.setEnabled(true);
                    return;
                }

                if(!validEmail(userEmail))
                {
                    Toast.makeText(Demographics_1.this,
                            "Please enter a valid Email!",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if(!allFieldsEntered(userEmail,userName,phoneNumber,activityLevel))
                {
                    Toast.makeText(Demographics_1.this,
                            "Please fill in all data entry fields to continue!",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                // Adds users input into Current_user Class in AppData
                addUserClassValues(userName,userEmail, phoneNumber, activityLevel);

                // Directs User to HomePage
                switchPages();
            }
        });
    }

    private boolean allFieldsEntered(String userEmail, String userName, String phoneNumber, String activityLevel)
    {
        return !(userEmail.isEmpty() || userName.isEmpty() || phoneNumber.isEmpty() || activityLevel.isEmpty());
    }

    private boolean emailsMatch(String userEmail, String confirmEmail)
    {
        return userEmail.equals(confirmEmail);
    }

    private boolean validEmail(String userEmail)
    {
        return  userEmail.contains("@") && userEmail.contains(".com");
    }

    private void addUserClassValues(String  userName,String userEmail, String phoneNumber, String activityLevel)
    {
        AppData.cur_user.setName(userName);
        AppData.cur_user.setEmail(userEmail);
        AppData.cur_user.setPhone(phoneNumber);
        AppData.cur_user.setActivityLevel(activityLevel);
    }

    private void switchPages()
    {
        Intent launchActivity1= new Intent(CKD.Android.Demographics_1.this, Demographics_3.class);
        startActivity(launchActivity1);
    }

}






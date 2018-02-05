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

        //TODO Verify that the email and confirm email are the same
        //TODO Verify that the email matches the general convention of [a-zA-Z0-9]@[a-z].[a-z]
        //TODO Make user do Email Verfication
        //TODO Push Activity Level onto a different page. It's placement here is awkward.

        //  Fills in Name and Email Data for profile
        // May be excessive and might simplify by removing name from Register Page
         Email.setText(AppData.cur_user.getEmail());


       /*   //TODO Used for debugging
         Name.setText("Matt");
         ConfirmEmail.setText(AppData.cur_user.getEmail());
         Phone.setText("123");
         ActivityLevel.setText("123");
*/

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

                if(validEmail(userEmail, confirmEmail))
                {
                    // Adds users input into Current_user Class in AppData
                    addUserClassValues(userName,userEmail, phoneNumber, activityLevel);

                    // Adds User to RealTime Database under Users Node
                 //   addUserToDatabase(userName, userEmail, phoneNumber, activityLevel, AppData.cur_user.getUID());
                    // Directs User to HomePage
                    switchPages();
                }
                else
                {
                    // Because the user entered an invalid email they can now edit the email
                    // they typed in on the previous page
                    //TODO update the users email they registered with mAuth.currentuser.setEmail();
                    Email.setEnabled(true);

                }
            }
        });

    }

    private boolean validEmail(String userEmail, String confirmEmail)
    {
        return  userEmail.equals(confirmEmail);
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
        Intent launchActivity1= new Intent(CKD.Android.Demographics_1.this,Demographics_2.class);
        startActivity(launchActivity1);
    }

}






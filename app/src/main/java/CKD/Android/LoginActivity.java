package CKD.Android;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity
{
    FirebaseAuth Authenticator = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // UI Components
        final EditText Login_Email =  findViewById(R.id.Login_TF_EmailAddress);
        final EditText Login_Password = findViewById(R.id.Login_TF_Password);
        final Button Login = findViewById(R.id.Login_Btn_Login);

        // OnClick Listener that redirects to Register Page
        Login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String UserEmail = Login_Email.getText().toString();
                String UserPassword= Login_Password.getText().toString();

                // Makes sure user does not leave either field empty
                if(fieldsAreEmpty(UserEmail,UserPassword))
                {
                        Toast.makeText(LoginActivity.this,
                                "Please Fill in Both Text Fields",
                                Toast.LENGTH_LONG).show();
                        return;
                }

                else
                {
                    Authenticator.signInWithEmailAndPassword(UserEmail, UserPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            // User Successfully Registered to database
                            if (task.isSuccessful())
                            {
                                updateFirebaseInAppData();
                                loadUserClass();
                                switchPages();
                            }
                            // User unable to register_class user to database
                            // User is prompted with the appropriate error message
                            else
                            {
                                String error_Message = task.getException().toString();
                                showErrorMessage(error_Message);
                            }
                        }
                    });
                }
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
//        DatabaseReference add_node = users_node.child(userUID).child("Additional");

        AppData.readFromDatabaseAndWriteToAppData(cur_user_node,"Name");
        AppData.readFromDatabaseAndWriteToAppData(cur_user_node,"Email");
        AppData.readFromDatabaseAndWriteToAppData(cur_user_node,"Phone Number");

        //Commented out as we do not need to store this data on the phone Is only use
        // Only used to store in the database

        /*   AppData.readFromDatabaseAndWriteToAppData(add_node,"Activity Level");
        AppData.readFromDatabaseAndWriteToAppData(add_node,"Age");
        AppData.readFromDatabaseAndWriteToAppData(add_node,"Education");
        AppData.readFromDatabaseAndWriteToAppData(add_node,"Gender");
        AppData.readFromDatabaseAndWriteToAppData(add_node,"Health");
        AppData.readFromDatabaseAndWriteToAppData(add_node,"Marital Status");
        AppData.readFromDatabaseAndWriteToAppData(add_node,"Work");  */


    }



    private void updateFirebaseInAppData()
    {
        AppData.setmAuth(Authenticator);
        AppData.setFirebaseUser(Authenticator.getCurrentUser());
        AppData.setDb(FirebaseDatabase.getInstance());
    }

    private void showErrorMessage(String error_Message)
    {
        if(error_Message.contains("password"))
        {
            Toast.makeText(LoginActivity.this,
                    "Incorrect Password!", Toast.LENGTH_LONG).show();
        }
        else if(error_Message.contains("email"))
        {
            Toast.makeText(LoginActivity.this,
                    "Incorrect Email!", Toast.LENGTH_LONG).show();
        }
        else if(error_Message.contains("no user record"))
        {
            Toast.makeText(LoginActivity.this,
                    "Sorry there is no account registered to that email", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(LoginActivity.this,error_Message, Toast.LENGTH_LONG).show();
        }
    }

    private void switchPages()
    {
        Intent launchActivity1= new Intent(
                CKD.Android.LoginActivity.this,HomePage.class);
        startActivity(launchActivity1);

    }

    private boolean fieldsAreEmpty(String userEmail, String userPassword)
    {
        return userEmail.isEmpty() || userPassword.isEmpty();
    }

}

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

                if(fieldsAreEmpty(UserEmail,UserPassword))
                    {
                        Toast.makeText(LoginActivity.this,
                                "Please Fill in Both Text Fields",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                else
                {
                    Task<AuthResult> Auth =
                    Authenticator.signInWithEmailAndPassword(UserEmail, UserPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            // User Successfully Registered to database
                            if (task.isSuccessful())
                            {
                                //TODO Check to make sure AppData.mAuth is correctly updated
                                AuthResult result = task.getResult();

                                updateFirebaseInAppData();

                                loadUserClass();

                                Log.d("CURRENT USER", AppData.cur_user.toString());

                                switchPages();

                            }
                            // User unable to register_class user to database
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

        Log.d("CURRENT USER", AppData.cur_user.toString());

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


    /*    Log.d("Name",AppData.cur_user.getName());
        Log.d("EMAIL",AppData.cur_user.getEmail());
        Log.d("ACTIVTY",AppData.cur_user.getActivityLevel());
        Log.d("AGe",AppData.cur_user.getAge());
        Log.d("EDUCAT",AppData.cur_user.getEducation());
        Log.d("GENDER",AppData.cur_user.getGender());
        Log.d("HEALTH",AppData.cur_user.getHealthRating());
        Log.d("MARITAL",AppData.cur_user.getWorkStatus());
        Log.d("WORK",AppData.cur_user.getWorkStatus());
*/

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

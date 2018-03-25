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
import com.google.firebase.auth.ProviderQueryResult;


public class registerNewUser extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_user);

        // UI Components
        final EditText Email =  findViewById(R.id.Register_TF_EmailAddress);
        final EditText Password = findViewById(R.id.Register_TF_Password);
        final Button Register = findViewById(R.id.Register_Btn_Register);

        // OnClick Listener that redirects to Register Page
        Register.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                final String newEmail = Email.getText().toString();
                final String newPassword = Password.getText().toString();

                if(eitherFieldIsEmpty(newEmail, newPassword))
                {
                    Toast.makeText(registerNewUser.this,
                                    "Please Fill in Both Text Fields",
                                    Toast.LENGTH_LONG).show();
                    return;
                }
                // Makes sure users entered email address is valid
                // Contains "@" and ".com"
                if(!validEmail(newEmail))
                {
                    Toast.makeText(registerNewUser.this,
                            "Please use a valid email Address!",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                // Password must be atleast 6 characters long
                if(!validPassword(newPassword))
                {
                    Toast.makeText(registerNewUser.this,
                            "Password must be atleast 6 characters long!",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                // Creates UserClass instance in AppData with only the Email instantiated
                AppData.setCur_User((new UserClass( null, newEmail, null,null,
                        null,null)));
                // holds the users password
                AppData.cur_user.setPassword(newPassword);

                Intent launchActivity1= new Intent(CKD.Android.registerNewUser.this,Demographics_1.class);
                startActivity(launchActivity1);
            }
        });
    }

    private boolean validPassword(String newPassword)
    {
        return newPassword.length() >= 6;
    }

    private boolean validEmail(String newEmail)
    {
        return newEmail.contains("@") &&
               newEmail.contains(".com");
    }

    private boolean eitherFieldIsEmpty(String newEmail, String newPassword)
    {
        return newEmail.isEmpty() || newPassword.isEmpty();
    }
}




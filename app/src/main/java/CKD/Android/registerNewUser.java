package CKD.Android;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class registerNewUser extends AppCompatActivity {

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

                //TODO create second Password for verification and check ==
                //TODO create second if statement for appropriate Toast
                if(fieldsAreEmpty(newEmail, newPassword) && validEmail(newEmail))
                {
                    Toast.makeText(registerNewUser.this,
                                    "Please Fill in Both Text Fields",
                                    Toast.LENGTH_LONG).show();
                }

                else
                {
                    // Creates UserClass instance in AppData with only the Email instantiated
                    AppData.cur_user = new UserClass(null, newEmail, null,
                            null, null, null);

                    // holds the users password
                    AppData.cur_user.setPassword(newPassword);

                    Intent launchActivity1= new Intent(CKD.Android.registerNewUser.this,Demographics_1.class);
                    startActivity(launchActivity1);
                }
             }
        });
    }

    private boolean validEmail(String newEmail)
    {
        return newEmail.contains("@") &&
               newEmail.contains(".com");
    }

    private boolean fieldsAreEmpty(String newEmail, String newPassword)
    {
        return newEmail.isEmpty() || newPassword.isEmpty();
    }
}




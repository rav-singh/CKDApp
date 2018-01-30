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


public class LoginActivity extends AppCompatActivity
{
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

                Task<AuthResult> Auth =
                        AppData.getInstance().mAuth.signInWithEmailAndPassword(UserEmail, UserPassword)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task)
                                    {
                                        // User Successfully Registered to database
                                        if (task.isSuccessful())
                                        {
                                            AuthResult result = task.getResult();
                                            Intent launchActivity1= new Intent(
                                                    CKD.Android.LoginActivity.this,HomePage.class);
                                            startActivity(launchActivity1);

                                        }
                                        // User unable to register_class user to database
                                        else
                                        {
                                            String error_Message = task.getException().toString();
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
                                    }
                                });
            }
        });
    }
    }

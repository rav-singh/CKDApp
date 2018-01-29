package CKD.Android;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class registerNewUser extends AppCompatActivity {

    public EditText name;
    private String mDisplayName;
    private Button Register;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_user);

        // UI Components
        final EditText Email =  findViewById(R.id.TF_EmailAddress);
        final EditText Password = findViewById(R.id.TF_password);
        Button Register = findViewById(R.id.btn_register);

        // OnClick Listener that redirects to Register Page
        Register.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String newEmail = Email.getText().toString();
                String newPassword= Password.getText().toString();

                mAuth = FirebaseAuth.getInstance();

                Task<AuthResult> password =
                                 mAuth.createUserWithEmailAndPassword(newEmail, newPassword)
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
                                    CKD.Android.registerNewUser.this,MainActivity.class);
                            startActivity(launchActivity1);

                        }
                        // User unable to register_class user to database
                        else
                        {
                            @SuppressWarnings("ThrowableNotThrown")
                            Exception exception = task.getException();
                        }

                    }
                });
            }
        });
    }
}

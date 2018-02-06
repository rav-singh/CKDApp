package CKD.Android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.net.Authenticator;
import java.util.ArrayList;
import java.util.List;

public class Demographics_3 extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demographics_3);

        final TextView educationPrompt = findViewById(R.id.Demo3_TV_EducationPrompt);
        final Spinner userEducation = findViewById(R.id.Demo3_SPN_Education);
        final TextView workPrompt = findViewById(R.id.Demo3_TV_WorkPrompt);
        final Spinner userWork = findViewById(R.id.Demo3_SPN_Work);
        final CheckBox health1 = findViewById(R.id.Demo3_CB_Health_1);
        final CheckBox health2 = findViewById(R.id.Demo3_CB_Health_2);
        final CheckBox health3 = findViewById(R.id.Demo3_CB_Health_3);
        final CheckBox health4 = findViewById(R.id.Demo3_CB_Health_4);
        final CheckBox health5 = findViewById(R.id.Demo3_CB_Health_5);
        final Button next = findViewById(R.id.Demo3_Btn_next);

        next.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String selectedEducation = userEducation.getSelectedItem().toString();
                String selectedWork = userWork.getSelectedItem().toString();
                String selectedHealth;

                if(health1.isChecked())
                    selectedHealth = health1.getText().toString();
                else if(health2.isChecked())
                    selectedHealth = health1.getText().toString();
                else if(health3.isChecked())
                    selectedHealth = health1.getText().toString();
                else if(health4.isChecked())
                    selectedHealth = health1.getText().toString();
                else if(health5.isChecked())
                    selectedHealth = health1.getText().toString();
                // If no health was selected onClick method Stops and user is given
                // an appropriate message
                else
                {
                    errorNoHealthSelected();
                    return;
                }

                switchPages(Demographics_3.this,Demographics_2.class);

            }
        });
    }

    private void switchPages(Demographics_3 currentPage, Class nextPage)
    {
        Intent launchActivity1= new Intent(currentPage,nextPage);
        startActivity(launchActivity1);
    }

    private void errorNoHealthSelected()
    {
        Toast.makeText(Demographics_3.this,
                "Please Select A Present Health",
                Toast.LENGTH_LONG).show();
    }
}




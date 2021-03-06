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
    List<CheckBox> allHealthCB = new ArrayList<>();
    String selectedHealth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demographics_3);

        final Spinner userEducation = findViewById(R.id.Demo3_SPN_Education);
        final Spinner userWork = findViewById(R.id.Demo3_SPN_Work);

        initiliazeAndStoreCheckBoxes();

        for(CheckBox cb : allHealthCB)
        {
            setOnClickListeners(cb);
        }

        final Button next = findViewById(R.id.Demo3_Btn_next);
        next.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String selectedEducation = userEducation.getSelectedItem().toString();
                String selectedWork = userWork.getSelectedItem().toString();

                checkSelectedHealth();

                //TODO Ask Valenti if they should have option
                if(selectedEducation.contains("option"))
                {
                    Toast.makeText(Demographics_3.this,
                            "Please select your highest level of education",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if(selectedWork.contains("option"))
                {
                    Toast.makeText(Demographics_3.this,
                            "Please select your current work status",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                addValuesToUserClass(selectedEducation,selectedWork,selectedHealth);

                switchPages(Demographics_3.this,Demographics_4.class);

            }
        });
    }

    private void setOnClickListeners(final CheckBox cb)
    {
        cb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //cb.setChecked(!cb.isChecked());
                uncheckOtherBoxes(allHealthCB.indexOf(cb));
            }
        });

    }

    private void uncheckOtherBoxes(int selectedCheckBoxIndex)
    {
        for(CheckBox cb : allHealthCB)
        {
            if(!(allHealthCB.indexOf(cb) == selectedCheckBoxIndex))
            {
                cb.setChecked(false);
            }
        }
    }

    private void checkSelectedHealth()
    {
        for(CheckBox cb : allHealthCB)
        {
            if (cb.isChecked())
            {
                selectedHealth = cb.getText().toString();
                return;
            }
                // If no health was selected onClick method Stops and user is given
                // an appropriate message
        }

        errorNoHealthSelected();
        return;

    }

    private void initiliazeAndStoreCheckBoxes()
    {
        CheckBox _1 = findViewById(R.id.Demo3_CB_Health_1);
        allHealthCB.add(_1);
        final CheckBox _2 = findViewById(R.id.Demo3_CB_Health_2);
        allHealthCB.add(_2);
        final CheckBox _3 = findViewById(R.id.Demo3_CB_Health_3);
        allHealthCB.add(_3);
        final CheckBox _4 = findViewById(R.id.Demo3_CB_Health_4);
        allHealthCB.add(_4);
        final CheckBox _5 = findViewById(R.id.Demo3_CB_Health_5);
        allHealthCB.add(_5);
    }

    private void addValuesToUserClass(String selectedEducation, String selectedWork, String selectedHealth)
    {
        AppData.cur_user.setEducation(selectedEducation);
        AppData.cur_user.setWork(selectedWork);
        AppData.cur_user.setHealth(selectedHealth);
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




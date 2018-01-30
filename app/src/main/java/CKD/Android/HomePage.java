package CKD.Android;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;

public class HomePage  extends AppCompatActivity {

    public EditText name;
    private String mDisplayName;
    private Button Register;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // UI Component
        Button diet_Button = findViewById(R.id.Home_Btn_Diet);
        Button mood_Button = findViewById(R.id.Home_Btn_Mood);
        Button exercise_Button = findViewById(R.id.Home_Btn_Exerxcise);
        Button social_Button = findViewById(R.id.Home_Btn_Social);
        Button leaderboards_Button = findViewById(R.id.Home_Btn_Leaderboards);


        diet_Button.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent launchActivity1=
                        new Intent(CKD.Android.HomePage.this,MainActivity.class);
                startActivity(launchActivity1);
            }
        });

        mood_Button.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent launchActivity1=
                        new Intent(CKD.Android.HomePage.this,MainActivity.class);
                startActivity(launchActivity1);
            }
        });

        exercise_Button.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent launchActivity1=
                        new Intent(CKD.Android.HomePage.this,MainActivity.class);
                startActivity(launchActivity1);
            }
        });

        social_Button.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent launchActivity1=
                        new Intent(CKD.Android.HomePage.this,MainActivity.class);
                startActivity(launchActivity1);
            }
        });

        leaderboards_Button.setOnClickListener(new View.OnClickListener()
         {

            public void onClick(View v)
             {
                 Intent launchActivity1=
                         new Intent(CKD.Android.HomePage.this,MainActivity.class);
                 startActivity(launchActivity1);
             }
    });

    }

}
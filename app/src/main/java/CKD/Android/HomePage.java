package CKD.Android;


import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;


public class HomePage  extends AppCompatActivity {

    private Button diet_Button,
                   exercise_Button,
                   mood_Button,
                   social_Button,
                   leaderboards_Button,
                   logout_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        InitializeComponents();

        //TODO Once user Logs in Load AppData with their information from the database
        /*
           Log.d("HEYO",AppData.cur_user.getUID());
             Log.i("Tag",AppData.cur_user.getGender());
             Log.i("Tag",AppData.cur_user.getEmail());
             Log.i("Tag",AppData.cur_user.getName());
             Log.i("Tag",AppData.cur_user.getPhone());
             Log.i("Tag",AppData.cur_user.getActivityLevel());
         */

        diet_Button.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent launchActivity1=
                        new Intent(CKD.Android.HomePage.this,Diet.class);
                startActivity(launchActivity1);
            }
        });

        mood_Button.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent launchActivity1=
                        new Intent(CKD.Android.HomePage.this,Mood.class);
                startActivity(launchActivity1);
            }
        });

        exercise_Button.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent launchActivity1=
                        new Intent(CKD.Android.HomePage.this,Exercise.class);
                startActivity(launchActivity1);
            }
        });

        social_Button.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent launchActivity1=
                        new Intent(CKD.Android.HomePage.this,Social.class);
                startActivity(launchActivity1);
            }
        });

        leaderboards_Button.setOnClickListener(new View.OnClickListener()
         {

            public void onClick(View v)
             {
                 Intent launchActivity1=
                         new Intent(CKD.Android.HomePage.this,Leaderboards.class);
                 startActivity(launchActivity1);
             }
         });

        logout_Button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                FirebaseAuth.getInstance().signOut();

                AppData.signOut();

                Intent launchActivity1=
                        new Intent(CKD.Android.HomePage.this,MainActivity.class);
                startActivity(launchActivity1);
            }
        });

    }

    private void InitializeComponents() {
        // UI Component
        diet_Button = findViewById(R.id.Home_Btn_Diet);
        mood_Button = findViewById(R.id.Home_Btn_Mood);
        exercise_Button = findViewById(R.id.Home_Btn_Exerxcise);
        social_Button = findViewById(R.id.Home_Btn_Social);
        leaderboards_Button = findViewById(R.id.Home_Btn_Leaderboards);
        logout_Button = findViewById(R.id.HomePage_Btn_Logout);
    }
}
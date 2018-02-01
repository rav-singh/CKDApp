package CKD.Android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Mood extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);

        // UI Components
        final Button happy_btn = findViewById(R.id.Mood_Btn_Happy);
        final Button sad_btn = findViewById(R.id.Mood_Btn_Sad);
        final Button drunk_btn = findViewById(R.id.Mood_Btn_Drunk);
        final Button sleepy_btn = findViewById(R.id.Mood_Btn_Sleepy);


        // TODO Implement some way to record user submission and send to database for each button
        happy_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent launchActivity1= new Intent(
                        CKD.Android.Mood.this,HomePage.class);
                startActivity(launchActivity1);
            }
        });

        sad_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent launchActivity1= new Intent(
                        CKD.Android.Mood.this,HomePage.class);
                startActivity(launchActivity1);
            }
        });

        drunk_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent launchActivity1= new Intent(
                        CKD.Android.Mood.this,HomePage.class);
                startActivity(launchActivity1);
            }
        });


        sleepy_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent launchActivity1= new Intent(
                        CKD.Android.Mood.this,HomePage.class);
                startActivity(launchActivity1);
            }
        });

    }
}


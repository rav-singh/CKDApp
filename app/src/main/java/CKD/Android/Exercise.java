package CKD.Android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Exercise extends AppCompatActivity
{
    Boolean checklistUpdated = false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);


        //TODO Move this to a location that signifies the user did something on this page
        if(!checklistUpdated)
        {
            AppData.updateDailyChecklist("Exercise");
            checklistUpdated = true;
        }

        Button home_btn = findViewById(R.id.Exervice_btn_home);
        home_btn = AppData.activateHomeButton(home_btn,Exercise.this);
    }
}


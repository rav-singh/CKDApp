package CKD.Android;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;




public class HomePage  extends AppCompatActivity {

    private Button diet_Button,
                   exercise_Button,
                   mood_Button,
                   social_Button,
                   summary_Button,
                   logout_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        InitializeComponents();
        setOnClickListeners();

        checkDailyCheckList();
    }

    private void checkDailyCheckList()
    {
        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String date = AppData.getTodaysDate();

        FirebaseDatabase db = FirebaseDatabase.getInstance();

        DatabaseReference Date_node = db.getReference()
                                        .child("Data")
                                        .child("DailyCheckList")
                                        .child(UID)
                                        .child(date);


        Date_node.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                long numComponentsCompleted = dataSnapshot.getChildrenCount();

               if(numComponentsCompleted > 3 && !AppData.checkListPopUpPresented)
               {
                   AppData.checkListPopUpPresented = true;
                   AlertDialog alertDialog = new AlertDialog.Builder(HomePage.this).create();
                   alertDialog.setTitle("Daily Checklist Completed!");
                   alertDialog.setMessage("Great job! You completed all your daily components!");
                  // alertDialog.setIcon(R.drawable.welcome);
                   alertDialog.show();
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    private void setOnClickListeners()
    {
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

        summary_Button.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent launchActivity1=
                        new Intent(CKD.Android.HomePage.this,Summary.class);
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

    private void InitializeComponents()
    {
        // UI Component
        diet_Button = findViewById(R.id.Home_Btn_Diet);
        mood_Button = findViewById(R.id.Home_Btn_Mood);
        exercise_Button = findViewById(R.id.Home_Btn_Exercise);
        social_Button = findViewById(R.id.Home_Btn_Social);
        summary_Button = findViewById(R.id.Home_Btn_Summary);
        logout_Button = findViewById(R.id.HomePage_Btn_Logout);
    }
}
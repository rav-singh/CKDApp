package CKD.Android;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.provider.ContactsContract;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static CKD.Android.AppData.getTodaysDate;


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

        try
        {
            Boolean treatment = isUserCurrentlyOnDialysis();
            Boolean treatmentRecorded = isUserTreatmentRecorded(AppData.getTodaysDate());

            // First checks if the user has already been recorded as treated
            // If they have not recorded yet, it then checks if the current time
            // is within their scheduled times for dialysis
            if(!treatmentRecorded && treatment)
            {

                String date = getTodaysDate();
                String UID = AppData.cur_user.getUID();

                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference Treatment_Node = db.getReference("Data")
                                                .child("Participation")
                                                .child(UID)
                                                .child(date)
                                                .child("Treatment");
                Treatment_Node.setValue(1);

            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    private Boolean isUserCurrentlyOnDialysis() throws ParseException
    {
        String currentDay = AppData.getTodaysDay();

        if(!AppData.cur_user.getScheduledDays().contains(currentDay))
        {
            return false;
        }

        String currentTime = AppData.getTime();
        String startTime = AppData.cur_user.getScheduledStartTime().get(currentDay);
        String endTime = AppData.cur_user.getScheduledEndTime().get(currentDay);

        SimpleDateFormat militaryTime = new SimpleDateFormat("HH:mm");

        Date start = militaryTime.parse(startTime);
        Date end = militaryTime.parse(endTime);
        Date current = militaryTime.parse(currentTime);

        return (current.after(start) && current.before(end));
    }

    private Boolean isUserTreatmentRecorded(String todaysDate)
    {
        final boolean[] TreatmentRecorded = {false};

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference Treatment_Node = db.child("Data")
                                                .child("Participation")
                                                .child(AppData.cur_user.getUID())
                                                .child("Date")
                                                .child("Treatment");

        Treatment_Node.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                Integer count = dataSnapshot.getValue(Integer.class);

                if(count == null)
                {
                   return;
                }
                else
                {
                    TreatmentRecorded[0] = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
        return TreatmentRecorded[0];
    }

    private void checkDailyCheckList()
    {
        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String date = getTodaysDate();

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

                // Only case where the user views the pop up is the first time they
                // they come to the home page after participating all components
                // Once Alert is activated it adds another node in the database such
                // that the user will no longer see this Alert for the day.
               if(numComponentsCompleted == 4)
               {
                   AppData.updateDailyChecklist("Awarded");

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
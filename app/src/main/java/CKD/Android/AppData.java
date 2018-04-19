package CKD.Android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;


public final class  AppData
{
    private static AppData instance = null;
    public static UserClass cur_user;
    public static FirebaseDatabase db;
    static FirebaseAuth mAuth;
    static FirebaseUser firebaseUser;
    static String cur_Category;
    static String cur_Thread_Key;
    static ThreadClass cur_Thread;
    static Boolean isUserRegistering = false;
    static Boolean PopUpPresented = false;
    static Boolean userMakingComment = false;

    public static AppData getInstance()
    {
        if (instance == null)
          return new AppData();

        return instance;
    }


    static void signOut()
    {
        instance = null;
        cur_user = null;
        db = null;
        mAuth = null;
        firebaseUser = null;
    }

    static void setCur_User(UserClass cur_User)
    {
        AppData.cur_user = cur_User;
    }

    static void setFirebaseUser(FirebaseUser firebaseUser)
    {
        AppData.firebaseUser = firebaseUser;
    }

    static void setmAuth(FirebaseAuth mAuth)
    {
        AppData.mAuth = mAuth;
    }

    static void setDb(FirebaseDatabase db)
    {
        AppData.db = db;
    }

    static FirebaseAuth getmAuth(){return AppData.mAuth;}

    static void setCurrentThread(ThreadClass cur_Thread){AppData.cur_Thread = cur_Thread;}

    static void updateDailyChecklist(String component)
    {
        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String date = getTodaysDate();

        FirebaseDatabase db = FirebaseDatabase.getInstance();

        DatabaseReference Date_Node = db.getReference("Data")
                                        .child("DailyCheckList")
                                        .child(UID)
                                        .child(date);

        Date_Node.child(component).setValue("1");
    }

    static String getTodaysDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));

        // Create a calendar object with today date.
        Calendar calendar = Calendar.getInstance();

        // Get current date of calendar which point to the yesterday now
        Date today = calendar.getTime();

        return dateFormat.format(today);
    }


    static String getTodaysDay()
    {
        DateFormat dateFormat = new SimpleDateFormat("EEEE");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));

        // Create a calendar object with today date.
        Calendar calendar = Calendar.getInstance();

        // Get current date of calendar which point to the yesterday now
        Date today = calendar.getTime();

        return dateFormat.format(today);
    }

    static String getTime()
    {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));

        // Create a calendar object with today date.
        Calendar calendar = Calendar.getInstance();

        // Get current date of calendar which point to the yesterday now
        Date today = calendar.getTime();

        return dateFormat.format(today);
    }

    static String getYesterdaysDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));

        // Create a calendar object with today date.
        Calendar calendar = Calendar.getInstance();

        // Move calendar to yesterday
        calendar.add(Calendar.DATE, -1);

        // Get current date of calendar which point to the yesterday now
        Date yesterday = calendar.getTime();

        return dateFormat.format(yesterday);
    }

    static void updateParticipation(final String node_Name)
    {
        String date = getTodaysDate();
        String UID = cur_user.getUID();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference Date_Node = db.getReference("Data")
                .child("Participation")
                .child(UID)
                .child(date);

        Date_Node.child(node_Name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                Integer count = dataSnapshot.getValue(Integer.class);

                if(count == null)
                {
                    Date_Node.child(node_Name).setValue(1);
                }
                else
                {
                    Date_Node.child(node_Name).setValue(++count);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    static Button activateHomeButton(Button b, final Context page)
    {
        b.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent launchActivity1=
                        new Intent(page, HomePage.class);
                page.startActivity(launchActivity1);
            }
        });
        return b;
    }

    static void readFromDatabaseAndWriteToAppData(DatabaseReference node, final String key)
    {
        node.child(key).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                // Returns the value under the node passed in at the key passed in
                String value = dataSnapshot.getValue(String.class);

                if(Objects.equals(key, "Name"))
                    AppData.cur_user.setName(value);

                else if(Objects.equals(key, "Email"))
                    AppData.cur_user.setEmail(value);

                else if (Objects.equals(key, "Phone Number"))
                    AppData.cur_user.setPhone(value);
/*
                else if (Objects.equals(key, "Age"))
                    AppData.cur_user.setAge(value);

                else if (Objects.equals(key, "Activity Level"))
                    AppData.cur_user.setActivityLevel(value);

                else if (Objects.equals(key, "Marital Status"))
                    AppData.cur_user.setMarital(value);

                else if (Objects.equals(key, "Gender"))
                    AppData.cur_user.setGender(value);

                else if (Objects.equals(key, "Education"))
                    AppData.cur_user.setEducation(value);

                else if (Objects.equals(key, "Work"))
                    AppData.cur_user.setWork(value);

                else if (Objects.equals(key, "Health"))
                    AppData.cur_user.setHealth(value);
*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                //TODO What happens when theres an Error
                Log.i("DATABASE READ ERROR",
                        databaseError.getMessage().toString());
                Log.i("DATABASE READ ERROR",
                        databaseError.getMessage().toString());
                Log.i("DATABASE READ ERROR",
                        databaseError.getMessage().toString());
            }});
    }

    static void WriteTimesToAppData(DatabaseReference schedule_node)
    {
        for(final String Day : AppData.cur_user.getScheduledDays())
        {
            schedule_node.child(Day).child("StartTime").addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    String startTime = dataSnapshot.getValue(String.class);
                    AppData.cur_user.getScheduledStartTime().put(Day, startTime);

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            schedule_node.child(Day).child("EndTime").addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    String EndTime = dataSnapshot.getValue(String.class);
                    AppData.cur_user.getScheduledEndTime().put(Day, EndTime);
                }
                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            });
        }
    }


    static void WriteDaysToAppData(final DatabaseReference schedule_node)
    {
        schedule_node.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot d : dataSnapshot.getChildren())
                {
                    // Adds all the days the user is scheduled for to the list inside
                    // AppData.cur_user class
                    String Day = d.getKey();
                    AppData.cur_user.getScheduledDays().add(Day);
                }

                WriteTimesToAppData(schedule_node);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    static void disableBtn(Button b)
    {
        b.setEnabled(false);
        b.setClickable(false);
        b.setBackgroundColor(Color.TRANSPARENT);
        b.setText("");
    }

    static void enableBtn(Button b)
    {
        b.setEnabled(true);
        b.setClickable(true);
        b.setBackgroundColor(Color.WHITE);
        b.setText("Nutrition Blog");
    }
}

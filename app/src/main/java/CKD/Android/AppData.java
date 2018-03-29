package CKD.Android;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.PublicKey;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public final class  AppData
{
    private static AppData instance = null;
    public static UserClass cur_user;
    public static FirebaseDatabase db;
    static FirebaseAuth mAuth;
    public static FirebaseUser firebaseUser;
    public static String cur_Category;
    public static String cur_Thread_Key;
    public static ThreadClass cur_Thread;
    public static Boolean isUserRegistering = false;
    static Boolean checkListPopUpPresented = false;

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
        //Grabs references to the Root node and Users Node
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        DatabaseReference Data_node = db.getReference("Data");
        DatabaseReference Mood_node = Data_node.child("DailyCheckList");
        DatabaseReference UID_node = Mood_node.child(UID);

        UID_node.child(date).child(component).setValue("1");
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
}

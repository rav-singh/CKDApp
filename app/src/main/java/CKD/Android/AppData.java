package CKD.Android;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.security.PublicKey;


public final class  AppData
{
    private static AppData instance = null;
    public static UserClass cur_user;
    public static FirebaseDatabase db;
    public static FirebaseAuth mAuth;
    public static FirebaseUser firebaseUser;
    public static String cur_Category;
    public static String cur_Thread_Key;
    public static ThreadClass cur_Thread;
    public static Boolean isUserRegistering = false;

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

}

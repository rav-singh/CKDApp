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
    public static int numThreads;
    public static String threadCount;

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

    static int getNumThreads(){return AppData.numThreads;}

    static void setNumThreads(int num){AppData.numThreads = num;}

    static String getThreadCount(){return AppData.threadCount;}

    static void setThreadCount(String count){AppData.threadCount = count;}
}

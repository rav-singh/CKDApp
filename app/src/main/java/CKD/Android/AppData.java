package CKD.Android;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


class AppData
{
    private static AppData instance;

    public static UserClass cur_user;
    public static FirebaseDatabase db;
    private static FirebaseAuth mAuth;
    public static FirebaseUser firebaseUser;

    // If an instance of AppData hasn't been created yet create it
    // return instance
    public static AppData getInstance()
    {
        if(instance == null)
            instance = new AppData();

        return instance;
    }

    // Constructor contains all of the firebase References needed
    // If needed just refer to AppData.getInstance().Variable
    private AppData()
    {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        firebaseUser = mAuth.getCurrentUser();
    }

    // completely clears AppData class to sign user out.
    // Maybe have some data structure holding all users?
    // Possibly a HashMap with UID as the key and a Dictionary
    // containing UserClass variables
    public static void signOut()
    {
        mAuth.signOut();
    }

    public FirebaseAuth getmAuth(){return this.mAuth;}
    public void setmAuth(FirebaseAuth mAuth){this.mAuth = mAuth;}
}

package CKD.Android;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Matt on 1/30/2018.
 */

public class AppData
{
    private static AppData instance;

    public static UserClass cur_user;
    FirebaseDatabase db;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;

    public static AppData getInstance()
    {
        if(instance == null)
            instance = new AppData();

        return instance;
    }

    private AppData()
    {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance();

    }


}

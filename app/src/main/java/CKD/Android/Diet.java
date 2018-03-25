package CKD.Android;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Diet extends AppCompatActivity {

    // Request Codes for different meals
    public static final int REQUEST_CODE_Breakfast = 1111;
    public static final int REQUEST_CODE_Lunch = 2222;
    public static final int REQUEST_CODE_Dinner = 3333;
    public static final int REQUEST_CODE_Snacks = 4444;

    private Button btnAddBreakfast;
    private Button btnAddLunch;
    private Button btnAddDinner;
    private Button btnAddSnacks;
    private String thisFoodName;
    private int thisFoodNbdNo;
    private ListView lvBreakfast;
    private DatabaseReference mDatabse;

    // Arraylists for foodNames and Ndbnos for each meal
    ArrayList<String> breakfastNames = new ArrayList<String>();
    ArrayList<Integer> breakfastNdbnos = new ArrayList<Integer>();
    ArrayList<String> lunchNames = new ArrayList<String>();
    ArrayList<Integer> lunchNdbnos = new ArrayList<Integer>();
    ArrayList<String> dinnerNames = new ArrayList<String>();
    ArrayList<Integer> dinnerNdbnos = new ArrayList<Integer>();
    ArrayList<String> snackNames = new ArrayList<String>();
    ArrayList<Integer> snackNdbnos = new ArrayList<Integer>();

    String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseDatabase db = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        mDatabse = FirebaseDatabase.getInstance().getReference();

        btnAddBreakfast = (Button) findViewById(R.id.addBreakfast_btn);
        btnAddLunch = (Button) findViewById(R.id.addLunch_btn);
        btnAddDinner = (Button) findViewById(R.id.addDinner_btn);
        btnAddSnacks = (Button) findViewById(R.id.addSnacks_btn);


        btnAddBreakfast.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                // Indicate Breakfast Button was clicked
                Intent intent = SearchFood.makeIntent(Diet.this);
                //startActivity(myIntent);
                startActivityForResult(intent, REQUEST_CODE_Breakfast);
            }
        });

        btnAddLunch.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                // Indicate Lunch Button was clicked
                Intent intent = SearchFood.makeIntent(Diet.this);
                //startActivity(myIntent);
                startActivityForResult(intent, REQUEST_CODE_Lunch);
            }
        });

        btnAddDinner.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                // Indicate Breakfast Button was clicked
                Intent intent = SearchFood.makeIntent(Diet.this);
                //startActivity(myIntent);
                startActivityForResult(intent, REQUEST_CODE_Dinner);
            }
        });

        btnAddSnacks.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                // Indicate Breakfast Button was clicked
                Intent intent = SearchFood.makeIntent(Diet.this);
                //startActivity(myIntent);
                startActivityForResult(intent, REQUEST_CODE_Snacks);
            }
        });

    }

    // Listener for result back from SearchFood
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String intrequestcode = Integer.toString(requestCode);
        Log.i("request code was: ", intrequestcode);

        if(resultCode == Activity.RESULT_OK)
        {
            thisFoodName = data.getStringExtra("FoodName");
            thisFoodNbdNo = data.getIntExtra("NdbNo", 0);
            String intfoodNdbNo = Integer.toString(thisFoodNbdNo);
            Log.i("Logging foodName: " ,thisFoodName);
            Log.i("Logging NdbNo: " ,intfoodNdbNo);

            String date = new SimpleDateFormat("YYYY-MM-dd", Locale.getDefault()).format(new Date());

            // Creates a Diet Child Node under Data
            // Creates a UID Child Node under Diet
            DatabaseReference Diet_node = db.getReference().child("Data").child("Diet");
            DatabaseReference UID_node = Diet_node.child(UID);

            switch(requestCode)
            {
                case 1111: // Breakfast
                    breakfastNames.add(thisFoodName);
                    breakfastNdbnos.add(thisFoodNbdNo);
                    String bnames = breakfastNames.toString();
                    String bndbs = breakfastNdbnos.toString();;
                    UID_node.child(date).child("Breakfast").child("Food Names").setValue(bnames);
                    UID_node.child(date).child("Breakfast").child("Food NDBs").setValue(bndbs);
                    return;

                case 2222: // Lunch
                    lunchNames.add(thisFoodName);
                    lunchNdbnos.add(thisFoodNbdNo);
                    String lnames = lunchNames.toString();
                    String lndbs = lunchNdbnos.toString();
                    UID_node.child(date).child("Lunch").child("Food Names").setValue(lnames);
                    UID_node.child(date).child("Lunch").child("Food NDBs").setValue(lndbs);
                    return;

                case 3333: // Dinner
                    dinnerNames.add(thisFoodName);
                    dinnerNdbnos.add(thisFoodNbdNo);
                    String dnames = dinnerNames.toString();
                    String dndbs = dinnerNdbnos.toString();
                    UID_node.child(date).child("Dinner").child("Food Names").setValue(dnames);
                    UID_node.child(date).child("Dinner").child("Food NDBs").setValue(dndbs);
                    return;

                case 4444: // Snacks
                    snackNames.add(thisFoodName);
                    snackNdbnos.add(thisFoodNbdNo);
                    String snames = snackNames.toString();
                    String sndbs = snackNdbnos.toString();
                    UID_node.child(date).child("Snack").child("Food Names").setValue(snames);
                    UID_node.child(date).child("Snack").child("Food NDBs").setValue(sndbs);
                    return;

                default:
                    breakfastNames.add(thisFoodName);
                    breakfastNdbnos.add(thisFoodNbdNo);
                    String bdnames = breakfastNames.toString();
                    String bdndbs = breakfastNdbnos.toString();
                    UID_node.child(date).child("Breakfast").child("Food Names").setValue(bdnames);
                    UID_node.child(date).child("Breakfast").child("Food NDBs").setValue(bdndbs);
                    return;
            }


        }

        else
        {
            // Handle bad result from SearchFood.java
        }
    }

}


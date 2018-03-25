package CKD.Android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
    private DatabaseReference mDatabse;

    // Arraylists for foodNames and Ndbnos for each meal
    ArrayList<String> breakfastNames = new ArrayList<String>();
    ArrayList<String> breakfastNdbnos = new ArrayList<String>();
    ArrayList<String> lunchNames = new ArrayList<String>();
    ArrayList<String> lunchNdbnos = new ArrayList<>();
    ArrayList<String> dinnerNames = new ArrayList<String>();
    ArrayList<String> dinnerNdbnos = new ArrayList<>();
    ArrayList<String> snackNames = new ArrayList<String>();
    ArrayList<String> snackNdbnos = new ArrayList<>();

    String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseDatabase db = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        //TODO remove once searchclass is good
        AppData.updateDailyChecklist("Diet");

        mDatabse = FirebaseDatabase.getInstance().getReference();

        initializeButtons();

        setOnClickListeners();

        grabAllUsersDietSubmissions();

    }

    private void setOnClickListeners()
    {
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

    private void initializeButtons()
    {
        btnAddBreakfast = findViewById(R.id.addBreakfast_btn);
        btnAddLunch = findViewById(R.id.addLunch_btn);
        btnAddDinner = findViewById(R.id.addDinner_btn);
        btnAddSnacks = findViewById(R.id.addSnacks_btn);
    }

    private void grabAllUsersDietSubmissions()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference Date_node = db.getReference()
                                        .child("Data")
                                        .child("Diet")
                                        .child(UID)
                                        .child(AppData.getTodaysDate());

        Date_node.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                // Collects all of the users previously submitted data
                for (DataSnapshot d : dataSnapshot.getChildren())
                {
                    String meal = d.getKey();
                    grabMealSubmitted(meal,d);
                }
            }

            private void grabMealSubmitted(String meal, DataSnapshot d)
            {

                String foodID = (String) d.child("Food NDBs").getValue();
                String foodNames = String.valueOf(d.child("Food Names").getValue());

                foodNames = foodNames.replace("[" ,"");
                foodNames = foodNames.replace("]","");

                foodID = foodID.replace("[","");
                foodID = foodID.replace("]","");

              //  int index = foodNames.indexOf("UPC");

           //     String temp = foodNames.substring(0,index-1);

                breakfastNdbnos =   new ArrayList<String>(Arrays.asList(foodID.split(",")));
                breakfastNames =   new ArrayList<String>(Arrays.asList(foodNames.split(",")));

                int numFoodItems = breakfastNames.size();
                for(int i=0; i<numFoodItems; i++)
                {
                    String temp = breakfastNames.get(i);
                    if(temp.contains("UPC:"))
                    {
                        breakfastNames.remove(temp);
                        numFoodItems-- ;
                        i--;
                        continue;
                    }

                    Log.i("NAMESTEMP", temp);
                    Log.i("NAMESLIST", breakfastNames.get(i));
                }

                addFoodItems();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

    }



    private void addFoodItems()
     {
        LinearLayout linear = findViewById(R.id.Diet_LL_Breakfast);

        for (int j = 0; j < breakfastNdbnos.size(); j++)
        {
            LinearLayout childLayout = new LinearLayout(this);

            LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            childLayout.setOrientation(LinearLayout.VERTICAL);

            childLayout.setLayoutParams(linearParams);

            TextView fName = new TextView(this);
            TextView fID = new TextView(this);

            fName.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT, 1f));

            fID.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT, 1f));

            fName.setTextSize(17);
            fName.setPadding(5, 3, 0, 3);
            fName.setTypeface(Typeface.DEFAULT_BOLD);
            fName.setGravity(Gravity.LEFT | Gravity.CENTER);

            fID.setTextSize(16);
            fID.setPadding(5, 3, 0, 3);
            fID.setTypeface(null, Typeface.ITALIC);
            fID.setGravity(Gravity.LEFT | Gravity.CENTER);

            fName.setText(breakfastNames.get(j));
            fID.setText(breakfastNdbnos.get(j));

            childLayout.addView(fName);
            childLayout.addView(fID);

            linear.addView(childLayout);
        }
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
            String intFoodNdbNo = Integer.toString(thisFoodNbdNo);
            Log.i("Logging foodName: " ,thisFoodName);
            Log.i("Logging NdbNo: " ,intFoodNdbNo);

            String date = AppData.getTodaysDate();

            /*
            Creates a Diet Child Node under Data
            Creates a UID Child Node under Diet
            */
            DatabaseReference Diet_node = db.getReference().child("Data").child("Diet");
            DatabaseReference UID_node = Diet_node.child(UID);

            switch(requestCode)
            {
                case 1111: // Breakfast
                    breakfastNames.add(thisFoodName);
                    breakfastNdbnos.add(intFoodNdbNo);

                    String bnames = breakfastNames.toString();
                    String bndbs = breakfastNdbnos.toString();;
                    UID_node.child(date).child("Breakfast").child("Food Names").setValue(bnames);
                    UID_node.child(date).child("Breakfast").child("Food NDBs").setValue(bndbs);
                    return;

                case 2222: // Lunch
                    lunchNames.add(thisFoodName);
                    lunchNdbnos.add(intFoodNdbNo);
                    String lnames = lunchNames.toString();
                    String lndbs = lunchNdbnos.toString();
                    UID_node.child(date).child("Lunch").child("Food Names").setValue(lnames);
                    UID_node.child(date).child("Lunch").child("Food NDBs").setValue(lndbs);
                    return;

                case 3333: // Dinner
                    dinnerNames.add(thisFoodName);
                    dinnerNdbnos.add(intFoodNdbNo);
                    String dnames = dinnerNames.toString();
                    String dndbs = dinnerNdbnos.toString();
                    UID_node.child(date).child("Dinner").child("Food Names").setValue(dnames);
                    UID_node.child(date).child("Dinner").child("Food NDBs").setValue(dndbs);
                    return;

                case 4444: // Snacks
                    snackNames.add(thisFoodName);
                    snackNdbnos.add(intFoodNdbNo);
                    String snames = snackNames.toString();
                    String sndbs = snackNdbnos.toString();
                    UID_node.child(date).child("Snack").child("Food Names").setValue(snames);
                    UID_node.child(date).child("Snack").child("Food NDBs").setValue(sndbs);
                    return;

                default:
                    breakfastNames.add(thisFoodName);
                    breakfastNdbnos.add(intFoodNdbNo);
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


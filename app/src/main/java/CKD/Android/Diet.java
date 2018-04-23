package CKD.Android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Diet extends AppCompatActivity {

    // Request Codes for different meals
    public static final int REQUEST_CODE_Breakfast = 1111;
    public static final int REQUEST_CODE_Lunch = 2222;
    public static final int REQUEST_CODE_Dinner = 3333;
    public static final int REQUEST_CODE_Snacks = 4444;

    // Threshold Values
    public static final int POTASSIUM_THRESHOLD = 1000;
    public static final int PHOSPHORUS_THRESHOLD = 1000;
    public static final int SODIUM_THRESHOLD = 770;

    // String for nutrients thresholds
    String PhosphorusMsg = "", PotassiumMsg = "", SodiumMsg = "";

    private Button btnAddBreakfast, btnAddNoBreakfast, btnAddLunch, btnAddNoLunch,
                    btnAddDinner, btnAddNoDinner, btnAddSnacks, btnAddNoSnacks,
                    btnHome, btnNutritionBlog;

    // Variables to hold return of intent
    private String thisFoodName;
    private int thisFoodNbdNo, thisFoodQuantity, Potassium, Phosphorus, Sodium;

    // Arraylists for foodNames and Ndbnos for each meal
    ArrayList<String> breakfastNames, breakfastNdbnos, breakfastQuantity, breakfastPotassium, breakfastPhosphorus, breakfastSodium,
                        lunchNames, lunchNdbnos,lunchQuantity, lunchPotassium, lunchPhosphorus, lunchSodium,
                        dinnerNames, dinnerNdbnos, dinnerQuantity, dinnerPotassium, dinnerPhosphorus, dinnerSodium,
                        snackNames, snackNdbnos, snackQuantity, snackPotassium, snackPhosphorus, snackSodium;

    // Maps for name, list, ndbno, quantity, potassium, phosphorus, sodium
    Map <String,ArrayList<String>> mealNameLists = new HashMap<>();
    Map <String,ArrayList<String>> mealNdbnoLists = new HashMap<>();
    Map <String,ArrayList<String>> mealQuantityLists = new HashMap<>();
    Map <String,ArrayList<String>> mealPotassiumLists = new HashMap<>();
    Map <String,ArrayList<String>> mealPhosphrusLists = new HashMap<>();
    Map <String,ArrayList<String>> mealSodiumLists = new HashMap<>();

    // Maps for holding the buttons
    Map <String,Button> addMealBtns = new HashMap<>();
    Map <String,Button> noMealBtns = new HashMap<>();

    // Map for linear layout
    Map <String, LinearLayout> llList = new HashMap<>();

    // Grab UID from DB Global var and firebase instance
    String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseDatabase db = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        initializeButtons();
        initializeAndStoreMealNameLists();
        initializeAndStoreMealLayoutLists();
        setOnClickListeners();

        grabAllUsersDietSubmissions();

    }

    private void initializeAndStoreMealLayoutLists()
    {
        LinearLayout breakfastLL = findViewById(R.id.Diet_LL_Breakfast);
        LinearLayout lunchLL = findViewById(R.id.Diet_LL_Lunch);
        LinearLayout dinnerll = findViewById(R.id.Diet_LL_Dinner);
        LinearLayout snacksll = findViewById(R.id.Diet_LL_Snacks);

        llList.put("Breakfast", breakfastLL);
        llList.put("Lunch",lunchLL);
        llList.put("Dinner",dinnerll);
        llList.put("Snacks",snacksll);
    }

    private void initializeAndStoreMealNameLists()
    {
        // Arraylists for foodNames and Ndbnos for each meal
        breakfastNames = new ArrayList<>();
        mealNameLists.put("Breakfast",breakfastNames);

        breakfastNdbnos = new ArrayList<>();
        mealNdbnoLists.put("Breakfast", breakfastNdbnos);

        breakfastQuantity = new ArrayList<>();
        mealQuantityLists.put("Breakfast", breakfastQuantity);

        breakfastPotassium = new ArrayList<>();
        mealPotassiumLists.put("Breakfast", breakfastPotassium);

        breakfastPhosphorus = new ArrayList<>();
        mealPhosphrusLists.put("Breakfast", breakfastPhosphorus);

        breakfastSodium = new ArrayList<>();
        mealSodiumLists.put("Breakfast", breakfastSodium);

        lunchNames = new ArrayList<>();
        mealNameLists.put("Lunch",lunchNames);

        lunchNdbnos = new ArrayList<>();
        mealNdbnoLists.put("Lunch",lunchNdbnos);

        lunchQuantity = new ArrayList<>();
        mealQuantityLists.put("Lunch",lunchQuantity);

        lunchPotassium = new ArrayList<>();
        mealPotassiumLists.put("Lunch", lunchPotassium);

        lunchPhosphorus = new ArrayList<>();
        mealPhosphrusLists.put("Lunch", lunchPhosphorus);

        lunchSodium = new ArrayList<>();
        mealSodiumLists.put("Lunch", lunchSodium);

        dinnerNames = new ArrayList<>();
        mealNameLists.put("Dinner",dinnerNames);

        dinnerNdbnos = new ArrayList<>();
        mealNdbnoLists.put("Dinner", dinnerNdbnos);

        dinnerQuantity = new ArrayList<>();
        mealQuantityLists.put("Dinner", dinnerQuantity);

        dinnerPotassium = new ArrayList<>();
        mealPotassiumLists.put("Dinner", dinnerPotassium);

        dinnerPhosphorus = new ArrayList<>();
        mealPhosphrusLists.put("Dinner", dinnerPhosphorus);

        dinnerSodium = new ArrayList<>();
        mealSodiumLists.put("Dinner", dinnerSodium);

        snackNames = new ArrayList<>();
        mealNameLists.put("Snacks",snackNames);

        snackNdbnos = new ArrayList<>();
        mealNdbnoLists.put("Snacks",snackNdbnos);

        snackQuantity = new ArrayList<>();
        mealQuantityLists.put("Snacks",snackQuantity);

        snackPotassium = new ArrayList<>();
        mealPotassiumLists.put("Snacks", snackPotassium);

        snackPhosphorus = new ArrayList<>();
        mealPhosphrusLists.put("Snacks", snackPhosphorus);

        snackSodium = new ArrayList<>();
        mealSodiumLists.put("Snacks", snackSodium);

    }

    private void setOnClickListeners() {
        btnAddBreakfast.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Indicate Breakfast Button was clicked
                Intent intent = SearchFood.makeIntent(Diet.this);
                //startActivity(myIntent);
                startActivityForResult(intent, REQUEST_CODE_Breakfast);
            }
        });

        btnAddNoBreakfast.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // update UI
                updateUIForNoMeal(addMealBtns.get("Breakfast"), noMealBtns.get("Breakfast"));

                // call add no meal function
                addNoMealToDatabase(REQUEST_CODE_Breakfast);
            }
        });

        btnAddLunch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Indicate Lunch Button was clicked
                Intent intent = SearchFood.makeIntent(Diet.this);
                //startActivity(myIntent);
                startActivityForResult(intent, REQUEST_CODE_Lunch);
            }
        });

        btnAddNoLunch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // update UI
                updateUIForNoMeal(addMealBtns.get("Lunch"), noMealBtns.get("Lunch"));

                // call add no meal function
                addNoMealToDatabase(REQUEST_CODE_Lunch);
            }
        });

        btnAddDinner.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Indicate Breakfast Button was clicked
                Intent intent = SearchFood.makeIntent(Diet.this);
                //startActivity(myIntent);
                startActivityForResult(intent, REQUEST_CODE_Dinner);
            }
        });

        btnAddNoDinner.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // update UI
                updateUIForNoMeal(addMealBtns.get("Dinner"), noMealBtns.get("Dinner"));

                // call add no meal function
                addNoMealToDatabase(REQUEST_CODE_Dinner);
            }
        });

        btnAddSnacks.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Indicate Breakfast Button was clicked
                Intent intent = SearchFood.makeIntent(Diet.this);
                //startActivity(myIntent);
                startActivityForResult(intent, REQUEST_CODE_Snacks);
            }
        });

        btnAddNoSnacks.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // update UI
                updateUIForNoMeal(addMealBtns.get("Snacks"), noMealBtns.get("Snacks"));

                // call add no meal function
                addNoMealToDatabase(REQUEST_CODE_Snacks);
            }
        });

        btnNutritionBlog.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Redirect user to nutrition blog page
                Intent myIntent = new Intent(CKD.Android.Diet.this,NutritionBlog.class);
                startActivity(myIntent);
            }
        });
    }

    private void initializeButtons()
    {
        // Initialize buttons by the ID and add them to corresponding Map to update UI if needed
        btnAddBreakfast = findViewById(R.id.addBreakfast_btn);
        addMealBtns.put("Breakfast", btnAddBreakfast);
        btnAddNoBreakfast = findViewById(R.id.addNoBreakfast_btn);
        noMealBtns.put("Breakfast", btnAddNoBreakfast);
        btnAddLunch = findViewById(R.id.addLunch_btn);
        addMealBtns.put("Lunch", btnAddLunch);
        btnAddNoLunch = findViewById(R.id.addNoLunch_btn);
        noMealBtns.put("Lunch", btnAddNoLunch);
        btnAddDinner = findViewById(R.id.addDinner_btn);
        addMealBtns.put("Dinner", btnAddDinner);
        btnAddNoDinner = findViewById(R.id.addNoDinner_btn);
        noMealBtns.put("Dinner", btnAddNoDinner);
        btnAddSnacks = findViewById(R.id.addSnacks_btn);
        addMealBtns.put("Snacks", btnAddSnacks);
        btnAddNoSnacks = findViewById(R.id.addNoSnacks_btn);
        noMealBtns.put("Snacks", btnAddNoSnacks);

        btnHome = findViewById(R.id.Diet_BTN_Home);
        btnHome = AppData.activateHomeButton(btnHome,Diet.this);

        btnNutritionBlog = findViewById(R.id.Diet_BTN_NutritionBlog);
        AppData.disableBtn(btnNutritionBlog);

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

                if (meal != null)
                {
                    AppData.enableBtn(btnNutritionBlog);
                    btnNutritionBlog.setBackground(getResources().getDrawable(R.drawable.rounded_corner_textview));
                }
                // Initial Check from Database for if the user ate or not
                boolean noMeal = (Boolean) d.child("noMeal").getValue();

                // If they did not eat update UI and return immediately
                if (noMeal)
                {
                    updateUIForNoMeal(addMealBtns.get(meal), noMealBtns.get(meal));
                    return;
                }

                String foodID = (String) d.child("FoodNDBs").getValue();
                String foodNames = (String) d.child("FoodNames").getValue();
                String foodQuantity = (String) d.child("FoodQuantity").getValue();
                String PotassiumLvl = (String) d.child("PotassiumValues").getValue();
                String PhosphorusLvl = (String) d.child("PhosphorusValues").getValue();
                String SodiumLvl = (String) d.child("SodiumValues").getValue();

                assert foodNames != null;
                foodNames = foodNames.replace("[" ,"");
                foodNames = foodNames.replace("]","");

                assert foodID != null;
                foodID = foodID.replace("[","");
                foodID = foodID.replace("]","");

                assert foodQuantity != null;
                foodQuantity = foodQuantity.replace("[","");
                foodQuantity = foodQuantity.replace("]","");

                assert PotassiumLvl != null;
                PotassiumLvl = PotassiumLvl.replace("[","");
                PotassiumLvl = PotassiumLvl.replace("]","");

                assert PhosphorusLvl != null;
                PhosphorusLvl = PhosphorusLvl.replace("[","");
                PhosphorusLvl = PhosphorusLvl.replace("]","");

                assert SodiumLvl != null;
                SodiumLvl = SodiumLvl.replace("[","");
                SodiumLvl = SodiumLvl.replace("]","");

                // Commas within the Name of each food item are currently replaced with "_"
                ArrayList<String> ndbnoList = new ArrayList<>(Arrays.asList(foodID.split(",")));
                ArrayList<String> nameList  = new ArrayList<>(Arrays.asList(foodNames.split(",")));
                ArrayList<String> quantityList  = new ArrayList<>(Arrays.asList(foodQuantity.split(",")));
                ArrayList<String> potassiumList  = new ArrayList<>(Arrays.asList(PotassiumLvl.split(",")));
                ArrayList<String> phosphorusList  = new ArrayList<>(Arrays.asList(PhosphorusLvl.split(",")));
                ArrayList<String> sodiumList  = new ArrayList<>(Arrays.asList(SodiumLvl.split(",")));


                // Stores the data into the HashMap for corresponding meal
                mealNameLists.get(meal).addAll(nameList);
                mealNdbnoLists.get(meal).addAll(ndbnoList);
                mealQuantityLists.get(meal).addAll(quantityList);
                mealPotassiumLists.get(meal).addAll(potassiumList);
                mealPhosphrusLists.get(meal).addAll(phosphorusList);
                mealSodiumLists.get(meal).addAll(sodiumList);

                AppData.disableBtn(noMealBtns.get(meal));
                updateUI(meal);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

    }

    private void updateUI (String meal)
     {
        LinearLayout linear = llList.get(meal);
        //This prevents the UI from adding views already added into the LL
        int start = linear.getChildCount();

         ArrayList<String> nameList = mealNameLists.get(meal);
         ArrayList<String> quantityList = mealQuantityLists.get(meal);
         ArrayList<String> phosphorusList = mealPhosphrusLists.get(meal);
         ArrayList<String> potassiumList = mealPotassiumLists.get(meal);
         ArrayList<String> sodiumList = mealSodiumLists.get(meal);

         DisplayMetrics dm = new DisplayMetrics();

         getWindowManager().getDefaultDisplay().getMetrics(dm);

         int width = (int) (dm.widthPixels*.8);

         LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(width,LinearLayout.LayoutParams.WRAP_CONTENT);

        for (int j = start ; j < nameList.size() ; j++)
        {
            LinearLayout childLayout = new LinearLayout(this);

            childLayout.setOrientation(LinearLayout.VERTICAL);

            childLayout.setLayoutParams(linearParams);

            TextView fName = new TextView(this);
            TextView fquantity = new TextView(this);

            fName.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT, 1f));

            fquantity.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT, 1f));

            fName.setTextSize(17);
            fName.setPadding(5, 3, 0, 3);
            fName.setTypeface(Typeface.DEFAULT_BOLD);
            fName.setGravity(Gravity.START | Gravity.CENTER);

            fquantity.setTextSize(16);
            fquantity.setPadding(5, 3, 0, 3);
            fquantity.setTypeface(null, Typeface.ITALIC);
            fquantity.setGravity(Gravity.START | Gravity.CENTER);

            fquantity.setBackgroundColor(Color.GREEN);
            fName.setBackgroundColor(Color.GREEN);

            if (Integer.parseInt(phosphorusList.get(j).trim()) > PHOSPHORUS_THRESHOLD - 500)
            {

                fquantity.setBackgroundColor(getResources().getColor(R.color.LightYellow));
                fName.setBackgroundColor(Color.YELLOW);
                PhosphorusMsg = " * Moderately high in Phosphorus";

                if (Integer.parseInt(phosphorusList.get(j).trim()) > PHOSPHORUS_THRESHOLD)
                {
                    fquantity.setBackgroundColor(getResources().getColor(R.color.LightCoral));
                    fName.setBackgroundColor(getResources().getColor(R.color.LightCoral));
                    PhosphorusMsg = " * High in Phosphorus";

                }

            }

            else if (Integer.parseInt(potassiumList.get(j).trim()) > POTASSIUM_THRESHOLD - 500)
            {

                fquantity.setBackgroundColor(Color.YELLOW);
                fName.setBackgroundColor(Color.YELLOW);
                PotassiumMsg = " * Moderately high in Potassium";

                if (Integer.parseInt(potassiumList.get(j).trim()) > POTASSIUM_THRESHOLD)
                {
                    fquantity.setBackgroundColor(getResources().getColor(R.color.LightCoral));
                    fName.setBackgroundColor(getResources().getColor(R.color.LightCoral));
                    PotassiumMsg = " * High in Potassium";
                }

            }

            if (Integer.parseInt(sodiumList.get(j).trim()) > SODIUM_THRESHOLD - 500)
            {

                fquantity.setBackgroundColor(Color.YELLOW);
                fName.setBackgroundColor(Color.YELLOW);
                SodiumMsg = " * Moderately high in Sodium";

                if (Integer.parseInt(sodiumList.get(j).trim()) > SODIUM_THRESHOLD)
                {
                    fquantity.setBackgroundColor(getResources().getColor(R.color.LightCoral));
                    fName.setBackgroundColor(getResources().getColor(R.color.LightCoral));
                    SodiumMsg = " * High in Sodium";

                }

            }

            if (PhosphorusMsg == "" && PotassiumMsg == "" && SodiumMsg == "")
            {
                fName.setText(nameList.get(j).replaceAll("_",","));
                fquantity.setText("Quantity: ".concat(quantityList.get(j)));
            }

            else
            {
                fName.setText(nameList.get(j).replaceAll("_",","));
                fquantity.setText("Quantity: ".concat(quantityList.get(j)) + PhosphorusMsg + PotassiumMsg + SodiumMsg);
            }


            childLayout.addView(fName);
            childLayout.addView(fquantity);

            linear.addView(childLayout);
            linear.setBackground(this.getResources().getDrawable(R.drawable.rounded_corner_textview));

            PhosphorusMsg = "";
            PotassiumMsg = "";
            SodiumMsg = "";

        }

    }

    // Listener for result back from SearchFood
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == Activity.RESULT_OK)
        {
            thisFoodName = data.getStringExtra("FoodName");
            thisFoodNbdNo = data.getIntExtra("NdbNo", 0);
            thisFoodQuantity = data.getIntExtra("Quantity", 0);
            Potassium = data.getIntExtra("Potassium", 0);
            Phosphorus =  data.getIntExtra("Phosphorus", 0);
            Sodium =  data.getIntExtra("Sodium", 0);

            Log.i("Potassium Diet.java:", String.valueOf(Potassium));
            Log.i("Phosphorus Diet.java:", String.valueOf(Phosphorus));
            Log.i("Sodium Diet.java:", String.valueOf(Sodium));

            Potassium *= thisFoodQuantity;
            Phosphorus *= thisFoodQuantity;
            Sodium *= thisFoodQuantity;

            enterSelectedFoodItemIntoDatabase(requestCode, thisFoodName, thisFoodNbdNo,thisFoodQuantity, Potassium, Phosphorus, Sodium);
            updateUI(getMeal(requestCode));
        }

        else
        {
            // Handle bad result from SearchFood.java
        }
    }

    private void enterSelectedFoodItemIntoDatabase(int requestCode, String thisFoodName, int thisFoodNbdNo,
                                                   int quantity, int PotassiumLvl, int PhosphorusLvl, int SodiumLvl)
    {
        String meal = getMeal(requestCode);
        String date = AppData.getTodaysDate();

        // Navigates to the correct date under the corresponding user
        DatabaseReference Diet_node = db.getReference().child("Data").child("Diet");
        DatabaseReference Date_node = Diet_node
                                        .child(UID)
                                        .child(date);

        mealNameLists.get(meal).add(thisFoodName);
        mealNdbnoLists.get(meal).add(String.valueOf(thisFoodNbdNo));
        mealQuantityLists.get(meal).add(String.valueOf(quantity));
        mealPotassiumLists.get(meal).add(String.valueOf(PotassiumLvl));
        mealPhosphrusLists.get(meal).add(String.valueOf(PhosphorusLvl));
        mealSodiumLists.get(meal).add(String.valueOf(SodiumLvl));

        String fnames = mealNameLists.get(meal).toString();
        String fndbs = mealNdbnoLists.get(meal).toString();
        String fQuantity = mealQuantityLists.get(meal).toString();
        String fPotassium = mealPotassiumLists.get(meal).toString();
        String fPhosphorus = mealPhosphrusLists.get(meal).toString();
        String fSodium = mealSodiumLists.get(meal).toString();

        Date_node.child(meal).child("noMeal").setValue(false);
        AppData.disableBtn(noMealBtns.get(meal));
        AppData.enableBtn(btnNutritionBlog);
        Date_node.child(meal).child("FoodNames").setValue(fnames);
        Date_node.child(meal).child("FoodNDBs").setValue(fndbs);
        Date_node.child(meal).child("FoodQuantity").setValue(fQuantity);
        Date_node.child(meal).child("PotassiumValues").setValue(fPotassium);
        Date_node.child(meal).child("PhosphorusValues").setValue(fPhosphorus);
        Date_node.child(meal).child("SodiumValues").setValue(fSodium);

    }

    private void addNoMealToDatabase(int requestCode)
    {
        String meal = getMeal(requestCode);
        String date = AppData.getTodaysDate();

        // Navigates to the correct date under the corresponding user
        DatabaseReference Diet_node = db.getReference().child("Data").child("Diet");
        DatabaseReference Date_node = Diet_node
                .child(UID)
                .child(date);

        Date_node.child(meal).child("noMeal").setValue(true);
    }

    public void updateUIForNoMeal(Button addMeal, Button addNoMeal)
    {

        addNoMeal.setEnabled(false);
        addMeal.setEnabled(false);

        addNoMeal.setClickable(false);
        addMeal.setClickable(false);

        addNoMeal.setText("No Meal");
        addMeal.setText("");

        addNoMeal.setBackground(getResources().getDrawable(R.drawable.rounded_corner_textview));
        addMeal.setBackgroundColor(Color.TRANSPARENT);

    }

    private String getMeal(int requestCode)
    {
        switch(requestCode)
        {
            case 1111: // Breakfast
                return "Breakfast";

            case 2222: // Lunch
                return "Lunch";

            case 3333: // Dinner
                return "Dinner";

            case 4444: // Snacks
                return "Snacks";

            default: // Incorrect requestCode
                return "Breakfast";
        }
    }

}


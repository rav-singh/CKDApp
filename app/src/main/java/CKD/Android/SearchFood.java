package CKD.Android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.jar.Attributes;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SearchFood extends AppCompatActivity {

    private static final String TAG = "Diet";
    private OkHttpClient okHttpClient;
    private Request request;
    private String apiKey = "kOa0Zd0guy7xjuq3uPP0qYKvZlXRGoT0Joxaidud";
    private String usdaURL;
    private Button btnSearchFood, btnHome;
    private EditText searchedFood;
    private int ndbno, chosenNDBNO;
    private foodItem foods;
    ArrayList<foodItem> foodsList = new ArrayList<foodItem>();
    ArrayList<foodItem> parsedFoodsList = new ArrayList<foodItem>();
    private ListView lv;
    private int quantity;
    private Intent myIntent = new Intent();
    boolean grabbedP = false, grabbedK = false, grabbedNa = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);

        btnSearchFood = findViewById(R.id.searchFood_btn);

        btnHome = findViewById(R.id.searchFood_btn_home);
        btnHome = AppData.activateHomeButton(btnHome, SearchFood.this);

        searchedFood = findViewById(R.id.searchFood_EF);

        lv = findViewById(R.id.foods_LV);

        btnSearchFood.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                foodsList.clear();

                // Grab keywords from text field
                String item = searchedFood.getText().toString();
                Log.i(TAG, "You typed: " + item);

                // Generate api url based on user input , check for not null
                if (!item.isEmpty()) {
                    usdaURL = "https://api.nal.usda.gov/ndb/search/?format=json&q=" + item + "&sort=n&max=25&offset=0&api_key=" + apiKey;
                } else {
                    // handle error of empty search field
                }

                // Initialize a http client
                okHttpClient = new OkHttpClient();
                // Initialize a request
                request = new Request.Builder().url(usdaURL).build();

                // Execute the request
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i(TAG, e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String jsonData = response.body().string();
                        Log.i(TAG, jsonData);
                        if (jsonData.contains("timeout") || jsonData.contains("error")) ;
                        {
                            // handle api being down
                        }
                        // Parse the JSON into foodItem Class
                        try {

                            JSONObject jsonObj = new JSONObject(jsonData);
                            JSONObject foodItems = jsonObj.getJSONObject("list");
                            JSONArray dataInner = foodItems.getJSONArray("item");
                            // looping through all Food results
                            for (int i = 0; i < dataInner.length(); i++) {
                                JSONObject postObject = dataInner.getJSONObject(i);

                                ndbno = postObject.getInt("ndbno");

                                String foodName = postObject.getString("name");

                                foods = new foodItem(foodName, ndbno);
                                foodsList.add(foods);
                            }
                            parseNameStringsInFoodsList();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });


        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<foodItem> arrayAdapter = new ArrayAdapter<foodItem>(
                this,
                android.R.layout.simple_list_item_1,
                parsedFoodsList);

        lv.setAdapter(arrayAdapter);

        // Grab the user`s choice
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                foodItem selectedFood = (foodItem) lv.getItemAtPosition(i);

                myIntent.putExtra("FoodName", selectedFood.getName());
                myIntent.putExtra("NdbNo", selectedFood.getNdbno());

                chosenNDBNO = selectedFood.getNdbno();

                AlertDialog.Builder builder = new AlertDialog.Builder(SearchFood.this);

                builder.setTitle("Confirm Selection and Select Quantity");

                LinearLayout ll = new LinearLayout(SearchFood.this);
                ll.setOrientation(LinearLayout.VERTICAL);

                TextView selectedFoodName = new TextView(SearchFood.this);

                selectedFoodName.setText("You Selected: ".concat(selectedFood.getName()));
                ll.addView(selectedFoodName);

                final EditText input = new EditText(SearchFood.this);

                input.setInputType(InputType.TYPE_CLASS_NUMBER);

                ll.addView(input);

                builder.setView(ll);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String grabQuantity = input.getText().toString();

                        quantity = Integer.parseInt(grabQuantity);
                        myIntent.putExtra("Quantity", quantity);

                        int cnt = 1;
                        while (cnt <= 3) {
                            if (cnt == 1)
                                getNutritionFacts(305, "Phosphorus", cnt);

                            if (cnt == 2)
                                getNutritionFacts(306, "Potassium", cnt);

                            if (cnt == 3)
                                getNutritionFacts(307, "Sodium", cnt);

                            cnt++;
                        }

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });

    }

    // Each Foodname inside each FoodItem Class contains a Name with a UPC number
    // as well as commas within the name itself. We need to remove the UPC number
    // as well as commas with the name
    private void parseNameStringsInFoodsList() {
        for (foodItem food : foodsList) {
            // Contains name with commas and UPC number
            String name = food.getName();
            int ndbo = food.getNdbno();

            if (name.contains("UPC:")) {
                parseUPCorGTIN(name, "UPC", ndbo);
            } else if (name.contains("GTIN:")) {
                parseUPCorGTIN(name, "GTIN", ndbo);
            } else {
                parseUPCorGTIN(name, null, ndbo);
            }

        }
    }

    private void parseUPCorGTIN(String name, String remove, int ndbo) {
        if (remove != null) {
            int indexToRemove = name.indexOf(remove);

            // Removes UPC from String as well as commas
            String temp = name.substring(0, indexToRemove - 2);
            name = temp;
        }

        name = name.replaceAll("//[^A-Za-z0-9]//", "");
        name = name.replaceAll(",", "_");

        //Create a new foodItemClass to replace in the foodslist Array
        foodItem updatedFoodItem = new foodItem(name, ndbo);
        //Overwrites the foodItem Class in the array
        parsedFoodsList.add(updatedFoodItem);
    }

    private void getNutritionFacts(final int nutrientId, final String nutrient, final int count) {

    // Build API Request URL
    String url = "https://api.nal.usda.gov/ndb/nutrients/?format=json&api_key=" + apiKey + "&nutrients=" + nutrientId + "&ndbno=" + chosenNDBNO + "";

    // Initialize a http client
    okHttpClient = new OkHttpClient();

        // Initialize a request
        Request request = new Request.Builder().url(url).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            String jsonDataNutrients = response.body().string();
            Log.i(TAG, jsonDataNutrients);

            if (jsonDataNutrients.contains("timeout") || jsonDataNutrients.contains("error")) ;
            {
                // handle api being down
            }
            // Parse the JSON
            try {

                JSONObject jsonObj = new JSONObject(jsonDataNutrients);
                JSONObject report = jsonObj.getJSONObject("report");
                JSONArray foodsArray = report.getJSONArray("foods");

                // Missing Nutrient Information
                if (foodsArray.length() == 0)
                {
                    myIntent.putExtra(nutrient, 0);

                    if(nutrientId == 305)
                    {
                        Log.i("SearchFood.java", "No results for phosphorus");
                        grabbedP = true;
                        checkNutrientsForReturn();
                    }

                    else if(nutrientId ==  306)
                    {
                        Log.i("SearchFood.java", "No results for Potassium");
                        grabbedK = true;
                        checkNutrientsForReturn();
                    }

                    else
                    {
                        Log.i("SearchFood.java", "No results for Sodium");
                        grabbedNa = true;
                        checkNutrientsForReturn();
                    }

                    return;
                }


                // Looping through array to grab nutrient info
                for (int i = 0; i < foodsArray.length(); i++) {
                    // Grab JSON object and then the containing array
                    JSONObject postObject = foodsArray.getJSONObject(i);
                    JSONArray nutrientInfo = postObject.getJSONArray("nutrients");

                    for (int j = 0; j < nutrientInfo.length(); j++) {

                        JSONObject postObjectInner = nutrientInfo.getJSONObject(j);
                        int nutrientVal = postObjectInner.getInt("value");
                        myIntent.putExtra(nutrient, nutrientVal);
                        Log.i(nutrient, String.valueOf(nutrientVal));

                        if(nutrientId == 305)
                        {
                            Log.i("SearchFood.java", "Grabbed Phosphorus value");
                            grabbedP = true;
                            checkNutrientsForReturn();
                        }

                        else if(nutrientId ==  306)
                        {
                            Log.i("SearchFood.java", "Grabbed Potassium Value");
                            grabbedK = true;
                            checkNutrientsForReturn();
                        }

                        else
                        {
                            Log.i("SearchFood.java", "Grabbed Sodium Value");
                            grabbedNa = true;
                            checkNutrientsForReturn();
                        }

                    }
                }

            }

            catch (JSONException e) {
                e.printStackTrace();
            }

        }


    });

}

    public void checkNutrientsForReturn()
    {
        if (grabbedP && grabbedK && grabbedNa)
        {
            setResult(Activity.RESULT_OK, myIntent);
            AppData.updateDailyChecklist("Diet");
            finish();
        }
    }

    public static Intent makeIntent(Context context)
    {
        return new Intent(context, SearchFood.class);
    }

}

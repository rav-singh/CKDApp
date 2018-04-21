package CKD.Android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

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
    private int ndbno, chosenNDBNO, quantity;
    private foodItem foods;
    ArrayList<foodItem> foodsList = new ArrayList<>();
    ArrayList<foodItem> parsedFoodsList = new ArrayList<>();
    boolean databaseDoneReading = false;
    boolean foodsParsed = false;
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

        btnSearchFood.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // clears out the list every time a new search is executed
                foodsList.clear();

                // Grab keywords from text field
                String item = searchedFood.getText().toString();

                // Generate api url based on user input , check for not null
                if (!item.isEmpty()) {
                    usdaURL = "https://api.nal.usda.gov/ndb/search/?format=json&q=" + item + "&sort=n&max=25&offset=0&api_key=" + apiKey;
                } else {
                    setCustomToast("Please enter a food in search box");
                    return;
                }

                // Initialize http client
                okHttpClient = new OkHttpClient();
                // Initialize a request based on url
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
                        Log.i("json data:", jsonData);
                        if (jsonData.contains("timeout") || jsonData.contains("error")) {
                            setCustomToast("Timeout/Error on call to API");
                            return;
                        }

                        // Parse the JSON into foodItem class
                        try {

                            JSONObject jsonObj = new JSONObject(jsonData);
                            JSONObject foodItems = jsonObj.getJSONObject("list");
                            JSONArray dataInner = foodItems.getJSONArray("item");
                            // looping through all Food results
                            for (int i = 0; i < dataInner.length(); i++)
                            {
                                JSONObject postObject = dataInner.getJSONObject(i);
                                ndbno = postObject.getInt("ndbno");
                                String foodName = postObject.getString("name");

                                foods = new foodItem(foodName, ndbno);
                                foodsList.add(foods);
                                if(i == dataInner.length()-1)
                                {
                                    databaseDoneReading = true;
                                    parseNameStringsInFoodsList();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
                        upDateUI();
            }

        });

    }


    private void upDateUI()
    {
        while(!databaseDoneReading && !foodsParsed)
        {
            System.out.println("WAITINGGGG");
        }
        // Removes all views from previous search
        final ScrollView SV = findViewById(R.id.foods_SV);
        SV.removeAllViews();

        // Creates a new Linearlayout to add to the main LL.
        final LinearLayout templl = new LinearLayout(getApplicationContext());

        LinearLayout.LayoutParams llParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

        templl.setLayoutParams(llParams);
        templl.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams tvParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        tvParams.setMargins(0,0,0,10);


        for (int i = 0; i < parsedFoodsList.size(); i++)
        {
            TextView food = new TextView(this);
            food.setLayoutParams(tvParams);

            food.setText(parsedFoodsList.get(i).getName());
            food.setTextSize(20);
            food.setBackground(getResources().getDrawable(R.drawable.rounded_corner_textview));
            food.setTextColor(Color.BLACK);;
            templl.addView(food);
        }

            setOnClickListeners(templl);
            SV.addView(templl);
        // Hide keyboard on search
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

    }

    private void setOnClickListeners(LinearLayout templl)
    {
        int numFoods = parsedFoodsList.size();

        for(int i=0; i<numFoods;i++)
        {
            TextView view = (TextView) templl.getChildAt(i);
            final foodItem selectedFood = parsedFoodsList.get(i);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
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

                            if(quantity == 0 || quantity > 50)
                            {
                                setCustomToast("Invalid Quantity Try Again");
                                return;
                            }

                            myIntent.putExtra("Quantity", quantity);

                            // Make 3 calls separately so that we still get a JSON response even
                            // though one of the nutrients is missing
                            getNutritionFacts(305, "Phosphorus");
                            getNutritionFacts(306, "Potassium");
                            getNutritionFacts(307, "Sodium");

                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();

                }
            });

        }
    }

    // Each Foodname inside each FoodItem Class contains a Name with a UPC number
    // as well as commas within the name itself. We need to remove the UPC number
    // as well as commas with the name
    private void parseNameStringsInFoodsList()
    {
        for (foodItem food : foodsList)
        {
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
        foodsParsed = true;
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

    private void getNutritionFacts(final int nutrientId, final String nutrient) {

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

            if (jsonDataNutrients.contains("timeout") || jsonDataNutrients.contains("error"))
            {
                setCustomToast("Timeout/Error on call to API");
                return;
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

    // Set custom toast messages
    public void setCustomToast(final CharSequence text)
    {
        final Context context = getApplicationContext();
        final int duration = Toast.LENGTH_SHORT;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }

    // Checks if all values have been set so that all nutrients have a value
    // before returning intent
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

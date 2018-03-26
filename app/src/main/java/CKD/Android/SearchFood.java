package CKD.Android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
    private Button btnSearchFood;
    private EditText searchedFood;
    private foodItem foods;
    ArrayList<foodItem> foodsList = new ArrayList<foodItem>();
    ArrayList<foodItem> parsedfoodsList = new ArrayList<foodItem>();
    private ListView lv;
    private int grabPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);

        btnSearchFood = (Button) findViewById(R.id.searchFood_btn);
        searchedFood = findViewById(R.id.searchFood_EF);
        lv = (ListView) findViewById(R.id.foods_LV);

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
                        if(jsonData.contains("timeout") || jsonData.contains("error"));
                        {
                            //TODO TOAST TO LET USER KNOW
                        //    Intent launchActivity1= new Intent(SearchFood.this,HomePage.class);
                          //  startActivity(launchActivity1);
                        }
                        // Parse the JSON into foodItem Class
                        try {

                            JSONObject jsonObj = new JSONObject(jsonData);
                            JSONObject foodItems = jsonObj.getJSONObject("list");
                            JSONArray dataInner = foodItems.getJSONArray("item");
                            // looping through all Food results
                            for (int i = 0; i < dataInner.length(); i++) {
                                JSONObject postObject = dataInner.getJSONObject(i);
                                int ndbno = postObject.getInt("ndbno");
                                String foodName = postObject.getString("name");
                               Log.i("Data", "ndbno: " + ndbno);
                               Log.i("Data", "name: " + foodName);


                                foods = new foodItem(foodName, ndbno);
                                foodsList.add(foods);
                            }
                            parseNameStringsInFoodsList();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //TODO TOAST TO LET USER KNOW
                       //     Intent launchActivity1= new Intent(SearchFood.this,HomePage.class);
                         //   startActivity(launchActivity1);
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
                parsedfoodsList);

        lv.setAdapter(arrayAdapter);

        // Grab the user`s choice
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                foodItem selectedFood = (foodItem) lv.getItemAtPosition(i);
                Log.i("You selected :", selectedFood.toString());

                // Pass Data back to Diet class to log into the Database
                Intent myIntent = new Intent();
                Bundle extras = new Bundle();

                myIntent.putExtra("FoodName", selectedFood.getName());
                myIntent.putExtra("NdbNo", selectedFood.getNdbno());

                setResult(Activity.RESULT_OK, myIntent);
                //TODO REMOVE COMMENTING OUT ONCE API IS UP
              // AppData.updateDailyChecklist("Diet");
                finish();
            }
        });

}
    // Each Foodname inside each FoodItem Class contains a Name with a UPC number
    // as well as commas within the name itself. We need to remove the UPC number
    // as well as commas with the name
    private void parseNameStringsInFoodsList()
    {
        for(foodItem food : foodsList)
        {
            // Contains name with commas and UPC number
            String name = food.getName();
            int ndbo = food.getNdbno();

            if (name.contains("UPC:"))
            {
                int UPCINDEX = name.indexOf("UPC:");

                // Removes UPC from String as well as commas
                String temp = name.substring(0, UPCINDEX-2);

                temp = temp.replaceAll("//[^A-Za-z0-9]//","");
                temp = temp.replaceAll(",","_");

                //Create a new foodItemClass to replace in the foodslist Array
                foodItem updatedFoodItem = new foodItem(temp, ndbo);
                //Overwrites the foodItem Class in the array
               parsedfoodsList.add(updatedFoodItem);
            }
            else
                parsedfoodsList.add(food);

        }

        for(foodItem food : parsedfoodsList) {
            String temp = food.getName();
        }
    }


    public static Intent makeIntent(Context context) {
    return new Intent(context, SearchFood.class);
}

}

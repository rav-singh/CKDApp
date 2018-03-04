package CKD.Android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);

        btnSearchFood = (Button) findViewById(R.id.searchFood_btn);
        searchedFood = findViewById(R.id.searchFood_EF);

        btnSearchFood.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Grab keywords from text field
                String item = searchedFood.getText().toString();
                Log.i(TAG, "You typed: " +item);



                // Generate api url based on user input , check for not null
                if (!item.isEmpty())
                {
                    usdaURL = "https://api.nal.usda.gov/ndb/search/?format=json&q="+item+"&sort=n&max=25&offset=0&api_key="+apiKey;
                }

                else
                {
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
                        Log.i(TAG, response.body().string());

                    }
                });
            }
        });

    }
}
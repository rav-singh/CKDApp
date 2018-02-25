package CKD.Android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Diet extends AppCompatActivity {

    private static final String TAG = "Diet";
    private Button btnSendHTTP;
    private TextView etJSONResponse;
    private OkHttpClient okHttpClient;
    private Request request;
    private String url= "https://trackapi.nutritionix.com/v2/search/item?nix_item_id=513fc9e73fe3ffd40300109f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        TextView Temp = findViewById(R.id.Diet_TF_Title);
        btnSendHTTP = (Button) findViewById(R.id.sendHttp);
        etJSONResponse = (TextView) findViewById(R.id.jsonResponse);

        btnSendHTTP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("myTag", "Clicked send button");
                // Initialize a http client
                okHttpClient = new OkHttpClient();
                // Initialize a request
                request = new Request.Builder().url(url).header("x-app-id", "b1ae3ce7").header("x-app-key", "fd3652caba2d3af692b76870aac4e606").build();

                // Execute the request
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i(TAG,e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.i(TAG,response.body().string());
                        //string x =  Log.i(TAG,response.body().string());

                    }
                });
            }
        });
    }
}


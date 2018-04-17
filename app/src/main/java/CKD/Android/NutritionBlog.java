package CKD.Android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NutritionBlog extends AppCompatActivity {

    private TextView dateBox;
    String todaysDate;
    private Button NutritionLogButton;
    private TextView blogTitle;
    private TextView blogContent;
    FirebaseDatabase db = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_blog);

        dateBox = findViewById(R.id.NutritionBlog_TF_Date);
        todaysDate = AppData.getTodaysDate();
        Log.i("Today's Date", todaysDate);
        dateBox.setText(todaysDate);

        blogTitle = findViewById(R.id.NutritionBlog_TF_Blog_Title);
        blogContent = findViewById(R.id.NutritionBlog_TF_Blog_Text);

        DatabaseReference dataNode = db.getReference().child("Admins").child("DietBlog");

        dataNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title = dataSnapshot.child("Title").getValue(String.class);
                String body = dataSnapshot.child("Body").getValue(String.class);
                blogTitle.setText(title);
                blogContent.setText(body);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        NutritionLogButton = findViewById(R.id.NutritionBlog_btn_ActivityDietLog);

        NutritionLogButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(CKD.Android.NutritionBlog.this,Diet.class);
                startActivity(myIntent);


            }
        });

        Button home_btn = findViewById(R.id.NutritionBlog_btn_home);
        home_btn = AppData.activateHomeButton(home_btn,NutritionBlog.this);
    }
}

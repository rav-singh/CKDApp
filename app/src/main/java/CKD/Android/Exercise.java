package CKD.Android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Exercise extends AppCompatActivity
{
    Boolean checklistUpdated = false;
    private TextView authorBox;
    String title, body, author;
    private Button ActivityLogButton;
    private Button YTVideos;
    private TextView blogTitle;
    private TextView blogContent;
    FirebaseDatabase db = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        blogTitle = findViewById(R.id.Exercise_TF_Blog_Title);
        blogContent = findViewById(R.id.Exercise_TF_Blog_Text);
        authorBox = findViewById(R.id.Exercise_TF_Author);

        DatabaseReference dataNode = db.getReference().child("Admins").child("ExerciseBlog");
        dataNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                title = dataSnapshot.child("Title").getValue(String.class);
                body = dataSnapshot.child("Body").getValue(String.class);
                author = dataSnapshot.child("Author").getValue(String.class);

                blogTitle.setText(title);
                blogContent.setText(body);
                authorBox.setText(author);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ActivityLogButton = findViewById(R.id.Exercise_btn_ActivityLog);
        YTVideos = findViewById(R.id.Exercise_btn_YouTubeVideos);

        ActivityLogButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(CKD.Android.Exercise.this,ActivityLog.class);
                startActivity(myIntent);


            }
        });

        YTVideos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(CKD.Android.Exercise.this,Videos.class);
                startActivity(myIntent);


            }
        });


        //TODO Move this to a location that signifies the user did something on this page
        if(!checklistUpdated)
        {
            AppData.updateDailyChecklist("Exercise");
            checklistUpdated = true;
        }
    }
}


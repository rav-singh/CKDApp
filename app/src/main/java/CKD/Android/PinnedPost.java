package CKD.Android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PinnedPost extends AppCompatActivity
{

    TextView titleTV, bodyTV, authorTV;
    ScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinned_post);

        Button home_btn = findViewById(R.id.PP_btn_home);
        home_btn = AppData.activateHomeButton(home_btn,PinnedPost.this);

        loadPostFromDatabase();
    }

    private void loadPostFromDatabase()
    {
        titleTV = findViewById(R.id.PP_TF_Title);
        bodyTV = findViewById(R.id.PP_TF_Body);
        authorTV = findViewById(R.id.PP_TF_Author);

        FirebaseDatabase db = FirebaseDatabase.getInstance();

        DatabaseReference dataNode = db.getReference().child("Admins").child("PinnedPost");

        dataNode.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String title = dataSnapshot.child("title").getValue(String.class);
                String body = dataSnapshot.child("body").getValue(String.class);
                String author = dataSnapshot.child("author").getValue(String.class);

                titleTV.setText(title);
                bodyTV.setText(body);
                authorTV.setText(author);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });
    }
}


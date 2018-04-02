package CKD.Android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class NewThread extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_new_thread);

        final EditText threadTitle = findViewById(R.id.NewThread_TE_Title);
        final EditText threadBody=  findViewById(R.id.NewThread_TE_Body);
        Button submit_btn = findViewById(R.id.NewThread_Btn_Submit);

        Button home_btn = findViewById(R.id.NewThread_Btn_home);
        home_btn = AppData.activateHomeButton(home_btn,NewThread.this);



        submit_btn.setOnClickListener(new View.OnClickListener()
        {
            //TODO Add a max character limit for Title Length
            //TODO Make sure no fields are empty before proceeding
            public void onClick(View v)
            {
                AppData.updateParticipation("ThreadsMade");
                // Grabs all of the users enteries and an instance of the current time
                // Instantiates a new Thread class with the collected variable
                String authorUID = AppData.cur_user.getUID();
                String authorName = AppData.cur_user.getName();
                String title = threadTitle.getText().toString();
                String body = threadBody.getText().toString();
                String category = AppData.cur_Category;
                String date = AppData.getTodaysDate();

                ThreadClass newThread = new ThreadClass(authorName,authorUID, title, body, date, category);

                FirebaseDatabase db;

                if(AppData.db == null)
                {
                    db = FirebaseDatabase.getInstance();
                    AppData.db = db;
                }
                else
                    db = AppData.db;

                //References the Node under data and the current category the user is in
                DatabaseReference Category_node = db.getReference("Data").child("Social").child(category);

                // Creates a child under the given category node with an automatically
                // generated UID and saves it adding to the database
                String key = Category_node.push().getKey();

                // Under newly created thread UID the Thread Class is placed into the database
                // and then adds an empty child for comments to be added later
                Category_node.child(key).setValue(newThread);
                Category_node.child(key).child("Comments").setValue("No comments");

                Intent launchActivity1 =
                        new Intent(CKD.Android.NewThread.this,ThreadsList.class);
                startActivity(launchActivity1);
            }
        });
    }
}
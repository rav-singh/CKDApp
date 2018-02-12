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

        submit_btn.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                String authorUID = AppData.cur_user.getUID();
                String authorName = AppData.cur_user.getName();
                String title = threadTitle.getText().toString();
                String body = threadBody.getText().toString();
                String category = AppData.cur_Category;
                String date = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss",
                        Locale.getDefault()).format(new Date());

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
                String key = Category_node.push().getKey();

                Category_node.child(key).setValue(newThread);
                Category_node.child(key).child("Comments").setValue("No comments");


                Intent launchActivity1 =
                        new Intent(CKD.Android.NewThread.this,ThreadsList.class);
                startActivity(launchActivity1);
            }


        });

    }





}
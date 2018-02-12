package CKD.Android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class Thread extends AppCompatActivity
{
    final List<String> keyList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_thread);
        Button comment_btn = findViewById(R.id.Thread_Btn_Comment);

        getCurrentThread();
        getComments();


        comment_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent launchActivity1=
                        new Intent(CKD.Android.Thread.this,Comment.class);
                startActivity(launchActivity1);
            }
        });


    }

    private void getComments()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference Comments_node = db.getReference("Data").child("Social").child(AppData.cur_Category).child(AppData.cur_Thread_Key).child("Comments");

        Comments_node.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot d : dataSnapshot.getChildren())
                {
                    keyList.add(d.getKey());

                }

                TextView Comment1 = findViewById(R.id.Thread_TV_Comment1);
                TextView Comment1Auth = findViewById(R.id.Thread_TV_Cmnt1_author);

                //If there are no comments on this thread
                if (keyList.size() == 0)
                {
                    Comment1.setText("There are no comments on this thread");
                    return;
                }
                //TODO Implement a way to dynamically fill all/some of the comment boxes
                fillInComments(Comment1, Comment1Auth,0,dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    private void fillInComments(TextView comment1, TextView comment1Auth, int i, DataSnapshot dataSnapshot)
    {
        comment1.setText((String)dataSnapshot.child(keyList.get(i)).child("comment").getValue());
        comment1Auth.setText((String)dataSnapshot.child(keyList.get(0)).child("userName").getValue());
    }

    private void fillInThread(ThreadClass thread)
    {
        TextView threadTitle = findViewById(R.id.Thread_TV_ThreadTitle);
        TextView threadBody = findViewById(R.id.Thread_TV_ThreadBody);
        TextView threadAuthor = findViewById(R.id.Thread_TV_ThreadAuthor);

        threadTitle.setText(thread.getTitle());
        threadAuthor.setText(thread.getAuthor());
        threadBody.setText(thread.getBody());
    }

    private void getCurrentThread()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        DatabaseReference Thread_node = db.getReference("Data").child("Social").
                child(AppData.cur_Category).child(AppData.cur_Thread_Key);

        Thread_node.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                // Use this thread to fill the values on the page
                ThreadClass thread = dataSnapshot.getValue(ThreadClass.class);
                fillInThread(thread);

                AppData.setCurrentThread(thread);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("HELO", "Failed to read value.", error.toException());
            }
        });
    }




}

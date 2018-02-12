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


public class Thread extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_thread);

        TextView comment1 = findViewById(R.id.Thread_TV_Comment1);
        TextView cmnt1Author = findViewById(R.id.Thread_TV_Cmnt1_author);

        TextView comment2 = findViewById(R.id.Thread_TV_Comment2);
        TextView cmnt2Author = findViewById(R.id.Thread_TV_Cmnt2_author);

        TextView comment3 = findViewById(R.id.Thread_TV_Comment3);
        TextView cmnt3Author = findViewById(R.id.Thread_TV_Cmnt3_author);

        TextView comment4 = findViewById(R.id.Thread_TV_Comment4);
        TextView cmnt4Author = findViewById(R.id.Thread_TV_Cmnt4_author);

        TextView comment5 = findViewById(R.id.Thread_TV_Comment5);
        TextView cmnt5Author = findViewById(R.id.Thread_TV_Cmnt5_author);

        getCurrentThread();
        ThreadClass currentThread = AppData.cur_Thread;



    }

    private void getCurrentThread() {

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

                TextView threadTitle = findViewById(R.id.Thread_TV_ThreadTitle);
                TextView threadBody = findViewById(R.id.Thread_TV_ThreadBody);
                TextView threadAuthor = findViewById(R.id.Thread_TV_ThreadAuthor);


                threadTitle.setText(thread.getTitle());
                threadAuthor.setText(thread.getAuthor());
                threadBody.setText(thread.getBody());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("HELO", "Failed to read value.", error.toException());
            }
        });
    }
}

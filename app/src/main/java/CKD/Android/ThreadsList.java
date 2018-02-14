package CKD.Android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ThreadsList extends AppCompatActivity
{
    final List<String> keyList= new ArrayList<>();
    final List<Button> threadList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threads);

        TextView categoryTitle = findViewById(R.id.ThreadsList_TV_Category);
        Button thread1 = findViewById(R.id.ThreadsList_Btn_Thread1);
        Button thread2 = findViewById(R.id.ThreadsList_Btn_Thread2);
        Button thread3 = findViewById(R.id.ThreadsList_Btn_Thread3);
        Button thread4 = findViewById(R.id.ThreadsList_Btn_Thread4);
        Button nextPage = findViewById(R.id.ThreadsList_Btn_Next);
        Button newThread = findViewById(R.id.ThreadsList_Btn_NewThread);

        categoryTitle.setText(AppData.cur_Category);

        grabKeysInCategory();


        thread1.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                AppData.cur_Thread_Key = keyList.get(keyList.size()-1);
                Intent launchActivity1 =
                        new Intent(CKD.Android.ThreadsList.this,Thread.class);
                startActivity(launchActivity1);
            }
        });

        thread2.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                AppData.cur_Thread_Key = keyList.get(keyList.size()-2);
                Intent launchActivity1 =
                        new Intent(CKD.Android.ThreadsList.this,Thread.class);
                startActivity(launchActivity1);
            }
        });

        thread3.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                AppData.cur_Thread_Key = keyList.get(keyList.size()-3);
                Intent launchActivity1 =
                        new Intent(CKD.Android.ThreadsList.this,Thread.class);
                startActivity(launchActivity1);
            }
        });

        thread4.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                AppData.cur_Thread_Key = keyList.get(keyList.size()-4);
                Intent launchActivity1 =
                        new Intent(CKD.Android.ThreadsList.this,Thread.class);
                startActivity(launchActivity1);
            }
        });

        nextPage.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                //TODO Grab next 4 Threads
            }
        });

        newThread.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent launchActivity1 =
                        new Intent(CKD.Android.ThreadsList.this,NewThread.class);
                startActivity(launchActivity1);
            }
        });

    }

    private void grabKeysInCategory()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference Category_node = db.getReference("Data").child("Social").child(AppData.cur_Category);

        Category_node.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot d : dataSnapshot.getChildren())
                {
                    keyList.add(d.getKey());
                }

                Button thread1 = findViewById(R.id.ThreadsList_Btn_Thread1);
                threadList.add(thread1);

                Button thread2 = findViewById(R.id.ThreadsList_Btn_Thread2);
                thread1.setText("");
                threadList.add(thread2);

                Button thread3 = findViewById(R.id.ThreadsList_Btn_Thread3);
                thread1.setText("");
                threadList.add(thread3);

                Button thread4 = findViewById(R.id.ThreadsList_Btn_Thread4);
                thread1.setText("");
                threadList.add(thread4);

                if(keyList.size() == 0)
                {
                    thread1.setText("There are no current threads in this topic!!!");
                    return;
                }

                for(int j = 0, i = keyList.size()-1; i>=0 && j < 4; i--, j++ )
                {
                    fillThread(threadList.get(j), keyList.get(i), dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

    }

    private void fillThread(Button thread, String threadKey, DataSnapshot dataSnapshot)
    {
        String title = (String) dataSnapshot.child(threadKey).child("title").getValue();
        String author = (String) dataSnapshot.child(threadKey).child("author").getValue();
        thread.setText(title.concat("    ").concat("By:").concat(author));
    }
}
package CKD.Android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
    final List<Button> allThreadBtnList= new ArrayList<>();
    List<Button> activeThreadBtnList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_threads);

        TextView categoryTitle = findViewById(R.id.ThreadsList_TV_Category);

        // Sets the header to the current category
        categoryTitle.setText(AppData.cur_Category);

        //Takes all threadButtons and places them into threadButtonList
        initializeAndStoreThreadButtons();

        // Takes a DataSnapShot of the CurrentCategory and takes all of the ThreadUID's
        // places them into keyList
        grabKeysInCategory();

    }

    private void setOnClickOnButtons()
    {
        // Iterates through the ButtonList of activated threads and sets the Cur_thread key in
        // AppData accordingly and each button in list gets click listener
        for(int i = 0; i<activeThreadBtnList.size(); i++ )
        {
            final int finalI = i;
            activeThreadBtnList.get(i).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // This is a current work around as the newest thread keys are located
                    // at the end of the List of Keys
                    // The first Button is assigned with the Last Thread Key in the list
                    AppData.cur_Thread_Key = keyList.get((keyList.size()-1)-finalI);

                    Intent launchActivity1 =
                            new Intent(CKD.Android.ThreadsList.this,Thread.class);
                    startActivity(launchActivity1);
                }
            });
        }

        Button nextPage = findViewById(R.id.ThreadsList_Btn_Next);
        Button newThread = findViewById(R.id.ThreadsList_Btn_NewThread);

        nextPage.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                //TODO Grab next 10 Threads
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
        // References the Node based on the users current Category
        DatabaseReference Category_node = db.getReference("Data")
                                            .child("Social")
                                            .child(AppData.cur_Category);

        Category_node.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                // goes through and adds all of the thread UID's from the database and adds them
                //to a list
                for(DataSnapshot d : dataSnapshot.getChildren())
                {
                    keyList.add(d.getKey());
                }
                // If no Threads have been created change the first button accordingly and return
                if(keyList.size() == 0)
                {
                    allThreadBtnList.get(0).setText("There are no current threads in this topic!!!");
                    allThreadBtnList.get(0).setBackgroundColor(Color.GRAY);
                    //Keeps one thread active
                    activeThreadBtnList.add(allThreadBtnList.get(0));
                    reOrderLayout();
                    return;
                }

                // Used to keep track which threads were NOT activated to remove from linear layout
                int threadlistButtonIndex = 0;

                // Starts at the end of List containing Thread Keys and the beginning of list containing Buttons
                // Once the entire List of Thread Keys is looked through OR 10 Thread Keys have been accessed
                // the foor loop breaks.
                for(int i = keyList.size()-1; i>=0 && threadlistButtonIndex < 10; i--, threadlistButtonIndex++ )
                {
                    activateThreads(allThreadBtnList.get(threadlistButtonIndex), keyList.get(i), dataSnapshot);
                }
                // Separates the activated Buttons from unactivated buttons (Instance of less than 10 threads in database
                // for the given category
                activeThreadBtnList = allThreadBtnList.subList(0,threadlistButtonIndex);
                reOrderLayout();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

    }

    // Goes through the list of Activated Buttons and ALL the buttons
    // If there is a button in the allThreadBtnList that is not in
    // activeThreadBtnList. it is removed from the linear Layout.
    // If there are atleast 10 Threads in Category this will do nothing
    // TODO Add a check to avoid doing this when there are 10 active threads
    private void reOrderLayout()
    {
        LinearLayout ll = findViewById(R.id.ThreadsList_Layout);

        for(Button b : allThreadBtnList)
        {
            if(activeThreadBtnList.contains(b))
                continue;
            else
                ll.removeView(b);
        }

        //Sets clicker on all buttons
        setOnClickOnButtons();
    }


    private void initializeAndStoreThreadButtons()
    {
        Button thread1 = findViewById(R.id.ThreadsList_Btn_Thread1);
        allThreadBtnList.add(thread1);

        Button thread2 = findViewById(R.id.ThreadsList_Btn_Thread2);
        allThreadBtnList.add(thread2);

        Button thread3 = findViewById(R.id.ThreadsList_Btn_Thread3);
        allThreadBtnList.add(thread3);

        Button thread4 = findViewById(R.id.ThreadsList_Btn_Thread4);
        allThreadBtnList.add(thread4);

        Button thread5 = findViewById(R.id.ThreadsList_Btn_Thread5);
        allThreadBtnList.add(thread5);

        Button thread6 = findViewById(R.id.ThreadsList_Btn_Thread6);
        allThreadBtnList.add(thread6);

        Button thread7 = findViewById(R.id.ThreadsList_Btn_Thread7);
        allThreadBtnList.add(thread7);

        Button thread8 = findViewById(R.id.ThreadsList_Btn_Thread8);
        allThreadBtnList.add(thread8);

        Button thread9 = findViewById(R.id.ThreadsList_Btn_Thread9);
        allThreadBtnList.add(thread9);

        Button thread10 = findViewById(R.id.ThreadsList_Btn_Thread10);
        allThreadBtnList.add(thread10);
    }


    // Each button is initially invisible, unclickable, with no text
    // Once confirmed there is a thread, the title is grabbed from the snapshot
    // no longer invisible and becomes clickable. Unactivated buttons/threads will
    // remain unclickable and said characteristic will be used for re-organization of the page
    private void activateThreads(Button thread, String threadKey, DataSnapshot dataSnapshot)
    {
        String title = (String) dataSnapshot.child(threadKey).child("title").getValue();
        thread.setText(title);
        thread.setBackgroundColor(Color.LTGRAY);
    }
}
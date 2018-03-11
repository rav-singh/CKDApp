package CKD.Android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
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
import java.util.Collections;
import java.util.List;


public class ThreadsList extends AppCompatActivity
{
    final List<String> keyList= new ArrayList<>();
    final List<Button> allThreadBtnList= new ArrayList<>();
    List<Button> activeThreadBtnList = new ArrayList<>();
    DataSnapshot DS;
    int currentPage = 1;
    int maxPages;

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

    private void enableNextButton()
    {
        Button nextPage = findViewById(R.id.ThreadsList_Btn_Next);
        nextPage.setClickable(true);
        nextPage.setBackground(this.getResources().getDrawable(R.drawable.button_background));;
    }

    private void enablePrevButton()
    {
        Button prevPage = findViewById(R.id.ThreadsList_Btn_Prev);
        prevPage.setClickable(true);
        prevPage.setBackground(this.getResources().getDrawable(R.drawable.button_background));
    }

    private void disableNextButton()
    {
        Button nextPage = findViewById(R.id.ThreadsList_Btn_Next);
        nextPage.setClickable(false);
        nextPage.setBackground(this.getResources().getDrawable(R.drawable.disabled_button_background));
    }

    private void disablePrevButton()
    {
        Button prevPage = findViewById(R.id.ThreadsList_Btn_Prev);
        prevPage.setClickable(false);
        prevPage.setBackground(this.getResources().getDrawable(R.drawable.disabled_button_background));
    }

    //TODO Fix bug where Thread titles in buttons are centered and not left-aligned
    private void loadPreviousThreads(int pageNum)
    {
        int lowBound = (pageNum - 1) * 10;
        int numThreads = 0;

        // This references the LinearList INSIDE of the scrollview therefore does
        // Not require removing buttons
        LinearLayout ll = findViewById(R.id.ThreadsList_Layout);

        // Used for instance when the previous page of threads the user was viewing
        // has less than 10 threads to view. This checks if Buttons were removed.
        for(Button B : allThreadBtnList)
        {
            if(B.getParent() == null)
                ll.addView(B);
        }

        for(int i = lowBound; i < keyList.size() && numThreads < 10; i++, numThreads++)
        {
            activateThreads(allThreadBtnList.get(numThreads), keyList.get(i), DS);
        }

        activeThreadBtnList = allThreadBtnList.subList(0, numThreads);
        reOrderLayout();
    }

    private void loadNextThreads(int pageNum)
    {
        int lowBound = (pageNum - 1) * 10;
        int numThreads = 0;

        for(int i = lowBound; i < keyList.size() && numThreads < 10; i++, numThreads++)
        {
            activateThreads(allThreadBtnList.get(numThreads), keyList.get(i), DS);
        }
        // Separates the activated Buttons from unactivated buttons (Instance of less than 10 threads in database
        // for the given category
        activeThreadBtnList = allThreadBtnList.subList(0, numThreads);
        reOrderLayout();
    }

    private void setOnClickOnButtons()
    {
        // Iterates through the ButtonList of activated threads and sets the Cur_thread key in
        // AppData accordingly and each button in list gets click listener

        final int lowBound = (currentPage-1) *10;
        for(int i = 0; i<activeThreadBtnList.size(); i++ )
        {
            final int finalI = i;

            activeThreadBtnList.get(i).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AppData.cur_Thread_Key = keyList.get(lowBound+ finalI);

                    Intent launchActivity1 =
                            new Intent(CKD.Android.ThreadsList.this,Thread.class);
                    startActivity(launchActivity1);
                }
            });
        }

        Button nextPage = findViewById(R.id.ThreadsList_Btn_Next);
        Button prevPage = findViewById(R.id.ThreadsList_Btn_Prev);
        Button newThread = findViewById(R.id.ThreadsList_Btn_NewThread);

        nextPage.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                enablePrevButton();
                loadNextThreads(++currentPage);
                if(currentPage == maxPages)
                {
                    disableNextButton();

                }
            }

        });

        prevPage.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                enableNextButton();
                loadPreviousThreads(--currentPage);
                if(currentPage == 1)
                {
                    disablePrevButton();
                }
            }

        });

        if(currentPage == 1)
            disablePrevButton();

        if(maxPages == 1)
            disableNextButton();

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
                DS = dataSnapshot;
                // goes through and adds all of the thread UID's from the database and adds them
                //to a list
                for(DataSnapshot d : DS.getChildren())
                {
                    keyList.add(d.getKey());
                }
                // Calculates the maximum number of pages the user can navigate through given
                // 10 threads can be viewed at a time. Used to determine when to disable/enable
                // navigation buttons
                maxPages = (keyList.size() / 10) + 1;

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
                int numThreads = 0;

                // The thread keys grabbed from database are placed from oldest to newest order.
                // We want to Have newest posts at the beginning. So order is reversed to have
                Collections.reverse(keyList);

                for(int i = 0; i < keyList.size() && numThreads < 10; i++, numThreads++ )
                {
                    activateThreads(allThreadBtnList.get(numThreads), keyList.get(i), dataSnapshot);
                }
                // Separates the activated Buttons from unactivated buttons
                // (Instance of <10 threads available to fill all buttons on page)
                activeThreadBtnList = allThreadBtnList.subList(0,numThreads);

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
    // activeThreadBtnList. It is removed from the linear Layout.
    // If there are atleast 10 Threads in the Category this will do nothing
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

    // Takes uses the datasnapshot to get the title for a specific thread
    private void activateThreads(Button thread, String threadKey, DataSnapshot dataSnapshot)
    {
        String title = (String) dataSnapshot.child(threadKey).child("title").getValue();
        thread.setText(title);
        thread.setGravity(Gravity.LEFT|Gravity.CENTER);
        thread.setBackgroundColor(Color.LTGRAY);
    }
}
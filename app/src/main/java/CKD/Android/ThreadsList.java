package CKD.Android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ThreadsList extends AppCompatActivity
{
    final List<String> keyList= new ArrayList<>();
    List<Button> activeThreadBtnList= new ArrayList<>();
    final List<ThreadClass> threadsList= new ArrayList<>();

    final Map<Integer,List<ThreadClass>> threadsMap = new HashMap<>();
    final Map<Integer,List<String>> threadsKeyMap = new HashMap<>();
    final int threadsPerPage = 10;

    LinearLayout listOfThreads;

    DataSnapshot DS;
    int currentPage;
    int maxPages;
    Boolean dailyCheckListUpdated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_threads);

        listOfThreads = findViewById(R.id.ThreadsList_Layout);

        currentPage=1;

        initializeButtons();

        TextView categoryTitle = findViewById(R.id.ThreadsList_TV_Category);

        // Sets the header to the current category
        categoryTitle.setText(AppData.cur_Category);

        grabKeysInCategory();

    }

    private void initializeButtons()
    {
        Button home_btn = findViewById(R.id.Threads_btn_home);
        home_btn = AppData.activateHomeButton(home_btn,ThreadsList.this);

        Button newThread = findViewById(R.id.ThreadsList_Btn_NewThread);
        newThread.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent launchActivity1 =
                        new Intent(CKD.Android.ThreadsList.this,NewThread.class);
                startActivity(launchActivity1);
            }
        });

        Button nextPage = findViewById(R.id.ThreadsList_Btn_Next);

        nextPage.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                enablePrevButton();
                activeThreadBtnList.clear();
                currentPage++;
                loadNextThreads();

                if(currentPage == maxPages)
                {
                    disableNextButton();
                }
            }

        });
        Button prevPage = findViewById(R.id.ThreadsList_Btn_Prev);

        prevPage.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                enableNextButton();
                currentPage--;
                activeThreadBtnList.clear();
                loadPreviousThreads();

                if(currentPage == 1)
                {
                    disablePrevButton();
                }
            }

        });
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

    private void loadPreviousThreads()
    {
        listOfThreads.removeAllViews();
        updateUI();
    }

    private void loadNextThreads()
    {
        listOfThreads.removeAllViews();
        updateUI();
    }

    private void setOnClickOnButtons()
    {
        // Iterates through the ButtonList of activated threads and sets the Cur_thread key in
        // AppData accordingly and each button in list gets click listener
        for(int i = 0; i < activeThreadBtnList.size(); i++ )
        {
            final int finalI = i;

            activeThreadBtnList.get(i).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    //Returns a list of Strings to reference the corresponding threads
                    //in the databse
                    String threadKey = threadsKeyMap.get(currentPage).get(finalI);
                    AppData.cur_Thread_Key = threadKey;

                    // This avoids the database being written to every time the
                    // user opens this page. Once the user closes the app the next
                    // next time this page is opened the
                    if(!dailyCheckListUpdated)
                    {
                        AppData.updateDailyChecklist("Social");
                        dailyCheckListUpdated = true;
                    }
                    Intent launchActivity1 =
                            new Intent(CKD.Android.ThreadsList.this,Thread.class);
                    startActivity(launchActivity1);
                }
            });
        }

        if(currentPage == 1)
            disablePrevButton();

        if(maxPages == 1)
            disableNextButton();

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
                for(DataSnapshot d : dataSnapshot.getChildren())
                {
                    ThreadClass thread = d.getValue(ThreadClass.class);
                    threadsList.add(thread);
                    keyList.add(d.getKey());
                }

                int numThreads = keyList.size();

                //This accounts for when there are a maximum number of threads on a page
                if(numThreads%threadsPerPage == 0)
                    maxPages = numThreads/threadsPerPage;
                else
                    maxPages = (numThreads / threadsPerPage) + 1;

                // If no Threads have been created change the first button accordingly and return
                if(threadsList.size() == 0)
                {
                    noThreadsHere();
                    return;
                }

                Collections.reverse(keyList);
                Collections.reverse(threadsList);

                sortPagesIntoMap();
                sortKeysIntoMap();

                updateUI();

            }

            private void sortPagesIntoMap()
            {
                List<ThreadClass> pageOfThreads;

                for(int pagenum = 1; pagenum <= maxPages; pagenum++)
                {
                    if(pagenum == maxPages)
                    {
                        pageOfThreads = threadsList.subList((pagenum-1)*threadsPerPage,threadsList.size());
                    }
                    else
                    {
                        pageOfThreads = threadsList.subList((pagenum-1)*threadsPerPage, (pagenum-1) + threadsPerPage);
                    }

                    threadsMap.put(pagenum,pageOfThreads);
                }


            }

            private void sortKeysIntoMap()
            {
                List<String> threadKey;

                for (int pagenum = 1; pagenum <= maxPages; pagenum++)
                {
                    if (pagenum == maxPages)
                    {
                        threadKey = keyList.subList((pagenum-1) * threadsPerPage, threadsList.size());

                    } else
                    {
                        threadKey = keyList.subList((pagenum-1) * threadsPerPage, (pagenum-1) + threadsPerPage);
                    }

                    threadsKeyMap.put(pagenum , threadKey);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

    }

    private void noThreadsHere()
    {
        Button empty = new Button(this);

        empty.setClickable(false);

        empty.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT, 1f));

        empty.setText("There are currently no posts in this topic!");

        empty.setBackground(this.getResources().getDrawable(R.drawable.rounded_corner_textview));

        listOfThreads.addView(empty);
    }

    private void updateUI ()
    {
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        List<ThreadClass> pageOfThreads = threadsMap.get(currentPage);

        //Indicates which threads have already been loaded to avoid referencing that thread again
        int viewCount = listOfThreads.getChildCount();

        for(int i = viewCount;  i < pageOfThreads.size() ; i++)
        {
            ThreadClass currentThread = pageOfThreads.get(i);

            LinearLayout threadView = new LinearLayout(this);
            threadView.setLayoutParams(linearParams);
            threadView.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout likesView = new LinearLayout(this);
            likesView.setLayoutParams(linearParams);
            likesView.setOrientation(LinearLayout.VERTICAL);

            ImageButton arrow = new ImageButton(this);

            arrow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT, 1f));

            arrow.setImageDrawable(this.getResources().getDrawable(android.R.drawable.arrow_up_float));
            arrow.setBackgroundResource(0);

            arrow = setOnClickUpVote(arrow,keyList.get(i));

            likesView.addView(arrow);

            TextView likes = new TextView(this);
            likes.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            likes.setText(String.valueOf(currentThread.getLikes()));

            likesView.addView(likes);
            threadView.addView(likesView);

            Button thread = new Button(this);
            thread.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            thread.setText(currentThread.getTitle());
            thread.setBackground(this.getResources().getDrawable(R.drawable.rounded_corner_textview));

            threadView.addView(thread);
            activeThreadBtnList.add(thread);


            listOfThreads.addView(threadView);
        }

            setOnClickOnButtons();

    }

    private ImageButton setOnClickUpVote(final ImageButton arrow, final String threadKey)
    {
        arrow.setOnClickListener( new View.OnClickListener()
        {
            public void onClick(View v)
            {
                //Toggles the selected state of the button
                arrow.setSelected(!arrow.isSelected());
                final boolean isSelected = arrow.isSelected();

                FirebaseDatabase db = FirebaseDatabase.getInstance();
                final DatabaseReference Likes_Node = db.getReference("Data")
                                                        .child("Social")
                                                        .child(AppData.cur_Category)
                                                        .child(threadKey)
                                                        .child("likes");

                Likes_Node.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        Integer count = dataSnapshot.getValue(Integer.class);
                        if(isSelected)
                        {
                            Likes_Node.setValue(++count);
                            arrow.setBackgroundColor(Color.GREEN);
                        }
                        else
                        {
                            Likes_Node.setValue(--count);
                            arrow.setBackgroundResource(0);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                });
            }
        });

        return arrow;
    }

}

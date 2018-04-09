package CKD.Android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ThreadsList extends AppCompatActivity
{
    List<String> keyList= new ArrayList<>();
    List<Button> activeThreadBtnList= new ArrayList<>();
    List<TextView> textViewsList = new ArrayList<>();
    List<ThreadClass> threadsList= new ArrayList<>();


    Map<Integer,List<ThreadClass>> threadsMap = new HashMap<>();
    Map<Integer,List<String>> threadsKeyMap = new HashMap<>();
    int threadsPerPage;

    Spinner spinner;

    LinearLayout listOfThreads;

    int currentPage;
    int maxPages;
    Boolean dailyCheckListUpdated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_threads);

        listOfThreads = findViewById(R.id.ThreadsList_Layout);

        spinner = findViewById(R.id.ThreadsList_SPN_ThreadsPerPage);

        currentPage=1;
        threadsPerPage = 10;

        initializeButtons();

        TextView categoryTitle = findViewById(R.id.ThreadsList_TV_Category);

        // Sets the header to the current category
        categoryTitle.setText(AppData.cur_Category);

        grabKeysInCategory();

        initializeSpinner();
    }

    private void initializeSpinner()
    {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View view, int position, long id)
            {
               int num = Integer.parseInt(spinner.getSelectedItem().toString());

                // They selected the same number so nothing needs to be done
               if(num == threadsPerPage)
               {
                   return;
               }
                //Reset all the mappings and data structures for new mappings based on threads
                //Per Page the user selected
               listOfThreads.removeAllViews();
               threadsKeyMap.clear();
               threadsMap.clear();
               activeThreadBtnList.clear();
               textViewsList.clear();

               // Update variable to users selection and set user back to page 1
                threadsPerPage = num;
                currentPage = 1;


                int numThreads = keyList.size();
                //This accounts for when there are a maximum number of threads on a page
                if(numThreads%threadsPerPage == 0)
                    maxPages = numThreads/threadsPerPage;
                else
                    maxPages = (numThreads / threadsPerPage) + 1;

                resortPagesIntoMap();
                resortKeysIntoMap();

                updateUI();
            }

            private void resortKeysIntoMap()
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

            private void resortPagesIntoMap()
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

            public void onNothingSelected(AdapterView<?> arg0) { }
        });
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
        textViewsList.clear();
        updateUI();
    }

    private void loadNextThreads()
    {
        listOfThreads.removeAllViews();
        textViewsList.clear();
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

        if(maxPages == 1 || maxPages == currentPage)
            disableNextButton();

        if(currentPage < maxPages)
            enableNextButton();



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
        List<ThreadClass> pageOfThreads = threadsMap.get(currentPage);
        List<String> pageOfKeys = threadsKeyMap.get(currentPage);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = (int) (dm.widthPixels*.75);

        LinearLayout.LayoutParams likesViewParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams outerParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams threadParams = new LinearLayout.LayoutParams
                (width, LinearLayout.LayoutParams.WRAP_CONTENT);


        //Indicates which threads have already been loaded to avoid referencing that thread again
        int viewCount = listOfThreads.getChildCount();

        for(int i = viewCount;  i < pageOfThreads.size() ; i++)
        {
            ThreadClass currentThread = pageOfThreads.get(i);

            LinearLayout threadView = new LinearLayout(this);
            threadView.setLayoutParams(outerParams);
            threadView.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout likesView = new LinearLayout(this);
            likesView.setLayoutParams(likesViewParams);
            likesView.setOrientation(LinearLayout.VERTICAL);

            ImageButton arrow = new ImageButton(this);

            arrow.setLayoutParams(likesViewParams);

            arrow.setImageDrawable(this.getResources().getDrawable(android.R.drawable.arrow_up_float));
            arrow.setBackgroundResource(0);
            arrow = setOnClickUpVote(arrow,pageOfKeys.get(i));

            likesView.addView(arrow);

            TextView likes = new TextView(this);
            likes.setLayoutParams(likesViewParams);
            likes.setText(String.valueOf(currentThread.getLikes()));
            likes.setTextSize(20);

            likesView.addView(likes);

            textViewsList.add(likes);

            threadView.addView(likesView);

            Button thread = new Button(this);
            thread.setLayoutParams(threadParams);
            thread.setText(currentThread.getTitle());
            thread.setGravity(Gravity.LEFT);
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
                            arrow.setColorFilter(Color.GREEN);
                        }
                        else
                        {
                            Likes_Node.setValue(--count);
                            arrow.setColorFilter(Color.WHITE);
                        }
                        upDateUpVoteCounter(count);
                    }

                    private void upDateUpVoteCounter(int count)
                    {
                        int indexOfThread = threadsKeyMap.get(currentPage).indexOf(threadKey);
                        textViewsList.get(indexOfThread).setText(String.valueOf(count));
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

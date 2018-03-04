package CKD.Android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
    final List<String> keyList = new ArrayList<>();
    final List<TextView> cmntList = new ArrayList<>();
    final List<TextView> authList = new ArrayList<>();
    List<TextView> allViewsList = new ArrayList<>();
    List<TextView> activeCommentList = new ArrayList<>();
    List<TextView> activeCommentAuthList = new ArrayList<>();
    DataSnapshot DS;
    int currentPage = 1;
    int maxPages;
    //TODO Fix the XML file so that longer threads do not cause the navigation
    //TODO buttons to go off screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO Make only comments scrollable

        setContentView(R.layout.activity_thread);

        getCurrentThread();
        getComments();

        setOnClickListeners();

        if(currentPage == 1)
            disablePrevButton();

        if(maxPages == 1)
            disableNextButton();

    }

    private void setOnClickListeners()
    {
        Button comment_btn = findViewById(R.id.Thread_Btn_Comment);
        Button nextButton = findViewById(R.id.Thread_Btn_Next);
        Button prevButton = findViewById(R.id.Thread_Btn_Prev);

        comment_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent launchActivity1=
                        new Intent(CKD.Android.Thread.this,Comment.class);
                startActivity(launchActivity1);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                enablePrevButton();
                loadNextComments(++currentPage);
                if(currentPage == maxPages)
                {
                    disableNextButton();
                }
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                enableNextButton();
                loadPrevComments(--currentPage);
                if(currentPage == 1)
                {
                    disablePrevButton();
                }
            }
        });
    }

    private void disablePrevButton()
    {
        Button prevPage = findViewById(R.id.Thread_Btn_Prev);
        prevPage.setClickable(false);
        //prevPage.setBackground(this.getResources().getDrawable(R.drawable.disabled_button_background));
    }

    private void enableNextButton()
    {
        Button nextPage = findViewById(R.id.Thread_Btn_Next);
        nextPage.setClickable(true);
        //nextPage.setBackground(this.getResources().getDrawable(R.drawable.button_background));
    }

    private void disableNextButton()
    {
        Button nextPage = findViewById(R.id.Thread_Btn_Next);
        nextPage.setClickable(false);
        //nextPage.setBackground(this.getResources().getDrawable(R.drawable.disabled_button_background));
    }

    private void enablePrevButton()
    {
        Button prevPage = findViewById(R.id.Thread_Btn_Prev);
        prevPage.setClickable(true);
     //   prevPage.setBackground(this.getResources().getDrawable(R.drawable.button_background));
    }

    private void loadNextComments(int pageNum)
    {
        int lowBound = (pageNum - 1) * 10;
        int numComments = 0;

        for(int i = lowBound; i < keyList.size() && numComments < 10; i++, numComments++)
        {
            fillInComments(cmntList.get(numComments), authList.get(numComments), i, DS);
        }

        activeCommentList = cmntList.subList(0, numComments);
        activeCommentAuthList = authList.subList(0, numComments);
        reOrderLayout();
    }

    private void loadPrevComments(int pageNum)
    {
        int lowBound = (pageNum - 1) * 10;
        int numComments = 0;
        LinearLayout ll = findViewById(R.id.Thread_Layout);

        //This combines all the view references on the page into 1 list
        //that will be used to add into the Linear layout
        for(int i = 0; i < 10; i++)
        {
            allViewsList.add(cmntList.get(i));
            allViewsList.add(authList.get(i));
        }

        // This checks each Textview to make sure it is not already in the Linear Layout
        for(TextView TV : allViewsList)
        {
            if(TV.getParent() == null)
                ll.addView(TV);
        }


        for(int i = lowBound; i < keyList.size() && numComments < 10; i++, numComments++)
        {
            fillInComments(cmntList.get(i), authList.get(i), i, DS);
        }

    }

    private void getComments()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference Comments_node = db.getReference("Data")
                .child("Social")
                .child(AppData.cur_Category)
                .child(AppData.cur_Thread_Key)
                .child("Comments");

        Comments_node.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                int numComments = 0;
                DS = dataSnapshot;
                for(DataSnapshot d : DS.getChildren())
                {
                    keyList.add(d.getKey());
                }

                maxPages = (keyList.size() / 10) + 1;

                if(maxPages == 1)
                    disableNextButton();

                fillLists();

                //If there are no comments on this thread
                if (keyList.size() == 0)
                {
                    cmntList.get(0).setText("There are no comments on this thread");
                    authList.get(0).setText("");
                    numComments++;
                }

                for(int i = 0; i < keyList.size() && numComments < 10; i++, numComments++)
                {
                    fillInComments(cmntList.get(i), authList.get(i), i, dataSnapshot);
                }

                activeCommentList = cmntList.subList(0, numComments);
                activeCommentAuthList = authList.subList(0, numComments);
                reOrderLayout();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    private void reOrderLayout ()
    {
        LinearLayout ll = findViewById(R.id.Thread_Layout);

        for(TextView TV : cmntList)
        {
            if(activeCommentList.contains(TV))
                continue;
            else
                ll.removeView(TV);
        }

        for(TextView TV: authList)
        {
            if(activeCommentAuthList.contains(TV))
                continue;
            else
                ll.removeView(TV);
        }


    }

    private void fillLists ()
    {

        TextView Comment1 = findViewById(R.id.Thread_TV_Comment1);
        TextView Comment1Auth = findViewById(R.id.Thread_TV_Cmnt1_author);
        cmntList.add(Comment1);
        authList.add(Comment1Auth);

        TextView Comment2 = findViewById(R.id.Thread_TV_Comment2);
        TextView Comment2Auth = findViewById(R.id.Thread_TV_Cmnt2_author);
        cmntList.add(Comment2);
        authList.add(Comment2Auth);

        TextView Comment3 = findViewById(R.id.Thread_TV_Comment3);
        TextView Comment3Auth = findViewById(R.id.Thread_TV_Cmnt3_author);
        cmntList.add(Comment3);
        authList.add(Comment3Auth);

        TextView Comment4 = findViewById(R.id.Thread_TV_Comment4);
        TextView Comment4Auth = findViewById(R.id.Thread_TV_Cmnt4_author);
        cmntList.add(Comment4);
        authList.add(Comment4Auth);

        TextView Comment5 = findViewById(R.id.Thread_TV_Comment5);
        TextView Comment5Auth = findViewById(R.id.Thread_TV_Cmnt5_author);
        cmntList.add(Comment5);
        authList.add(Comment5Auth);

        TextView Comment6 = findViewById(R.id.Thread_TV_Comment6);
        TextView Comment6Auth = findViewById(R.id.Thread_TV_Cmnt6_author);
        cmntList.add(Comment6);
        authList.add(Comment6Auth);

        TextView Comment7 = findViewById(R.id.Thread_TV_Comment7);
        TextView Comment7Auth = findViewById(R.id.Thread_TV_Cmnt7_author);
        cmntList.add(Comment7);
        authList.add(Comment7Auth);

        TextView Comment8 = findViewById(R.id.Thread_TV_Comment8);
        TextView Comment8Auth = findViewById(R.id.Thread_TV_Cmnt8_author);
        cmntList.add(Comment8);
        authList.add(Comment8Auth);

        TextView Comment9 = findViewById(R.id.Thread_TV_Comment9);
        TextView Comment9Auth = findViewById(R.id.Thread_TV_Cmnt9_author);
        cmntList.add(Comment9);
        authList.add(Comment9Auth);

        TextView Comment10 = findViewById(R.id.Thread_TV_Comment10);
        TextView Comment10Auth = findViewById(R.id.Thread_TV_Cmnt10_author);
        cmntList.add(Comment10);
        authList.add(Comment10Auth);
    }

    private void fillInComments(TextView comment1, TextView comment1Auth, int i, DataSnapshot dataSnapshot)
    {
        comment1.setText((String)dataSnapshot.child(keyList.get(i)).child("comment").getValue());
        comment1Auth.setText((String)dataSnapshot.child(keyList.get(i)).child("userName").getValue());
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

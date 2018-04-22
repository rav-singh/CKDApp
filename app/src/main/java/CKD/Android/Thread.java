package CKD.Android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Thread extends AppCompatActivity
{
    final List<String> keyList = new ArrayList<>();
    List<CommentClass> commentClassList = new ArrayList<>();

    DataSnapshot DS;
    LinearLayout LLinScrollView;

    final float[] topCorners = new float[]{20.0f, 20.0f, 20.0f, 20.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    final float[] bottomCorners = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 20.0f, 20.0f, 20.0f, 20.0f};
    final float[] notBottomLeftCorner = new float[]{20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 20.0f, 0.0f, 0.0f};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_thread);

        LLinScrollView  = findViewById(R.id.Thread_LL_Comments);

        Button home_btn = findViewById(R.id.Thread_btn_home);
        home_btn = AppData.activateHomeButton(home_btn,Thread.this);

        // The if statements prevents the counter falsely incrementing the counter
        // when the user is directed here from the Comment class.
        if(!AppData.userMakingComment)
        {
            AppData.userMakingComment = false;
        }
        else
        {
            AppData.updateParticipation("ThreadsViewed");
        }

        getCurrentThread();
        getComments();

        setOnClickListeners();
    }

    private void setOnClickListeners()
    {
        Button comment_btn = findViewById(R.id.Thread_Btn_Comment);

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
        DatabaseReference Comments_node = db.getReference("Data")
                .child("Social")
                .child(AppData.cur_Category)
                .child(AppData.cur_Thread_Key)
                .child("Comments");

        Comments_node.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                DS = dataSnapshot;
                for(DataSnapshot d : DS.getChildren())
                {
                    keyList.add(d.getKey());

                    commentClassList.add(d.getValue(CommentClass.class));
                }


                upDateUI();

            }


            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    private void upDateUI()
    {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = (int) (dm.widthPixels*.90);

      //  LLinScrollView.removeAllViews();

        LinearLayout.LayoutParams commentLLParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        commentLLParams.setMargins(0,0,0,10);

        LinearLayout.LayoutParams commentParams = new LinearLayout.LayoutParams
                (width, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams authParams = new LinearLayout.LayoutParams
                (width, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout commentListLL = new LinearLayout(this);
        commentListLL.setOrientation(LinearLayout.VERTICAL);

        commentListLL.setLayoutParams(commentLLParams);

        GradientDrawable top = new GradientDrawable();
        GradientDrawable bottom = new GradientDrawable();

        top.setCornerRadii(topCorners);
        bottom.setCornerRadii(bottomCorners);
        top.setColor(Color.WHITE);
        bottom.setColor(Color.WHITE);;

        for(CommentClass cmt : commentClassList)
        {
            LinearLayout commentLL = new LinearLayout(this);
            commentLL.setOrientation(LinearLayout.VERTICAL);
            commentLL.setLayoutParams(commentLLParams);

            TextView body = new TextView(this);

            body.setLayoutParams(commentParams);
            body.setTextSize(getResources().getInteger(R.integer.java_text_size_small));
            body.setTextColor(Color.BLACK);
            body.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            body.setText(cmt.getComment());
            body.setBackground(top);

            commentLL.addView(body);

            TextView author = new TextView(this);

            author.setLayoutParams(authParams);
            author.setTextSize(getResources().getInteger(R.integer.java_text_size_xsmall));
            author.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            author.setText("By: ".concat(cmt.getUserName()));

            author.setBackground(bottom);

            commentLL.addView(author);

            commentListLL.addView(commentLL);
        }
        LLinScrollView.addView(commentListLL);
    }

    private void getCurrentThread()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        DatabaseReference Thread_node = db.getReference("Data")
                                            .child("Social")
                                            .child(AppData.cur_Category)
                                            .child(AppData.cur_Thread_Key);

        Thread_node.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Use this thread to fill the values on the page
                ThreadClass thread = dataSnapshot.getValue(ThreadClass.class);
                fillInThread(thread);

                AppData.setCurrentThread(thread);
            }

            private void fillInThread(ThreadClass thread)
            {
                TextView threadTitle = findViewById(R.id.Thread_TV_ThreadTitle);
                TextView threadBody = findViewById(R.id.Thread_TV_ThreadBody);
                TextView threadAuthor = findViewById(R.id.Thread_TV_ThreadAuthor);

                GradientDrawable body = new GradientDrawable();
                GradientDrawable auth = new GradientDrawable();

                body.setCornerRadii(topCorners);
                body.setColor(Color.WHITE);

                auth.setCornerRadii(bottomCorners);
                auth.setColor(Color.WHITE);

                threadTitle.setText(thread.getTitle());

                threadBody.setText(thread.getBody());
                threadBody.setBackground(body);

                threadAuthor.setText("By: ".concat(thread.getAuthor()));
                threadAuthor.setBackground(auth);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("HELO", "Failed to read value.", error.toException());
            }
        });
    }

}

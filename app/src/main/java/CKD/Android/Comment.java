package CKD.Android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Comment extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_comment);

        final TextView theadTitle = findViewById(R.id.Comment_TV_ThreadTitle);
        final TextView threadBody = findViewById(R.id.Comment_TV_ThreadBody);
        final EditText comment = findViewById(R.id.Comment_TE_Comment);
        Button submit_btn = findViewById(R.id.Comment_Btn_Submit);

        threadBody.setMovementMethod(new ScrollingMovementMethod());

        threadBody.setText(AppData.cur_Thread.getBody());
        theadTitle.setText(AppData.cur_Thread.getTitle());

        submit_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String userUID = AppData.cur_user.getUID();
                String userName = AppData.cur_user.getName();
                String userComment = comment.getText().toString();
                String category = AppData.cur_Category;
                String date = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss",
                        Locale.getDefault()).format(new Date());


                FirebaseDatabase db;

                if(AppData.db == null)
                {
                    db = FirebaseDatabase.getInstance();
                    AppData.db = db;
                }
                else
                    db = AppData.db;

                //Creates/grabs
                DatabaseReference Comments_node =
                        db.getReference("Data").child("Social").child(category).child(AppData.cur_Thread_Key).child("Comments");

                String key = Comments_node.child("Comments").push().getKey();

                CommentClass newComment = new CommentClass(userName,userUID,userComment,date,AppData.cur_Thread_Key,key);

                Comments_node.child(key).setValue(newComment);


                Intent launchActivity1 =
                        new Intent(CKD.Android.Comment.this,Thread.class);
                startActivity(launchActivity1);
            }


        });

    }

}

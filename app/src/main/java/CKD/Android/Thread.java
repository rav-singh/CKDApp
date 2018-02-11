package CKD.Android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;


public class Thread extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_thread);

        TextView threadTitle = findViewById(R.id.Thread_TV_ThreadTitle);
        TextView threadBody= findViewById(R.id.Thread_TV_ThreadBody);
        TextView threadAuthor= findViewById(R.id.Thread_TV_ThreadAuthor);

        TextView comment1= findViewById(R.id.Thread_TV_Comment1);
        TextView cmnt1Author= findViewById(R.id.Thread_TV_Cmnt1_author);

        TextView comment2= findViewById(R.id.Thread_TV_Comment2);
        TextView cmnt2Author= findViewById(R.id.Thread_TV_Cmnt2_author);

        TextView comment3 = findViewById(R.id.Thread_TV_Comment3);
        TextView cmnt3Author= findViewById(R.id.Thread_TV_Cmnt3_author);

        TextView comment4= findViewById(R.id.Thread_TV_Comment4);
        TextView cmnt4Author= findViewById(R.id.Thread_TV_Cmnt4_author);

        TextView comment5= findViewById(R.id.Thread_TV_Comment5);
        TextView cmnt5Author= findViewById(R.id.Thread_TV_Cmnt5_author);



    }
}
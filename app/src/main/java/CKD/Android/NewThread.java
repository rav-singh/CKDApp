package CKD.Android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;


public class NewThread extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_new_thread);

        EditText threadTitle = findViewById(R.id.NewThread_TE_Title);
        EditText threadBody=  findViewById(R.id.NewThread_TE_Body);
        Button submit_btn = findViewById(R.id.NewThread_Btn_Submit);


        submit_btn.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                //TODO Place function that adds the new Thread to the database AND updates the ThreadlistPage
                Intent launchActivity1 =
                        new Intent(CKD.Android.NewThread.this,ThreadsList.class);
                startActivity(launchActivity1);
            }
        });

    }
}
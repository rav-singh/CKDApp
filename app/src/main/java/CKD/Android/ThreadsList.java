package CKD.Android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ThreadsList extends AppCompatActivity
{

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

        thread1.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent launchActivity1 =
                        new Intent(CKD.Android.ThreadsList.this,Thread.class);
                startActivity(launchActivity1);
            }
        });
        thread2.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent launchActivity1 =
                        new Intent(CKD.Android.ThreadsList.this,Thread.class);
                startActivity(launchActivity1);
            }
        });
        thread3.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent launchActivity1 =
                        new Intent(CKD.Android.ThreadsList.this,Thread.class);
                startActivity(launchActivity1);
            }
        });
        thread4.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
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
}
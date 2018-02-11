package CKD.Android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Social extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        TextView Title = findViewById(R.id.Social_TV_Title);
        Button gen_Disc_Cat = findViewById(R.id.Social_Btn_General);
        Button new_Mem_Cat = findViewById(R.id.Social_Btn_NewMembers);
        Button recipes_cat= findViewById(R.id.Social_Btn_Recipes);
        Button help_cat = findViewById(R.id.Social_Btn_Help);


        gen_Disc_Cat.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                AppData.cur_Category = "General Discussion";
                Intent launchActivity1 =
                        new Intent(CKD.Android.Social.this,ThreadsList.class);
                startActivity(launchActivity1);
            }
        });

        new_Mem_Cat.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                AppData.cur_Category = "New Members";
                Intent launchActivity1=
                        new Intent(CKD.Android.Social.this,ThreadsList.class);
                startActivity(launchActivity1);
            }
        });

        recipes_cat.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                AppData.cur_Category = "Recipes";
                Intent launchActivity1=
                        new Intent(CKD.Android.Social.this,ThreadsList.class);
                startActivity(launchActivity1);
            }
        });

        help_cat.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                AppData.cur_Category = "HELP";
                Intent launchActivity1=
                        new Intent(CKD.Android.Social.this,ThreadsList.class);
                startActivity(launchActivity1);
            }
        });



    }
}
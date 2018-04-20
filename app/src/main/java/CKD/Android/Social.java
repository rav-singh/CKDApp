package CKD.Android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;


public class Social extends AppCompatActivity
{
    Map<Button,String> buttonMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        Button pPost = findViewById(R.id.Social_Btn_PinnedPost);

        pPost.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent launchActivity1=
                        new Intent(CKD.Android.Social.this,PinnedPost.class);
                startActivity(launchActivity1);
            }
        });

        initializeButtons();
        for(Button b : buttonMap.keySet())
            setOnClickListeners(b);

        Button home_btn = findViewById(R.id.Social_Btn_Home);
        home_btn = AppData.activateHomeButton(home_btn,Social.this);
    }

    private void setOnClickListeners(final Button b)
    {
        b.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String category = buttonMap.get(b);
                AppData.cur_Category = category;

                Intent launchActivity1=
                        new Intent(CKD.Android.Social.this,ThreadsList.class);
                startActivity(launchActivity1);
            }
        });
    }

    private void initializeButtons()
    {
        Button wellness = findViewById(R.id.Social_Btn_Wellness);
        buttonMap.put(wellness,"Wellness");

        Button recipes= findViewById(R.id.Social_Btn_Recipes);
        buttonMap.put(recipes,"Recipies");

        Button activities = findViewById(R.id.Social_Btn_Activities);
        buttonMap.put(activities,"Activies");

        Button general = findViewById(R.id.Social_Btn_General);
        buttonMap.put(general,"General");
    }
}
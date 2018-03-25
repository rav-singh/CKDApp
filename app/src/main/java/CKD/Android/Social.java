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

        initializeButtons();

        for(Button b : buttonMap.keySet())
            setOnClickListeners(b);
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
        Button general = findViewById(R.id.Social_Btn_General);
        buttonMap.put(general,"General");
        Button _new = findViewById(R.id.Social_Btn_NewMembers);
        buttonMap.put(_new,"New Members");
        Button recipies= findViewById(R.id.Social_Btn_Recipes);
        buttonMap.put(recipies,"Recipies");
        Button help = findViewById(R.id.Social_Btn_Help);
        buttonMap.put(help,"Help");
    }
}
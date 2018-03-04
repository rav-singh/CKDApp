package CKD.Android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Diet extends AppCompatActivity {

    private Button btnAddBreakfast;
    private Button btnAddLunch;
    private Button btnAddDinner;
    private Button btnAddSnacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        btnAddBreakfast = (Button) findViewById(R.id.addBreakfast_btn);
        btnAddLunch = (Button) findViewById(R.id.addLunch_btn);
        btnAddDinner = (Button) findViewById(R.id.addDinner_btn);
        btnAddSnacks = (Button) findViewById(R.id.addSnacks_btn);

        btnAddBreakfast.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent myIntent = new Intent(CKD.Android.Diet.this,SearchFood.class);
                startActivity(myIntent);
            }
        });

        btnAddLunch.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent launchActivity1=
                        new Intent(CKD.Android.Diet.this,SearchFood.class);
                startActivity(launchActivity1);
            }
        });

        btnAddDinner.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent launchActivity1=
                        new Intent(CKD.Android.Diet.this,SearchFood.class);
                startActivity(launchActivity1);
            }
        });

        btnAddSnacks.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                Intent launchActivity1=
                        new Intent(CKD.Android.Diet.this,SearchFood.class);
                startActivity(launchActivity1);
            }
        });

            }


}


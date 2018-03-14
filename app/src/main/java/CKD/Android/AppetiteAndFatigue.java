package CKD.Android;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AppetiteAndFatigue extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appetite);

       Button next_btn = findViewById(R.id.app_Btn_Next);
       final Spinner appetite = findViewById(R.id.app_SPN_appetite);
       final Spinner fatigue = findViewById(R.id.app_SPN_fatigue);

        next_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String fat = fatigue.getSelectedItem().toString();
                String app = appetite.getSelectedItem().toString();

                if (fat.contains("Select") || app.contains("Select"))
                {
                    Toast.makeText(AppetiteAndFatigue.this,
                            "Please select your Appetite and Mood from the dropdown",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                addAppAndFatToDB(app,fat);

                Intent launchActivity1= new Intent(
                        CKD.Android.AppetiteAndFatigue.this,HomePage.class);
                startActivity(launchActivity1);
            }

            private void addAppAndFatToDB(String app, String fat)
            {
                String date = new SimpleDateFormat("YYYY-MM-dd", Locale.getDefault()).format(new Date());
                String UID = AppData.cur_user.getUID();

                //Grabs references to the Root node and Users Node
                FirebaseDatabase db = FirebaseDatabase.getInstance();

                DatabaseReference Data_node = db.getReference("Data");
                DatabaseReference AppAndFat_node = Data_node.child("AppetiteAndFatigue");
                DatabaseReference UID_node = AppAndFat_node.child(UID);
                DatabaseReference Date_node = UID_node.child(date);

                UID_node.child(date).child("Appetite").setValue(app);
                UID_node.child(date).child("Fatigue").setValue(fat);
            }

        });

    }
}
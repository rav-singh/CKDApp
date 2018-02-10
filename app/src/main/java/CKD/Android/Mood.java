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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Mood extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);

        final List<String> selectedMoods = new ArrayList<>();

        // UI Components
        //TODO Add alternate images in resources file
        final ImageButton happy_btn = findViewById(R.id.Mood_ImBtn_Happy);
        final ImageButton depressed_btn = findViewById(R.id.Mood_ImBtn_Depressed);
        final ImageButton anxious_btn = findViewById(R.id.Mood_ImBtn_Anxious);
        final ImageButton fatigued_btn = findViewById(R.id.Mood_ImBtn_Fatigued);
        final ImageButton flat_btn = findViewById(R.id.Mood_ImBtn_Flat);
        final ImageButton nausea_btn = findViewById(R.id.Mood_ImBtn_Nausea);
        final Button next_btn = findViewById(R.id.Mood_Btn_Next);


        happy_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                //When the user Clicks on the image it either selects it or de-selects it
                //based on previous state
                happy_btn.setSelected(!happy_btn.isSelected());

                setBackgroundOnSelection(happy_btn);
               }
        });

        depressed_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                //When the user Clicks on the image it either selects it or de-selects it
                //based on previous state
                depressed_btn.setSelected(!depressed_btn.isSelected());
                setBackgroundOnSelection(depressed_btn);
            }
        });

        anxious_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                //When the user Clicks on the image it either selects it or de-selects it
                //based on previous state
                anxious_btn.setSelected(!anxious_btn.isSelected());
                setBackgroundOnSelection(anxious_btn);
            }
        });


        flat_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                //When the user Clicks on the image it either selects it or de-selects it
                //based on previous state
                flat_btn.setSelected(!flat_btn.isSelected());
                setBackgroundOnSelection(flat_btn);
            }
        });

        nausea_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                //When the user Clicks on the image it either selects it or de-selects it
                //based on previous state
                nausea_btn.setSelected(!nausea_btn.isSelected());
                setBackgroundOnSelection(nausea_btn);
            }
        });

        fatigued_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                //When the user Clicks on the image it either selects it or de-selects it
                //based on previous state
                fatigued_btn.setSelected(!fatigued_btn.isSelected());
                setBackgroundOnSelection(fatigued_btn);
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                checkForSelectedMoods();

                addDateAndMoodsToDB();

                Intent launchActivity1= new Intent(
                        CKD.Android.Mood.this,HomePage.class);
                startActivity(launchActivity1);

            }

            private void addDateAndMoodsToDB()
            {
                //TODO Need to make sure that the date still works appropriately when past the 10th
                String date = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String moods = selectedMoods.toString();

                //Grabs references to the Root node and Users Node
                FirebaseDatabase db = FirebaseDatabase.getInstance();

                // Creates a Mood Child Node under Data
                // Creates a UID Child Node under Mood

                DatabaseReference Data_node = db.getReference("Data");
                DatabaseReference Mood_node = Data_node.child("Mood");
                DatabaseReference UID_node = Mood_node.child(UID);

                UID_node.child(date).setValue(moods);
            }

            private void checkForSelectedMoods()
            {
                if(happy_btn.isSelected()) selectedMoods.add("Happy");
                if(anxious_btn.isSelected()) selectedMoods.add("Anxious");
                if(fatigued_btn.isSelected()) selectedMoods.add("Fatigued");
                if(depressed_btn.isSelected()) selectedMoods.add("Depressed");
                if(nausea_btn.isSelected()) selectedMoods.add("Nausea");
                if(flat_btn.isSelected()) selectedMoods.add("Flat");
            }
        });


    }


    private void setBackgroundOnSelection(ImageButton button)
    {
        if(button.isSelected())
            button.setBackgroundColor(Color.GREEN);
        else
            button.setBackgroundColor(Color.TRANSPARENT);
    }

}
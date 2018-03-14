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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Mood extends AppCompatActivity
{
    final Calendar cal = Calendar.getInstance();
    Map<ImageButton,String> imageButtonsMap  = new HashMap<>();

    final List<String> selectedMoods = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mood);

        final Button next_btn = findViewById(R.id.Mood_Btn_Next);

        initializeImageButtons();

        for(ImageButton ib : imageButtonsMap.keySet())
            setOnClickListeners(ib);

        next_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                for(ImageButton ib : imageButtonsMap.keySet())
                    checkForSelectedMoods(ib);

                addDateAndMoodsToDB();

                Intent launchActivity1= new Intent(
                        CKD.Android.Mood.this,AppetiteAndFatigue.class);
                startActivity(launchActivity1);

            }
        });
    }

    private void setOnClickListeners(final ImageButton ib)
    {
        ib.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //When the user Clicks on the image it either selects it or de-selects it
                //based on previous state
                ib.setSelected(!ib.isSelected());
                setBackgroundOnSelection(ib);
            }
        });
    }
        /*
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

    } */


    private void setBackgroundOnSelection(ImageButton button)
    {
        if(button.isSelected())
            button.setBackgroundColor(Color.GREEN);
        else
            button.setBackgroundColor(Color.TRANSPARENT);
    }

    private Date yesterday()
    {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    private void addDateAndMoodsToDB()
    {
        String date = new SimpleDateFormat("YYYY-MM-dd", Locale.getDefault()).format(new Date());
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

    // If the image Button is selected, it grabs the associated String
    // in the Map for Happy, Sad, Etc
    private void checkForSelectedMoods(ImageButton ib)
    {
        if(ib.isSelected())
            selectedMoods.add(imageButtonsMap.get(ib));
    }

    private void initializeImageButtons()
    {
        //TODO Add alternate images in resources file
        final ImageButton happy_btn = findViewById(R.id.Mood_ImBtn_Happy);
        imageButtonsMap.put(happy_btn,"Happy");
        final ImageButton depressed_btn = findViewById(R.id.Mood_ImBtn_Depressed);
        imageButtonsMap.put(depressed_btn,"Depressed");
        final ImageButton anxious_btn = findViewById(R.id.Mood_ImBtn_Anxious);
        imageButtonsMap.put(anxious_btn,"Anxious");
        final ImageButton fatigued_btn = findViewById(R.id.Mood_ImBtn_Fatigued);
        imageButtonsMap.put(fatigued_btn, "Fatigued");
        final ImageButton flat_btn = findViewById(R.id.Mood_ImBtn_Meh);
        imageButtonsMap.put(flat_btn,"Meh");
        final ImageButton nausea_btn = findViewById(R.id.Mood_ImBtn_Nausea);
        imageButtonsMap.put(nausea_btn,"Nauseous");
    }
}
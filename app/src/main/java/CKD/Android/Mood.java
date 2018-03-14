package CKD.Android;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Mood extends AppCompatActivity
{
    Map<ImageButton,String> imageButtonsMap  = new HashMap<>();
    Boolean userRecordedYesterday = false;

    List<String> selectedMoods = new ArrayList<>();
    List<String> datesSubmitted = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);

        // If the boolean hasn't been set to true, check if the database
        // Contains that date for this user
      //  userRecordedYesterday = didUserSubmitYesterday();

        checkYesterday();

        initializeImageButtons();

        Button next_btn = findViewById(R.id.Mood_Btn_Next);

        next_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                for(ImageButton ib : imageButtonsMap.keySet())
                    checkForSelectedMoods(ib);

                if(userRecordedYesterday)
                {
                    addDateAndMoodsToDB(getTodaysDate());
                    selectedMoods.clear();
                }
                else
                {
                    addDateAndMoodsToDB(getYesterdaysDate());

                    userRecordedYesterday = true;

                    clearSelectedImageButtons();
                    updateUI("Please Select your mood for today!");
                    selectedMoods.clear();
                    return;
                }

                Intent launchActivity1= new Intent(
                        CKD.Android.Mood.this,AppetiteAndFatigue.class);
                startActivity(launchActivity1);
            }
        });
    }

    private void checkYesterday()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference Mood_node = db.getReference().child("Data").child("Mood");
        DatabaseReference UID_node = Mood_node.child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        UID_node.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                // Collects all of the users previously submitted data
                for (DataSnapshot d : dataSnapshot.getChildren())
                {
                    if(d.getKey().equals(getYesterdaysDate()))
                    {
                        updateUI("Please Submit for Today");
                        userRecordedYesterday= true;
                        break;
                    }
                }
                if(!userRecordedYesterday)
                    updateUI("Please Submit for Yesterday");
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

    }

    private void clearSelectedImageButtons()
    {
        for(ImageButton ib : imageButtonsMap.keySet())
        {
            ib.setSelected(false);
            setBackgroundOnSelection(ib);
        }
    }

    private void updateUI(String s)
    {
        TextView header = findViewById(R.id.Mood_TV_MoodPrompt);
        header.setText(s);
    }

    private String getYesterdaysDate() {
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");

        // Create a calendar object with today date.
        Calendar calendar = Calendar.getInstance();

        // Move calendar to yesterday
        calendar.add(Calendar.DATE, -1);

        // Get current date of calendar which point to the yesterday now
        Date yesterday = calendar.getTime();

        return dateFormat.format(yesterday);
    }

    private String getTodaysDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");

        // Create a calendar object with today date.
        Calendar calendar = Calendar.getInstance();

        // Get current date of calendar which point to the yesterday now
        Date today = calendar.getTime();

        return dateFormat.format(today);
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

    private void setBackgroundOnSelection(ImageButton button)
    {
        if(button.isSelected())
            button.setBackgroundColor(Color.GREEN);
        else
            button.setBackgroundColor(Color.TRANSPARENT);
    }

    private void addDateAndMoodsToDB(String date)
    {
        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String moods = selectedMoods.toString();

        //Grabs references to the Root node and Users Node
        FirebaseDatabase db = FirebaseDatabase.getInstance();

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

        for(ImageButton ib : imageButtonsMap.keySet())
            setOnClickListeners(ib);
    }
}
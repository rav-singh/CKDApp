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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Mood extends AppCompatActivity
{
    Map<ImageButton,String> imageButtonsMap  = new HashMap<>();
    Boolean userRecordedYesterday = false;

    final private String todayPrompt = "How would you describe your overall mood for today?";
    final private String yesPrompt = "How would you describe your overall mood for yesterday?";

    List<String> selectedMoods = new ArrayList<>();
    Boolean checklistUpdated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);

        // If the User Is directed here from demographics
        // Data from previous Day is not necessary to add
        if(!AppData.isUserRegistering)
        {
            checkYesterday();
        }

        else
        {
            userRecordedYesterday = true;
            updateUI(todayPrompt);
        }

        initializeImageButtons();
        initializeNextButton();

        Button home_btn = findViewById(R.id.Mood_btn_home);
        home_btn = AppData.activateHomeButton(home_btn,Mood.this);

    }


    private void initializeNextButton()
    {
        Button next_btn = findViewById(R.id.Mood_Btn_Next);

        // When Next is clicked selected moods are added
        next_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Goes through the map containing all moods
                // and checks if the user has selected them
                for(ImageButton ib : imageButtonsMap.keySet())
                    checkForSelectedMoods(ib);

                // Enters data into database for current day
                if(userRecordedYesterday)
                {
                    addDateAndMoodsToDB(AppData.getTodaysDate());
                    // TODO should be able to remove this. Test later
                    selectedMoods.clear();
                }
                // Enters data into database for previous day
                // updates UI and Boolean values and clears
                // selected moods
                else
                {
                    addDateAndMoodsToDB(AppData.getYesterdaysDate());

                    userRecordedYesterday = true;

                    clearSelectedImageButtons();
                    updateUI(todayPrompt);
                    selectedMoods.clear();
                    return;
                }
                // TODO Might Crash after user returns to mood after registering
                AppData.isUserRegistering = false;

                if(!checklistUpdated)
                {
                    AppData.updateDailyChecklist("Mood");
                    checklistUpdated = true;
                }

                Intent launchActivity1= new Intent(
                    Mood.this,AppetiteAndFatigue.class);
                startActivity(launchActivity1);
            }
        });
    }

    // Reads from the database to determine if the user has recorded
    // their mood from the previous day. A boolean is used for later
    // referencing
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
                    if(d.getKey().equals(AppData.getYesterdaysDate()))
                    {
                        updateUI(todayPrompt);
                        userRecordedYesterday= true;
                        break;
                    }
                }
                if(!userRecordedYesterday)
                    updateUI(yesPrompt);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

    }

    // If the user submits their mood from the previous day, this method
    // clears all selected states of image buttons for submitting the
    // current day
    private void clearSelectedImageButtons()
    {
        for(ImageButton ib : imageButtonsMap.keySet())
        {
            ib.setSelected(false);
            setBackgroundOnSelection(ib);
        }
    }

    // Updates the UI depending on whether the user is submitting for the
    // current day or the previous day
    private void updateUI(String s)
    {
        TextView header = findViewById(R.id.Mood_TV_MoodPrompt);
        header.setText(s);
    }

    // Sets all image buttons to select and deselect upon clicking
    // and sets background to show appropriate selected state
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

    // Updates ImageButtons backgrounds based on Selected State
    private void setBackgroundOnSelection(ImageButton button)
    {
        if(button.isSelected())
            button.setBackgroundColor(Color.GREEN);
        else
            button.setBackgroundColor(Color.TRANSPARENT);
    }

    // Places the users selected moods int a string and enters
    // the data into the database with appropriate date
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

    // Places all of the possible moods into hash map
    // to correlate the mood with the image button and
    // then sets all of the onClickListeners

    private void initializeImageButtons()
    {
        //TODO Add alternate images in resources file
        ImageButton happy_btn = findViewById(R.id.Mood_ImBtn_Happy);
        imageButtonsMap.put(happy_btn,"Happy");

        ImageButton depressed_btn = findViewById(R.id.Mood_ImBtn_Depressed);
        imageButtonsMap.put(depressed_btn,"Depressed");

        ImageButton anxious_btn = findViewById(R.id.Mood_ImBtn_Anxious);
        imageButtonsMap.put(anxious_btn,"Anxious");

        ImageButton fatigued_btn = findViewById(R.id.Mood_ImBtn_Fatigued);
        imageButtonsMap.put(fatigued_btn, "Fatigued");

        ImageButton flat_btn = findViewById(R.id.Mood_ImBtn_Meh);
        imageButtonsMap.put(flat_btn,"Meh");

        ImageButton nausea_btn = findViewById(R.id.Mood_ImBtn_Nausea);
        imageButtonsMap.put(nausea_btn,"Nauseous");

        for(ImageButton ib : imageButtonsMap.keySet())
            setOnClickListeners(ib);
    }
}
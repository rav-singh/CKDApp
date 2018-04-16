package CKD.Android;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityLog extends AppCompatActivity {

    private EditText activityET;
    private Button addActivity;;
    ArrayList<String> ActivityNames = new ArrayList<>();
    ArrayList<String> ActivityDurations = new ArrayList<>();
    LinearLayout activitiesLL;


    FirebaseDatabase db = FirebaseDatabase.getInstance();
    String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        addActivity = findViewById(R.id.addActivity_Button);
        activityET = findViewById(R.id.activityInput);
        activitiesLL = findViewById(R.id.ActivityLog_LL);
        Button home = findViewById(R.id.activityLog_btn_home);
        home = AppData.activateHomeButton(home,ActivityLog.this);

        // Grab all current entries
        loadEntries();

        // Once the Add Activity button is pressed
        addActivity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                String newActivity = activityET.getText().toString();

                // Makes sure user does not enter empty string or an exercise with a comma
                //TODO TOAST ERROR
                if(newActivity.length() <= 2 || newActivity.contains(","))
                    return;

                ActivityNames.add(newActivity);

                askDuration();
                activityET.setText("");

            }
            private void askDuration()
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityLog.this);
                builder.setTitle("Duration (in minutes): ");

                LinearLayout ll = new LinearLayout(ActivityLog.this);
                ll.setOrientation(LinearLayout.VERTICAL);

                final EditText input = new EditText(ActivityLog.this);

                input.setInputType(InputType.TYPE_CLASS_TEXT);
                ll.addView(input);
                builder.setView(ll);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        duration = input.getText().toString();

                        ActivityDurations.add(duration);

                        DatabaseReference dataNode = db.getReference()
                                .child("Data")
                                .child("Exercise")
                                .child(UID)
                                .child(AppData.getTodaysDate());

                        dataNode.child("ActivityNames").setValue(ActivityNames.toString());
                        dataNode.child("ActivityDurations").setValue(ActivityDurations.toString());

                        displayEntries();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }




    private void loadEntries ()
    {
        DatabaseReference activityEntryNode = db.getReference()
                                                .child("Data")
                                                .child("Exercise")
                                                .child(UID)
                                                .child(AppData.getTodaysDate());

        activityEntryNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dbActivityNames = dataSnapshot.child("ActivityNames").getValue(String.class);
                String dbActivityDurations = dataSnapshot.child("ActivityDurations").getValue(String.class);

                if(dbActivityNames == null)
                    return;

                dbActivityNames = dbActivityNames.replace("]","");
                dbActivityNames = dbActivityNames.replace("[","");
                dbActivityDurations = dbActivityDurations.replace("]","");
                dbActivityDurations = dbActivityDurations.replace("[","");

                List<String> namesList = Arrays.asList(dbActivityNames.split(","));
                List<String> durationList = Arrays.asList(dbActivityDurations.split(","));

                ActivityNames.addAll(namesList);
                ActivityDurations.addAll(durationList);

                displayEntries();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void displayEntries()
    {
        int start = activitiesLL.getChildCount();

        RelativeLayout rl = findViewById(R.id.ActivityLog_RL);
        int width = (int) (rl.getWidth()*.5);

        LinearLayout.LayoutParams params_LL = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams params_TV = new LinearLayout.LayoutParams
                (width, LinearLayout.LayoutParams.WRAP_CONTENT);
        params_TV.setMargins(10,0,10,0);


        // For each activity it creates two Text Views (One for name and one for time)
        // Both of those are added into a horizontal linear Layout. That linear layout
        // is then added into a vertical linear layout
        for(int i = start;  i < ActivityNames.size() ; i++)
        {
            LinearLayout singleActivity_ll = new LinearLayout(this);
            singleActivity_ll.setLayoutParams(params_LL);
            singleActivity_ll.setOrientation(LinearLayout.HORIZONTAL);


            TextView name = new TextView(this);
            TextView duration = new TextView(this);

            name.setLayoutParams(params_TV);
            duration.setLayoutParams(params_TV);

            name.setTextSize(17);
            name.setPadding(5, 3, 0, 3);
            name.setTypeface(Typeface.DEFAULT_BOLD);
            name.setText(ActivityNames.get(i));

            duration.setTextSize(17);
            duration.setPadding(5, 3, 0, 3);
            duration.setTypeface(Typeface.DEFAULT_BOLD);
            duration.setText(ActivityDurations.get(i));

            singleActivity_ll.addView(name);
            singleActivity_ll.addView(duration);
            singleActivity_ll.setBackground(getResources().getDrawable(R.drawable.rounded_corner_textview));

            activitiesLL.addView(singleActivity_ll);
        }


    }


}


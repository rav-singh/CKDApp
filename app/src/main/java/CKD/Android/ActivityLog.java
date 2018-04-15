package CKD.Android;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class ActivityLog extends AppCompatActivity {

    private EditText activityET;
    private Button addActivity;
    private ListView list_activities;
    ArrayAdapter<activityItem> adapter_entries;
    ArrayList<String> ActivityNames;
    ArrayList<String> ActivityDurations;
    ArrayList<activityItem> itemList;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

//        list_names = (ListView) findViewById(R.id.activities_LV_Names);
//        list_durations = (ListView) findViewById(R.id.activities_LV_Durations);
        list_activities = (ListView) findViewById(R.id.activities_LV);

        addActivity = findViewById(R.id.addActivity_Button);
        activityET = findViewById(R.id.activityInput);

        // Grab all current entries
        loadEntries();

        // Display all current entries
        //displayEntries();

        // Once the Add Activity button is pressed
        addActivity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addButtonPressed();
            }
        });
    }

    private void addButtonPressed() {
        String newActivity = activityET.getText().toString();
       // activityET.setText("");
        Log.i("Activity is: ", newActivity);
        ActivityNames.add(newActivity);

        askDuration();
        activityET.setText("");
        //loadEntries();
    }

    private void askDuration() {
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
            public void onClick(DialogInterface dialog, int which) {
                duration = input.getText().toString();
                ActivityDurations.add(duration);
                DatabaseReference dataNode = db.getReference().child("Data").child("Exercise").child(UID).child(AppData.getTodaysDate());
                dataNode.child("ActivityNames").setValue(ActivityNames.toString());
                dataNode.child("ActivityDurations").setValue(ActivityDurations.toString());
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

    private void loadEntries () {
        DatabaseReference activityEntryNode = db.getReference().child("Data").child("Exercise").child(UID).child(AppData.getTodaysDate());
        activityEntryNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dbActivityNames = dataSnapshot.child("ActivityNames").getValue(String.class);
                String dbActivityDurations = dataSnapshot.child("ActivityDurations").getValue(String.class);
                Log.i("1: Names:", dbActivityNames);
                Log.i("1: Durations:", dbActivityDurations);

                dbActivityNames = dbActivityNames.replace("]","");
                dbActivityNames = dbActivityNames.replace("[","");
                dbActivityDurations = dbActivityDurations.replace("]","");
                dbActivityDurations = dbActivityDurations.replace("[","");

                Log.i("2: Names:", dbActivityNames);
                Log.i("2: Durations:", dbActivityDurations);

                ActivityNames = new ArrayList<String> (Arrays.asList(dbActivityNames.split(", ")));
                ActivityDurations = new ArrayList<String> (Arrays.asList(dbActivityDurations.split(", ")));

                itemList = new ArrayList<activityItem>();

                for(int i = 0; i < ActivityNames.size(); i++) {
                    activityItem myActivity = new activityItem(ActivityNames.get(i), ActivityDurations.get(i));
                    itemList.add(myActivity);
                }
                for(activityItem item : itemList) {
                    Log.i("Combined:", item.toString());
                }

                Log.i("As ArrayList:", ActivityNames.toString());
                Log.i("As ArrayList:", ActivityDurations.toString());

                displayEntries();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void displayEntries() {
        Log.i("In displayEntries:", ActivityNames.toString());
        adapter_entries = new ArrayAdapter<activityItem>(this, android.R.layout.simple_list_item_1, itemList);

        list_activities.setAdapter(adapter_entries);
    }
}

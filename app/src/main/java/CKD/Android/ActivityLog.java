package CKD.Android;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    ArrayList<String> ActivityNames;
    ArrayList<String> ActivityDurations;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        addActivity = findViewById(R.id.addActivity_Button);
        activityET = findViewById(R.id.activityInput);

        // Need to grab all activity log entries from DB and display them here.
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

                ActivityNames = new ArrayList<String>(Arrays.asList(dbActivityNames.split(", ")));
                ActivityDurations = new ArrayList<String> (Arrays.asList(dbActivityDurations.split(", ")));

                Log.i("As ArrayList:", ActivityNames.toString());
                Log.i("As ArrayList:", ActivityDurations.toString());

                // At this point, I have the Activity Names and Durations stored as ArrayList<String>
                // Need to display them like with Diet entries.
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        // Once the Add Activity button is pressed
        addActivity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String newActivity = activityET.getText().toString();
                Log.i("Activity is: ", newActivity);
                ActivityNames.add(newActivity);

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
        });
    }
}

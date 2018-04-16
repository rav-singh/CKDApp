package CKD.Android;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
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

    final float[] topLeftCorner = new float[]{20.0f, 20.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    final float[] topRightCorner = new float[]{0.0f, 0.0f, 20.0f, 20.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    final float[] botRightCorner = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 20.0f, 20.0f, 0.0f, 0.0f};
    final float[] botLeftCorner = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 20.0f, 20.0f};
    final float[] leftCorners = new float[]{20.0f, 20.0f, 0.0f, 0.0f, 0.0f, 0.0f, 20.0f, 20.0f};
    final float[] rightCorners = new float[]{0.0f, 0.0f, 20.0f, 20.0f, 20.0f, 20.0f, 0.0f, 0.0f};
    int colorA;
    int colorB;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        colorA = getResources().getColor(R.color.LightGreen);
        colorB = getResources().getColor(R.color.LightCoral);

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
                String newActivity = activityET.getText().toString().trim();

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

                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                ll.addView(input);
                builder.setView(ll);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        duration = input.getText().toString().trim();

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

        LinearLayout.LayoutParams params_LL = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams params_activity = new LinearLayout.LayoutParams
                (0, LinearLayout.LayoutParams.WRAP_CONTENT,4);


        LinearLayout.LayoutParams params_duration = new LinearLayout.LayoutParams
                (0, LinearLayout.LayoutParams.WRAP_CONTENT,1);

        // For each activity it creates two Text Views (One for name and one for time)
        // Both of those are added into a horizontal linear Layout. That linear layout
        // is then added into a vertical linear layout
        for(int i = start;  i < ActivityNames.size() ; i++)
        {
            // If there are existing views in the layout, the
            // rounded corners need to be removed for aesthetics
            if(i!=0)
            {
                updateLastActivityCorners(i-1);
            }

            LinearLayout singleActivity_ll = new LinearLayout(this);

            singleActivity_ll.setLayoutParams(params_LL);
            singleActivity_ll.setOrientation(LinearLayout.HORIZONTAL);
            singleActivity_ll.setWeightSum(5);

            TextView name = new TextView(this);
            TextView duration = new TextView(this);

            name.setLayoutParams(params_activity);
            duration.setLayoutParams(params_duration);

            name.setTextSize(20);
            name.setTextColor(Color.BLACK);
            name.setText(ActivityNames.get(i).trim());
            name.setPadding(50,0,0,0);

            duration.setTextSize(20);
            duration.setTextColor(Color.BLACK);
            duration.setText(ActivityDurations.get(i).trim());

            GradientDrawable d1 = new GradientDrawable();
            GradientDrawable d2 = new GradientDrawable();

            // First and only Activity
            if(i == 0 && i == ActivityNames.size()-1)
            {
                d1.setCornerRadii(leftCorners);
                d1.setColor(colorA);
                name.setBackground(d1);

                d2.setCornerRadii(rightCorners);
                d2.setColor(colorA);
                duration.setBackground(d2);
            }

            // If first(but not last) activity add top corners
            else if(i == 0)
            {
                d1.setCornerRadii(topLeftCorner);
                d1.setColor(colorA);
                name.setBackground(d1);

                d2.setCornerRadii(topRightCorner);
                d2.setColor(colorA);
                duration.setBackground(d2);
            }

            // If last activity, make bottom corners
            else if(i == ActivityNames.size()-1)
            {
                d1.setCornerRadii(botLeftCorner);
                d2.setCornerRadii(botRightCorner);

                // Decides which color the last one is
                if(i%2 ==0)
                {
                    d1.setColor(colorA);
                    d2.setColor(colorA);
                }
                else
                {
                    d1.setColor(colorB);
                    d2.setColor(colorB);
                }

                name.setBackground(d1);
                duration.setBackground(d2);

            }
            // Last two take care of middle section where no rounded
            // corners are needed
            else if(i%2 == 0 )
            {
                name.setBackgroundColor(colorA);
                duration.setBackgroundColor(colorA);
            }
            else
            {
                name.setBackgroundColor(colorB);
                duration.setBackgroundColor(colorB);
            }


            singleActivity_ll.addView(name);
            singleActivity_ll.addView(duration);

            activitiesLL.addView(singleActivity_ll);
        }


    }

    private void updateLastActivityCorners(int lastViewIndex)
    {
        GradientDrawable d1 = new GradientDrawable();
        GradientDrawable d2 = new GradientDrawable();

        LinearLayout lastActivity = (LinearLayout) activitiesLL.getChildAt(lastViewIndex);

        TextView name = (TextView) lastActivity.getChildAt(0);
        TextView duration = (TextView) lastActivity.getChildAt(1);

        name.setBackground(null);
        duration.setBackground(null);

        if(lastViewIndex == 0)
        {
            d1.setCornerRadii(topLeftCorner);
            d1.setColor(colorA);
            d2.setCornerRadii(topRightCorner);
            d2.setColor(colorA);

            name.setBackground(d1);
            duration.setBackground(d2);
        }

        else if(lastViewIndex %2 == 0)
        {
            name.setBackgroundColor(colorA);
            duration.setBackgroundColor(colorA);
        }
        else
        {
            name.setBackgroundColor(colorB);
            duration.setBackgroundColor(colorB);
        }



    }


}


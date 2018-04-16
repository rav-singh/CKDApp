package CKD.Android;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Summary extends AppCompatActivity
{

    private Button export;
    private DataSnapshot DS;

    private List<String> adminsUIDList = new ArrayList<>();
    private List<String> selectedAdminsUIDList = new ArrayList<>();
    private List<String> adminsNameList = new ArrayList<>();
    private List<String> adminsFacilityList = new ArrayList<>();
    private List<Button> adminsButtonList = new ArrayList<>();
    private LinearLayout adminLL;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        export = findViewById(R.id.Summary_Export);
        adminLL = findViewById(R.id.Summary_AdminListView);

        Button home_btn = findViewById(R.id.Summary_btn_home);
        home_btn = AppData.activateHomeButton(home_btn,Summary.this);

        export.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                addPatientsUIDToSelectedAdmins();
                Intent launchActivity1 = new Intent(Summary.this, HomePage.class);
                startActivity(launchActivity1);
            }
        });

        fillInAdminButtons();
    }


    private void addPatientsUIDToSelectedAdmins()
    {
        String date = AppData.getTodaysDate();
        String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String userName = AppData.cur_user.getName();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference Medical_node = db.getReference("Admins").child("Medical");

        for(String adminUID : selectedAdminsUIDList)
        {
            DatabaseReference Patients_node = Medical_node.child(adminUID).child("Patients");

            Patients_node.child(userUID).child("Name").setValue(userName);
            Patients_node.child(userUID).child("Date").setValue(date);

        }
    }

    private void setOnClickListeners(final Button b, final int i)
    {
        b.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                b.setSelected(!b.isSelected());

                String adminUID = adminsUIDList.get(i);

                if(b.isSelected())
                {
                    selectedAdminsUIDList.add(adminUID);
                    b.setBackground(getResources().getDrawable(R.drawable.rounded_corner_textview_green));
                }
                else
                {
                    selectedAdminsUIDList.remove(adminUID);
                    b.setBackground(getResources().getDrawable(R.drawable.rounded_corner_textview));
                }
            }
        });
    }

    private void fillInAdminButtons()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        DatabaseReference HealthAdmin_node = db.getReference("Admins").child("Medical");

        HealthAdmin_node.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                DS = dataSnapshot;

                // Grabs all of the admins UID's and places them into a List
                fillAdminInfoLists();
                // Creates Buttons Displaying admins name and facility and stores in a list
                initializeAndStoreAdminButtons();
                updateUI();
            }


            private void fillAdminInfoLists()
            {
                for(DataSnapshot d : DS.getChildren())
                {
                    String adminName = (String) d.child("Name").getValue();
                    String adminFacility = (String) d.child("Facility").getValue();
                    // Stores the admins UID, name and Facility into Lists for referencing later
                    adminsUIDList.add(d.getKey());
                    adminsNameList.add(adminName);
                    adminsFacilityList.add(adminFacility);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    private void initializeAndStoreAdminButtons()
    {
        for(int i = 0; i<adminsUIDList.size(); i++)
        {
            String name = adminsNameList.get(i);
            String facility = adminsFacilityList.get(i);
            String displayName = name.concat(" - ").concat(facility);

            Button adminButton = new Button (this);
            adminButton.setText(displayName);
            adminsButtonList.add(adminButton);
        }
    }

    private void updateUI ()
    {
        TextView prompt = findViewById(R.id.Summary_TV_Prompt);
        int width = prompt.getWidth();

        LinearLayout.LayoutParams params_LL = new LinearLayout.LayoutParams
            (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params_LL.setMargins(10,10,10,10);

        LinearLayout.LayoutParams params_btn = new LinearLayout.LayoutParams
                (width, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(params_LL);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER);
        // Adds all of the buttons in the List into a new temporary Linear layout
        // and then adds the entire temp LL into the adminLL
        for(int i = 0;  i < adminsButtonList.size() ; i++)
        {
            Button adminButton = adminsButtonList.get(i);
            adminButton.setLayoutParams(params_btn);
            adminButton.setBackground(this.getResources().getDrawable(R.drawable.rounded_corner_textview));
            adminButton.setGravity(Gravity.CENTER);
            setOnClickListeners(adminButton,i);

            ll.addView(adminButton);
        }
            adminLL.addView(ll);
    }




}

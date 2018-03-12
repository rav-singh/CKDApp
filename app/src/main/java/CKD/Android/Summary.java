package CKD.Android;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
    private List<String> adminsnameList = new ArrayList<>();
    private Map<String,String> selectedAdmins= new HashMap<>();
    private List<Button> allAdminBtnList = new ArrayList<>();
    private List<Button> activeAdminBtnList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        initializeButtons();

        fillInAdminButtons();


        int i = 0;
        for( Button b : activeAdminBtnList)
        {
            setOnClickListeners(b, i++);
        }

        export.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                addPatientsUIDToSelectedAdmins();
                Intent launchActivity1 = new Intent(Summary.this, HomePage.class);
                startActivity(launchActivity1);
            }
        });
    }

    private void UpdateUI()
    {
        LinearLayout ll = findViewById(R.id.Summary_AdminListView);

        // If a button has in the list of all buttons has not been activated
        // then remove it from the Linear Layout
        for(Button b : allAdminBtnList)
        {
            if(!activeAdminBtnList.contains(b))
                ll.removeView(b);
        }
    }

    private void addPatientsUIDToSelectedAdmins()
    {
        // String date = new SimpleDateFormat("YYYY-MM-dd", Locale.getDefault()).format(new Date());
        String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase db = FirebaseDatabase.getInstance();

        DatabaseReference Admin_node = db.getReference("Admins");

        for(String adminUID : selectedAdmins.keySet())
        {
            DatabaseReference Patients_node = Admin_node.child(adminUID).child("Patients");
            Patients_node.child(AppData.cur_user.getName()).setValue(userUID);
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
                String adminName = adminsnameList.get(i);

                if(b.isSelected())
                {
                    selectedAdmins.put(adminUID, adminName);
                    b.setBackgroundColor(Color.GREEN);
                }
                else
                {
                    selectedAdmins.remove(adminUID);
                    b.setBackgroundColor(Color.WHITE);
                }
            }
        });
    }

    private void fillInAdminButtons()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        DatabaseReference Admin_node = db.getReference("Admins");

        Admin_node.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                DS = dataSnapshot;

                // Grabs all of the admins UID's and places them into a List
                fillAdminUIDList();

                int i = 0;
                for(String adminKey : adminsUIDList)
                {
                    fillInAdminButtons(adminKey, i++);
                }
                UpdateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }
    // Grabs Admins Name from Database and sets the text on that button
    // to said name. Then adds the admins name to the list in the same index
    // as well as adding that button to the activated Buttons list
    private void fillInAdminButtons(String UID, int i )
    {
        String adminName = (String)DS.child(UID).child("Name").getValue();

        allAdminBtnList.get(i).setText(adminName);

        adminsnameList.add(adminName);

        activeAdminBtnList.add(allAdminBtnList.get(i));
    }

    private void fillAdminUIDList()
    {
        for(DataSnapshot d : DS.getChildren())
        {
            adminsUIDList.add(d.getKey());
        }
    }

    private void initializeButtons()
    {
        Button admin1 = findViewById(R.id.Summary_Admin1);
        allAdminBtnList.add(admin1);
        Button admin2 = findViewById(R.id.Summary_Admin2);
        allAdminBtnList.add(admin2);
        Button admin3 = findViewById(R.id.Summary_Admin3);
        allAdminBtnList.add(admin3);
        Button admin4 = findViewById(R.id.Summary_Admin4);
        allAdminBtnList.add(admin4);
        Button admin5 = findViewById(R.id.Summary_Admin5);
        allAdminBtnList.add(admin5);
        Button admin6 = findViewById(R.id.Summary_Admin6);
        allAdminBtnList.add(admin6);
        Button admin7 = findViewById(R.id.Summary_Admin7);
        allAdminBtnList.add(admin7);
        Button admin8 = findViewById(R.id.Summary_Admin8);
        allAdminBtnList.add(admin8);
        Button admin9 = findViewById(R.id.Summary_Admin9);
        allAdminBtnList.add(admin9);
        Button admin10 = findViewById(R.id.Summary_Admin10);
        allAdminBtnList.add(admin10);

        export = findViewById(R.id.Summary_Export);


    }
}

package CKD.Android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Summary extends AppCompatActivity
{
    private Button  admin1, admin2,
                    admin3, admin4,
                    admin5, export;

    private DataSnapshot DS;

    private List<String> adminsUIDList = new ArrayList<>();
    private List<String> adminsnameList = new ArrayList<>();
    private Map<String,String> selectedAdmins= new HashMap<>();
    private List<Button> adminBtnList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        initializeButtons();

        fillInAdminButtons();


        int i =0;
        for( Button b : adminBtnList)
        {
            setOnClickListeners(b, i++);
        }

        export.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                addPatientsUIDToSelectedAdmins();
            }
        });

    }

    private void addPatientsUIDToSelectedAdmins()
    {
        //TODO Need to make sure that the date still works appropriately when past the 10th
        String date = new SimpleDateFormat("YYYY-MM-dd", Locale.getDefault()).format(new Date());
        String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Grabs references to the Root node and Users Node
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        // Creates a Mood Child Node under Data
        // Creates a UID Child Node under Mood
        DatabaseReference Admin_node = db.getReference("Admins");

        for(String adminUID : selectedAdmins.keySet())
        {
            DatabaseReference Patients_node = Admin_node.child(adminUID).child("Patients");
            Patients_node.child(date).setValue(userUID);
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

                // Grabs all of the admins UID's and places them into
                // and List
                fillAdminUIDList();

                int i = 0;
                for(String adminKey : adminsUIDList)
                {
                    fillInAdminButtons(adminKey, i++);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    private void fillInAdminButtons(String UID, int i )
    {
        String adminName = (String)DS.child(UID).child("Name").getValue();
        adminBtnList.get(i).setText(adminName);
        adminsnameList.add(adminName);
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
        admin1 = findViewById(R.id.Summary_Admin1);
        adminBtnList.add(admin1);
        admin2 = findViewById(R.id.Summary_Admin2);
        adminBtnList.add(admin2);
        admin3 = findViewById(R.id.Summary_Admin3);
        adminBtnList.add(admin3);
        admin4 = findViewById(R.id.Summary_Admin4);
        adminBtnList.add(admin4);
        admin5 = findViewById(R.id.Summary_Admin5);
        adminBtnList.add(admin5);
        export = findViewById(R.id.Summary_Export);


    }
}

package CKD.Android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class Summary extends AppCompatActivity
{
    private Button  admin1, admin2,
            admin3, admin4,
            admin5, export;
    private DataSnapshot DS;
    private List<String> adminsList = new ArrayList<>();
    private List<Button> adminBtnList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        initializeButtons();

        fillInAdminButtons();

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
                for(String adminKey : adminsList)
                {
                    fillInAdminButtons(adminKey, i++);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fillInAdminButtons(String UID, int i )
    {
        adminBtnList.get(i).setText((String)DS.child(UID).child("Name").getValue());
    }

    private void fillAdminUIDList()
    {
        for(DataSnapshot d : DS.getChildren())
        {
            adminsList.add(d.getKey());
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

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
    Button  admin1, admin2,
            admin3, admin4,
            admin5, export;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        initializeButtons();


    }

    private void initializeButtons()
    {
        admin1 = findViewById(R.id.Summary_Admin1);
        admin2 = findViewById(R.id.Summary_Admin2);
        admin3 = findViewById(R.id.Summary_Admin3);
        admin4 = findViewById(R.id.Summary_Admin4);
        admin5 = findViewById(R.id.Summary_Admin5);
        export = findViewById(R.id.Summary_Export);
    }
}

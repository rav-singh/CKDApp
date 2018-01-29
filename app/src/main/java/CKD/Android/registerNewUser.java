package CKD.Android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registerNewUser extends AppCompatActivity {

    public EditText name;
    private String mDisplayName;
    private Button Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_user);

        final EditText name = (EditText) findViewById(R.id.nameTF);
        Button Register = (Button) findViewById(R.id.registerNow);

        // OnClick Listener that redirects to Register Page
        Register.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // Convert Text to String and store it it in var mDisplayName
                mDisplayName = name.getText().toString();
                // Get Instance of Database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                // Child Name
                DatabaseReference myRef = database.getReference("NAME");
                // Set Value to whatever name user Enters
                myRef.setValue(mDisplayName);

            }
        });

        // Read from the database
        /*myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });*/
    }
}

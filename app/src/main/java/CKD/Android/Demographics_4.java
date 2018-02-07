package CKD.Android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import java.util.ArrayList;
import java.util.List;

public class Demographics_4 extends AppCompatActivity
{
    List<String> co_Morbid_list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demographics_4);

        final CheckBox allergy = findViewById(R.id.Demo4_CB_Allergy);
        final CheckBox arthritis = findViewById(R.id.Demo4_CB_Arthritis);
        final CheckBox blood_clots = findViewById(R.id.Demo4_CB_BloodClots);
        final CheckBox blood_dis = findViewById(R.id.Demo4_CB_BloodDisorder);
        final CheckBox cancer = findViewById(R.id.Demo4_CB_Cancer);
        final CheckBox cOPD = findViewById(R.id.Demo4_CB_ChronObsPDis);
        final CheckBox dementia = findViewById(R.id.Demo4_CB_Dementia);
        final CheckBox depression = findViewById(R.id.Demo4_CB_Depression);
        final CheckBox diabetes = findViewById(R.id.Demo4_CB_Diabetes);
        final CheckBox glaucoma = findViewById(R.id.Demo4_CB_Glaucoma);
        final CheckBox hart_Dis = findViewById(R.id.Demo4_CB_HeartDisease);
        final CheckBox HBP_HCL = findViewById(R.id.Demo4_CB_HighBPHighChol);
        final CheckBox kid_Dis = findViewById(R.id.Demo4_CB_KidneyDisease);
        final CheckBox liv_Dis = findViewById(R.id.Demo4_CB_LiverDiesease);
        final CheckBox lupus = findViewById(R.id.Demo4_CB_Lupus);
        final CheckBox mac_Degen = findViewById(R.id.Demo4_CB_MacularDegen);
        final CheckBox mental = findViewById(R.id.Demo4_CB_MentalHthCond);
        final CheckBox migraine = findViewById(R.id.Demo4_CB_Migraines);
        final CheckBox mult_scl = findViewById(R.id.Demo4_CB_MS);
        final CheckBox park_Dis = findViewById(R.id.Demo4_CB_ParkDis);
        final CheckBox stroke = findViewById(R.id.Demo4_CB_Stroke);
        final CheckBox thyroid_Dis = findViewById(R.id.Demo4_CB_Thyroid);
        final CheckBox other = findViewById(R.id.Demo4_CB_Other);
        Button next = findViewById(R.id.Demo4_Btn_Next);

        next.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Called once user clicks next, Updates class list with all the diseases user
                // selected
                collectCoMorbList();
                // Updates list of comorbitities to AppData current User Class
                updateUserClass();

                switchPages(Demographics_4.this, Demographics_2.class);
            }

            private void collectCoMorbList()
            {
                if (allergy.isChecked()) {co_Morbid_list.add(allergy.getText().toString());}
                if (arthritis.isChecked()) {co_Morbid_list.add(arthritis.getText().toString());}
                if (blood_clots.isChecked()) {co_Morbid_list.add(blood_clots.getText().toString());}
                if (blood_dis.isChecked()) {co_Morbid_list.add(blood_dis.getText().toString());}
                if (cancer.isChecked()) {co_Morbid_list.add(cancer.getText().toString());}
                if (cOPD.isChecked()) {co_Morbid_list.add(cOPD.getText().toString());}
                if (dementia.isChecked()) {co_Morbid_list.add(dementia.getText().toString());}
                if (depression.isChecked()) {co_Morbid_list.add(depression.getText().toString());}
                if (diabetes.isChecked()) {co_Morbid_list.add(diabetes.getText().toString());}
                if (glaucoma.isChecked()) {co_Morbid_list.add(glaucoma.getText().toString());}
                if (hart_Dis.isChecked()) {co_Morbid_list.add(hart_Dis.getText().toString());}
                if (HBP_HCL.isChecked()) {co_Morbid_list.add(HBP_HCL.getText().toString());}
                if (kid_Dis.isChecked()) {co_Morbid_list.add(kid_Dis.getText().toString());}
                if (liv_Dis.isChecked()) {co_Morbid_list.add(liv_Dis.getText().toString());}
                if (lupus.isChecked()) {co_Morbid_list.add(lupus.getText().toString());}
                if (mac_Degen.isChecked()) {co_Morbid_list.add(mac_Degen.getText().toString());}
                if (mental.isChecked()) {co_Morbid_list.add(mental.getText().toString());}
                if (migraine.isChecked()) {co_Morbid_list.add(migraine.getText().toString());}
                if (mult_scl.isChecked()) {co_Morbid_list.add(mult_scl.getText().toString());}
                if (park_Dis.isChecked()) {co_Morbid_list.add(park_Dis.getText().toString());}
                if (stroke.isChecked()) {co_Morbid_list.add(stroke.getText().toString());}
                if (thyroid_Dis.isChecked()) {co_Morbid_list.add(thyroid_Dis.getText().toString());}
                if (other.isChecked()) {co_Morbid_list.add(other.getText().toString());}
                if (co_Morbid_list.isEmpty()){co_Morbid_list.add("User selected no prior health issues");}
            }

        });
    }
    private void updateUserClass()
    {
        AppData.cur_user.setCoMorbs(co_Morbid_list);
    }


    private void switchPages(Demographics_4 currentPage, Class nextPage)
    {
        Intent launchActivity1= new Intent(currentPage,nextPage);
        startActivity(launchActivity1);
    }

}




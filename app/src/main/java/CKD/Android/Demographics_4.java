package CKD.Android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class Demographics_4 extends AppCompatActivity
{
    List<String> co_Morbid_list = new ArrayList<>();
    List<CheckBox> allCheckBoxes = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demographics_4);

        initializeCheckBoxes();

        setOtherOnClickListener();

        // Initially want to remove the EditText from the Layout
        // and add back in once user checks the box
        removeOtherEditTextfromlayout();

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
        });
    }

    private void setOtherOnClickListener()
    {
        final CheckBox otherCB = allCheckBoxes.get(allCheckBoxes.size()-1);

        otherCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(otherCB.isChecked())
                   addOtherEditTextToLayout();
                else
                    removeOtherEditTextfromlayout();
            }
        });
    }

    private void removeOtherEditTextfromlayout()
    {
        EditText otherET = findViewById(R.id.Demo4_ET_Other);
        otherET.setEnabled(false);
        otherET.setBackgroundColor(Color.TRANSPARENT);;
    }

    private void addOtherEditTextToLayout()
    {
        EditText otherET = findViewById(R.id.Demo4_ET_Other);
        otherET.setEnabled(true);
        otherET.setBackground(this.getResources().getDrawable(R.drawable.rounded_corner_textview));
    }

    private void initializeCheckBoxes()
    {
        final CheckBox allergy = findViewById(R.id.Demo4_CB_Allergy);
        allCheckBoxes.add(allergy);
        final CheckBox arthritis = findViewById(R.id.Demo4_CB_Arthritis);
        allCheckBoxes.add(arthritis);
        final CheckBox blood_clots = findViewById(R.id.Demo4_CB_BloodClots);
        allCheckBoxes.add(blood_clots);
        final CheckBox blood_dis = findViewById(R.id.Demo4_CB_BloodDisorder);
        allCheckBoxes.add(blood_dis);
        final CheckBox cancer = findViewById(R.id.Demo4_CB_Cancer);
        allCheckBoxes.add(cancer);
        final CheckBox cOPD = findViewById(R.id.Demo4_CB_ChronObsPDis);
        allCheckBoxes.add(cOPD);
        final CheckBox dementia = findViewById(R.id.Demo4_CB_Dementia);
        allCheckBoxes.add(dementia);
        final CheckBox depression = findViewById(R.id.Demo4_CB_Depression);
        allCheckBoxes.add(depression);
        final CheckBox diabetes = findViewById(R.id.Demo4_CB_Diabetes);
        allCheckBoxes.add(diabetes);
        final CheckBox glaucoma = findViewById(R.id.Demo4_CB_Glaucoma);
        allCheckBoxes.add(glaucoma);
        final CheckBox hart_Dis = findViewById(R.id.Demo4_CB_HeartDisease);
        allCheckBoxes.add(hart_Dis);
        final CheckBox HBP_HCL = findViewById(R.id.Demo4_CB_HighBPHighChol);
        allCheckBoxes.add(HBP_HCL);
        final CheckBox kid_Dis = findViewById(R.id.Demo4_CB_KidneyDisease);
        allCheckBoxes.add(kid_Dis);
        final CheckBox liv_Dis = findViewById(R.id.Demo4_CB_LiverDiesease);
        allCheckBoxes.add(liv_Dis);
        final CheckBox lupus = findViewById(R.id.Demo4_CB_Lupus);
        allCheckBoxes.add(lupus);
        final CheckBox mac_Degen = findViewById(R.id.Demo4_CB_MacularDegen);
        allCheckBoxes.add(mac_Degen);
        final CheckBox mental = findViewById(R.id.Demo4_CB_MentalHthCond);
        allCheckBoxes.add(mental);
        final CheckBox migraine = findViewById(R.id.Demo4_CB_Migraines);
        allCheckBoxes.add(migraine);
        final CheckBox mult_scl = findViewById(R.id.Demo4_CB_MS);
        allCheckBoxes.add(mult_scl);
        final CheckBox park_Dis = findViewById(R.id.Demo4_CB_ParkDis);
        allCheckBoxes.add(park_Dis);
        final CheckBox stroke = findViewById(R.id.Demo4_CB_Stroke);
        allCheckBoxes.add(stroke);
        final CheckBox thyroid_Dis = findViewById(R.id.Demo4_CB_Thyroid);
        allCheckBoxes.add(thyroid_Dis);
        final CheckBox other = findViewById(R.id.Demo4_CB_Other);
        allCheckBoxes.add(other);
    }

    private void collectCoMorbList()
    {
        for(CheckBox cb : allCheckBoxes)
        {
            if(cb.isChecked())
            {
                if(cb.getText().toString().equals("Other"))
                {
                    co_Morbid_list.add(cb.getText().toString());
                }
                else
                {
                    co_Morbid_list.add(cb.getText().toString());
                }
            }
        }
        if (co_Morbid_list.isEmpty()){co_Morbid_list.add("User selected no prior health issues");}
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




package CKD.Android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Demographics_1 extends AppCompatActivity
{
    List<CheckBox> daysCBList = new ArrayList<>();
    Map<String,LinearLayout> llDaysMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demographics_1);

        // UI Components
        final EditText Name = findViewById(R.id.Demo_TF_Name);
        final EditText Email =  findViewById(R.id.Demo_TF_Email);
        final EditText ConfirmEmail = findViewById(R.id.Demo_TF_Email_Confirm);
        final EditText Phone = findViewById(R.id.Demo_TF_Phone);
        final Spinner ActivityLevel = findViewById(R.id.Demo_Spn_ActivityL);

        final Button Register = findViewById(R.id.Demo_Btn_Next_1);

        initializeAndStoreLinearLayouts();
        initializeAndStoreCheckBoxes();

        // May be excessive and might simplify by removing name from Register Page
         Email.setText(AppData.cur_user.getEmail());

        // OnClick Listener that redirects to homePage
        Register.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Grabs all user input into strings
                String userName = Name.getText().toString();
                String userEmail = Email.getText().toString();
                String phoneNumber = Phone.getText().toString();
                String activityLevel = (String) ActivityLevel.getSelectedItem();
                String confirmEmail = ConfirmEmail.getText().toString();

                if (activityLevel.contains("Inactive"))
                    activityLevel = "1";
                if (activityLevel.contains("Active"))
                    activityLevel = "10";

               Boolean error = catchAnyErrors(userName,userEmail,phoneNumber,activityLevel,confirmEmail);
               if(error) return;

                List<String> daysSelected = getDaysSelected();

                try
                {
                    getStartTimes(daysSelected);
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }

                // Adds users input into Current_user Class in AppData
                addUserClassValues(userName, userEmail, phoneNumber, activityLevel, daysSelected);

                // Directs User to HomePage
                switchPages();
            }


            private Boolean catchAnyErrors(String userName, String userEmail, String phoneNumber,
                                           String activityLevel, String confirmEmail)
            {
                // If user does not match the email typed on the previous page
                // they are prompted with an error message and the textView
                // becomes editable
                if (!emailsMatch(userEmail, confirmEmail))
                {
                    Toast.makeText(Demographics_1.this,
                            "Emails do not Match!",
                            Toast.LENGTH_LONG).show();
                    Email.setEnabled(true);
                    return true;
                }

                if (!validEmail(userEmail)) {
                    Toast.makeText(Demographics_1.this,
                            "Please enter a valid Email!",
                            Toast.LENGTH_LONG).show();
                    return true;
                }

                if (!allFieldsEntered(userEmail, userName, phoneNumber, activityLevel)) {
                    Toast.makeText(Demographics_1.this,
                            "Please fill in all data entry fields to continue!",
                            Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }

            private List<String> getDaysSelected()
            {
                List<String> selected = new ArrayList<>();

                for(CheckBox cb : daysCBList)
                {
                    if(cb.isChecked())
                    {
                        selected.add((String) cb.getText());
                    }

                }
                return selected;
            }

            private void getStartTimes(List<String> daysSelected) throws ParseException {
                for(String day : daysSelected)
                {
                    LinearLayout Start_ll = llDaysMap.get(day.concat("_Start"));
                    LinearLayout End_ll = llDaysMap.get(day.concat("_End"));

                    Spinner startTime = (Spinner) Start_ll.getChildAt(1);
                    Spinner endTime = (Spinner) End_ll.getChildAt(1);

                    Spinner startAMorPM = (Spinner) Start_ll.getChildAt(2);
                    Spinner endAMorPM = (Spinner) End_ll.getChildAt(2);

                    String militaryStartTime = getMilitaryTime(startTime,startAMorPM);
                    String militaryEndTime = getMilitaryTime(endTime,endAMorPM);

                    AppData.cur_user.getScheduledStartTime().put(day,militaryStartTime);
                    AppData.cur_user.getScheduledEndTime().put(day,militaryEndTime);
                    AppData.cur_user.setScheduledDays(daysSelected);

                }
            }

            private String getMilitaryTime(Spinner timeSPN, Spinner AMorPMSPN) throws ParseException {
                String Time = timeSPN.getSelectedItem().toString();
                String AMorPM = AMorPMSPN.getSelectedItem().toString();
                String StandardTime = Time.concat(" ").concat(AMorPM);

                SimpleDateFormat StdDateFormat = new SimpleDateFormat("hh:mm a");
                SimpleDateFormat MilDateFormat = new SimpleDateFormat("HH:mm");

                Date StdDate = StdDateFormat.parse(StandardTime);

                return MilDateFormat.format(StdDate);

            }


        });
    }

    private void initializeAndStoreLinearLayouts()
    {
        LinearLayout SuLL = findViewById(R.id.Demo1_LL_Sunday);
        llDaysMap.put("Sunday",SuLL);

        LinearLayout MoLL = findViewById(R.id.Demo1_LL_Monday);
        llDaysMap.put("Monday",MoLL);

        LinearLayout TuLL = findViewById(R.id.Demo1_LL_Tuesday);
        llDaysMap.put("Tuesday",TuLL);

        LinearLayout WeLL = findViewById(R.id.Demo1_LL_Wednesday);
        llDaysMap.put("Wednesday",WeLL);

        LinearLayout ThLL = findViewById(R.id.Demo1_LL_Thursday);
        llDaysMap.put("Thursday",ThLL);

        LinearLayout FrLL = findViewById(R.id.Demo1_LL_Friday);
        llDaysMap.put("Friday",FrLL);

        LinearLayout SaLL = findViewById(R.id.Demo1_LL_Saturday);
        llDaysMap.put("Saturday",SaLL);
    }

    private void initializeAndStoreCheckBoxes()
    {
        CheckBox Su = findViewById(R.id.Demo1_CB_Sunday);
        Su = setOnClickListener(Su);
        daysCBList.add(Su);

        CheckBox M = findViewById(R.id.Demo1_CB_Monday);
        M = setOnClickListener(M);
        daysCBList.add(M);

        CheckBox Tu = findViewById(R.id.Demo1_CB_Tuesday);
        Tu = setOnClickListener(Tu);
        daysCBList.add(Tu);

        CheckBox W = findViewById(R.id.Demo1_CB_Wednesday);
        W = setOnClickListener(W);
        daysCBList.add(W);

        CheckBox Th= findViewById(R.id.Demo1_CB_Thursday);
        Th = setOnClickListener(Th);
        daysCBList.add(Th);

        CheckBox F = findViewById(R.id.Demo1_CB_Friday);
        F = setOnClickListener(F);
        daysCBList.add(F);

        CheckBox Sa = findViewById(R.id.Demo1_CB_Saturday);
        Sa = setOnClickListener(Sa);
        daysCBList.add(Sa);

    }

    private CheckBox setOnClickListener(final CheckBox cb)
    {
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(cb.isChecked())
                {
                    addLayouts((LinearLayout) cb.getParent(), cb);
                }
                else
                {
                    LinearLayout linearLayout = (LinearLayout) cb.getParent();

                    int numViews = linearLayout.getChildCount();

                    for(int i = 1; i<numViews; i++)
                    {
                        linearLayout.removeViewAt(i);
                    }
                }

            }
        });

        return cb;
    }

    private void addLayouts(LinearLayout parent, CheckBox cb)
    {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        String Day = (String) cb.getText();

        LinearLayout FromLL = new LinearLayout(this);
        FromLL.setLayoutParams(params);

        LinearLayout ToLL = new LinearLayout(this);
        FromLL.setLayoutParams(params);

        LinearLayout inner = new LinearLayout(this);
        FromLL.setLayoutParams(params);
        inner.setOrientation(LinearLayout.VERTICAL);


        ArrayAdapter<String> Times = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.times_array));

        ArrayAdapter<String> AMorPM = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.am_pm_array));


        TextView From = new TextView(this);
        From.setLayoutParams(params);
        From.setText("From:");
        From.setTextSize(20);
        From.setPadding(5,0,0,0);

        Spinner FromTimeSpin = new Spinner(this);
        FromTimeSpin.setLayoutParams(params);
        FromTimeSpin.setAdapter(Times);


        Spinner FromAMorPMSpin = new Spinner(this);
        FromAMorPMSpin.setLayoutParams(params);
        FromAMorPMSpin.setAdapter(AMorPM);

        FromLL.addView(From);
        FromLL.addView(FromTimeSpin);
        FromLL.addView(FromAMorPMSpin);

        inner.addView(FromLL);

        TextView To = new TextView(this);
        To.setLayoutParams(params);
        To.setText("To:");
        To.setTextSize(20);
        To.setPadding(5,0,0,0);

        Spinner ToTimeSpin = new Spinner(this);
        ToTimeSpin.setLayoutParams(params);
        ToTimeSpin.setAdapter(Times);

        Spinner ToAMorPMSpin = new Spinner(this);
        ToAMorPMSpin.setLayoutParams(params);
        ToAMorPMSpin.setAdapter(AMorPM);

        ToLL.addView(To);
        ToLL.addView(ToTimeSpin);
        ToLL.addView(ToAMorPMSpin);
        inner.addView(ToLL);
        // The FromLL Contains 3 views, a TextView and two Spinners
        // referring to the Start Time
        //
        // The first Spinner is the Time (12:00-11:00)
        // The second Spinner is AM or PM
        //
        // This Linear Layout is used later to reference the Spinners
        // to grab the users selected Time
        llDaysMap.put(Day.concat("_Start"),FromLL);
        llDaysMap.put(Day.concat("_End"),ToLL);

        parent.addView(inner);
    }

    private boolean allFieldsEntered(String userEmail, String userName, String phoneNumber, String activityLevel)
    {
        return !(userEmail.isEmpty() || userName.isEmpty() || phoneNumber.isEmpty() || activityLevel.contains("Select"));
    }

    private boolean emailsMatch(String userEmail, String confirmEmail)
    {
        return userEmail.equals(confirmEmail);
    }

    private boolean validEmail(String userEmail)
    {
        return  userEmail.contains("@") && userEmail.contains(".com");
    }

    private void addUserClassValues(String userName, String userEmail,
                                    String phoneNumber, String activityLevel,
                                    List<String> days)
    {
        AppData.cur_user.setName(userName);
        AppData.cur_user.setEmail(userEmail);
        AppData.cur_user.setPhone(phoneNumber);
        AppData.cur_user.setActivityLevel(activityLevel);
    }

    private void switchPages()
    {
        Intent launchActivity1= new Intent(CKD.Android.Demographics_1.this, Demographics_3.class);
        startActivity(launchActivity1);
    }

}






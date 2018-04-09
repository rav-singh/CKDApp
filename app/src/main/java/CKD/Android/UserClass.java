package CKD.Android;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserClass
{
    private String name;
    private String email;
    private String phone;
    private String age;
    private String activityLevel;
    private String UID;
    private String marital;
    private String gender;
    private List<String> race;
    private List<String> coMorbs;
    private String password;
    private String education;
    private String workStatus;
    private String healthRating;
    private List<String> scheduledDays;
    //The Keys for this map are the Days, the Values are the Time in military Time (24:00)
    private Map<String,String> scheduledStartTimes;
    private Map<String,String> scheduledEndTimes;


    // TODO May need to instantiate all variables for login and grab data from Database using
    // TODO mAuth.getCurrentUser.getUID to access their information
    UserClass(String name, String email, String phone, String age, String activityLevel, String UID)
    {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.activityLevel =  activityLevel;
        this.UID = UID;
        this.race = new ArrayList<String>();
        this.scheduledEndTimes = new HashMap<>();
        this.scheduledStartTimes = new HashMap<>();
    }


    String getName(){return name;}
    String getEmail(){
        return email;
    }
    String getPhone(){
        return phone;
    }
    String getAge(){return age;}
    String getActivityLevel(){return activityLevel;}
    String getUID(){return UID;}
    String getMarital(){return marital;}
    String getGender(){return gender;}
    List<String> getRace(){return race;}
    List<String> getCoMorbs(){return coMorbs;}
    String getPassword(){return password;}
    String getWorkStatus(){return workStatus;}
    String getEducation(){return education;}
    String getHealthRating(){return healthRating;}
    List<String> getScheduledDays(){return scheduledDays;}
    Map<String,String> getScheduledStartTime(){return scheduledStartTimes;}
    Map<String,String> getScheduledEndTime(){return scheduledEndTimes;}

    void setName(String name)
    {
        this.name = name;
    }
    void setEmail(String email)
    {
        this.email = email;
    }
    void setPhone(String phone)
    {
        this.phone = phone;
    }
    void setAge(String age)
    {
        this.age = age;
    }
    void setActivityLevel(String activityLevel)
    {
        this.activityLevel = activityLevel;
    }
    void setUID(String UID)
    {
        this.UID = UID;
    }
    void setMarital(String marital) {this.marital = marital;}
    void setGender(String gender) {this.gender = gender;}
    void setRace (List<String> race) {this.race = race;}
    void setCoMorbs (List<String> coMorbs) {this.coMorbs = coMorbs;}
    void setPassword(String password) {this.password = password;}
    void setEducation(String education) {this.education = education;}
    void setWork(String work) {this.workStatus = work;}
    void setHealth(String health) {this.healthRating = health;}
    void setScheduledDays(List<String> scheduledDays){this.scheduledDays = scheduledDays;}
    void setScheduledStartTime(Map<String,String> scheduledStartTime){this.scheduledStartTimes = scheduledStartTime;}
    void setScheduledEndTime(Map<String,String> scheduledEndTime){this.scheduledEndTimes = scheduledEndTime;}

    void clearPassword(){this.password = "";}

}

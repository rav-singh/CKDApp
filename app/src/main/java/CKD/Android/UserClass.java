package CKD.Android;


import java.util.ArrayList;
import java.util.List;


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
    int getNumOfRaces(){return race.size();}        //Will be used for reading data
    int getNumOfCoMorbs(){return coMorbs.size();}   //Will be used for reading data
    String getPassword(){return password;}
    String getWorkStatus(){return workStatus;}
    String getEducation(){return education;}
    String getHealthRating(){return healthRating;}

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

    void clearPassword(){this.password = "";}

}

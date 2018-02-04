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
    private int numOfRaces;
    private String password;

    // TODO May need to instantiate all variables for login and grab data from Database using
    // TODO mAuth.getCurrentUser.getUID to access their information
    public UserClass(String name, String email, String phone, String age, String activityLevel, String UID )
    {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.activityLevel =  activityLevel;
        this.UID = UID;
        this.race = new ArrayList<String>();
    }


    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public String getPhone(){
        return phone;
    }

    public String getAge(){return age;}

    public String getActivityLevel(){return activityLevel;}

    public String getUID(){return UID;}

    public String getMarital(){return marital;}

    public String getGender(){return gender;}

    public List<String> getRace(){return race;}

    public int getNumOfRaces(){return numOfRaces;}

    public String getPassword(){return password;}

    public void setName(String name)
    {
        this.name = name;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public void setAge(String age)
    {
        this.age = age;
    }

    public void setActivityLevel(String activityLevel)
    {
        this.activityLevel = activityLevel;
    }

    public void setUID(String UID)
    {
        this.UID = UID;
    }

    public void setMarital(String marital) {this.marital = marital;}

    public void setGender(String gender) {this.gender = gender;}

    public void setRace (List<String> race) {this.race = race;}

    public void setPassword(String password) {this.password = password;}

    public void setNumOfRaces(int numOfRaces){this.numOfRaces = numOfRaces;}

    public void clearPassword(){this.password = "";}

}

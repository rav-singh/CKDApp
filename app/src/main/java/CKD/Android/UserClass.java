package CKD.Android;



public class UserClass
{

    private String Name;
    private String Email;
    private String Phone;
    private String UID;

    public UserClass()
    {

    }
    public void setName(String Name)
    {
        this.Name = Name;
    }

    public void setEmail(String Email){
        this.Email = Email;
    }

    public void setPhone(String Phone){
        this.Phone = Phone;
    }

    public void setUID(String UID){
        this.UID = UID;
    }

    public String getName(){
        return Name;
    }

    public String getEmail(){
        return Email;
    }

    public String getPhone(){
        return Phone;
    }

    public String UID(){
        return UID;
    }
}

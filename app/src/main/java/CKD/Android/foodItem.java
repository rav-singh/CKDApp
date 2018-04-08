package CKD.Android;


public class foodItem {
    private String name;
    private int ndbno;
    private int quantity;

    public foodItem() {

    }


    public foodItem(String name, int ndbno)
    {
        this.name = name;
        this.ndbno = ndbno;
    }

    public String getName() {
        return name;
    }

    public void setName(String author) {
        this.name = name;
    }

    public int getNdbno() {
        return ndbno;
    }

    public void setNdbno(int ndbno) {
        this.ndbno = ndbno;
    }

    public String toString() {
        return name;
    }
}
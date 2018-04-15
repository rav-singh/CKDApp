package CKD.Android;

import android.util.Log;

public class activityItem {
    private String name;
    private String duration;

    public activityItem(String name, String duration) {
        this.name = name;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return String.format("%-40s%40s", name, duration);
    }
}

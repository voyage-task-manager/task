package models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by felipe on 01/09/17.
 */

public class Task implements Parcelable {
    private String title;
    private String description;

    public Task (String title) {
        this.title = title;
    }

    protected Task(Parcel in) {
        title = in.readString();
        description = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public String toString () {
        return title;
    }

    public String getTitle () {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
    }
}

package models;

/**
 * Created by felipe on 01/09/17.
 */

public class Task {
    private String title;
    private String description;

    public Task (String title) {
        this.title = title;
    }

    public String toString () {
        return title;
    }
}

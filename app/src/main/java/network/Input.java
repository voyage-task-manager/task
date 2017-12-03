package network;

import android.content.Context;

import database.InputSchema;

/**
 * Created by zerodois on 02/12/17.
 */

public class Input {
    private int _day, _hour;
    private int value;
    private long _id;
    private final int dayN = 7;
    private final int hourN = 24;
    private InputSchema schema;
    private Context context;

    public Input(int day, int hour, int value) {
        setDay(day);
        setHour(hour);
        setValue(value);
    }

    private double[] toBinary (int n, int d) {
        double[] n2b = new double[d];
        int index = 0;
        int _n = n;
        while (n != 0) {
            n2b[index++] = (double)(n % 2);
            n = (int) Math.floor(n/2);
        }
        return n2b;
    }

    private double[] normalize (int day, int hour) {
        double[] d = toBinary(day, 3);
        double[] h = toBinary(hour, 5);
        double[] n = new double[]{ d[0], d[1], d[2], h[0], h[1], h[2], h[3], h[4] };
        return n;
    }


    public Input (Context context) {
        schema = new InputSchema(context);
        this.context = context;
    }

    public double[] toArray () {
        return normalize(_day, _hour);
    }
    public int getDay () {
        return this._day;
    }
    public int getHour () {
        return this._hour;
    }
    public void setDay(int day) {
        this._day = day;
    }
    public void setHour(int hour) {
        this._hour = hour;
    }
    public long getID () {
        return _id;
    }
    public void setID (long id) {
        this._id = id;
    }
    public int getValue () {
        return value;
    }
    public void setValue (int value) {
        this.value = value;
    }
    public boolean save () {
        return schema.record(this);
    }
}

package model;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by rajuk on 2017-05-09.
 */

public class Timeformot {
    Weather weather=new Weather();
    public String getTimeFormot(long Time){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Time);
        return DateFormat.format("HH:mm:ss a", cal).toString();
    }
}

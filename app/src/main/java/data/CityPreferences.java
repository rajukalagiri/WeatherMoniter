package data;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by rajuk on 2017-05-09.
 * im
 */

public class CityPreferences {
    private SharedPreferences pref;


    public CityPreferences(Activity activity) {
        pref = activity.getPreferences(Activity.MODE_PRIVATE);

    }

    public String getCityName() {
        return  pref.getString("city","Hyderabad,India") ;
    }

    public void setCityName(String city) {
        pref.edit().putString("city",city).apply();
    }
}
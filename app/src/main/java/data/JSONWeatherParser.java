package data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import Util.Utils;
import model.Place;
import model.Weather;

/**
 * Created by rajuk on 2017-04-27.
 */

public class JSONWeatherParser {
    public static Weather getWeather(String data){
        Weather weather=new Weather();
        try {
            JSONObject jsonObject= (JSONObject) new JSONTokener(data).nextValue();
            Place place=new Place();
            JSONObject coorObj= Utils.getObject("coord",jsonObject);
            place.setLat(Utils.getFloat("lat",coorObj));
            place.setLon(Utils.getFloat("lon",coorObj));
            JSONObject sysObj=Utils.getObject("sys",jsonObject);
            place.setCountry(Utils.getString("country",sysObj));
            place.setLastupdate(Utils.getInt("dt",jsonObject));
            place.setSunrise(Utils.getInt("sunrise",sysObj));
            place.setSuntset(Utils.getInt("sunset",sysObj));
            place.setCity(Utils.getString("name",jsonObject));
            weather.place=place;

            JSONArray jsonArray=jsonObject.getJSONArray("weather");
            JSONObject jsonWeather= jsonArray.getJSONObject(0);
            weather.currentCondition.setWeatherId(jsonWeather.getInt("id"));
            weather.currentCondition.setDescription(Utils.getString("description",jsonWeather));
            weather.currentCondition.setCondition(Utils.getString("main",jsonWeather));
            weather.currentCondition.setIcon(Utils.getString("icon",jsonWeather));


            JSONObject windObj=Utils.getObject("wind",jsonObject);
            weather.wind.setSpeed(Utils.getFloat("speed",windObj));
            weather.wind.setDeg(Utils.getFloat("deg",windObj));

            JSONObject mainObj=Utils.getObject("main",jsonObject);
            weather.currentCondition.setHumidity(Utils.getInt("humidity", mainObj));
            weather.currentCondition.setPressure(Utils.getInt("pressure",mainObj));
            weather.currentCondition.setMinTemp(Utils.getFloat("temp_min",mainObj));
            weather.currentCondition.setMaxTemp(Utils.getFloat("temp_max",mainObj));
            weather.currentCondition.setTemperature(Utils.getInt("temp",mainObj));

            JSONObject clodObj= Utils.getObject("clouds",jsonObject);
            weather.clouds.setPrecipitation(Utils.getInt("all",clodObj));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weather;
    }
}

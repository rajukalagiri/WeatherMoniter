package com.example.rajuk.weathermoniter;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import data.JSONWeatherParser;
import data.WeatherHttpClient;
import model.Weather;

public class MainActivity extends AppCompatActivity {
    private TextView cityName;
    private  TextView temp;
    private ImageView iconView;
    private  TextView description;
    private  TextView humidity;
    private TextView pressure;
    private  TextView wind;
    private TextView sunrise;
    private  TextView sunset;
    private TextView updated;

    Weather weather =new Weather();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName=(TextView) findViewById(R.id.citytText);
        iconView=(ImageView) findViewById(R.id.thumbnailIcon);
        description=(TextView) findViewById(R.id.cloudText);
        temp=(TextView) findViewById(R.id.tempText);
        humidity=(TextView) findViewById(R.id.humidText);
        pressure=(TextView) findViewById(R.id.pressureText);
        wind=(TextView) findViewById(R.id.windText);
        sunrise=(TextView) findViewById(R.id.riseText);
        sunset=(TextView) findViewById(R.id.setText);
        updated=(TextView) findViewById(R.id.updateText);

      renderWeatherData("Raikal,India");

    }
    public  void renderWeatherData(String city){
        WeatherTask weatherTask=new WeatherTask();
       weatherTask.execute((new String[]{city}));

    }
   private  class WeatherTask extends AsyncTask<String, Void, Weather> {


       @Override
       protected Weather doInBackground(String... params) {
          String data=((new WeatherHttpClient()).getWeatherData(params[0]));
           weather= JSONWeatherParser.getWeather(data);
           Log.v("Data: ", String.valueOf(weather.currentCondition.getHumidity()));
           return weather;
       }
       @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
       @Override
       protected void onPostExecute(Weather weather) {
           super.onPostExecute(weather);
           cityName.setText(weather.place.getCity()+"'"+weather.place.getCountry());
           temp.setText(""+weather.currentCondition.getTemperature()+"C");
           humidity.setText("Humidity: "+weather.currentCondition.getHumidity()+"%");
           pressure.setText("Pressure: "+weather.currentCondition.getPressure()+"hPa");
           wind.setText("Wind: "+weather.wind.getSpeed()+"mps");
           sunrise.setText("Sunrise: "+weather.place.getSunrise());
           sunset.setText("Sunset: "+weather.place.getSuntset());
           updated.setText("Last Updated: "+weather.place.getLastupdate());
           description.setText("Condition: "+weather.currentCondition.getCondition()+"("+weather.currentCondition.getDescription()+")");
       }

   }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.chande_cityId){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.example.rajuk.weathermoniter;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import Util.Utils;
import data.CityPreferences;
import data.JSONWeatherParser;
import data.WeatherHttpClient;
import model.Timeformot;
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
    private EditText location;
    Bitmap bmp = null;

    Weather weather =new Weather();
    Timeformot timeformot=new Timeformot();
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
            CityPreferences cityPreferences=new CityPreferences(MainActivity.this);
        renderWeatherData(cityPreferences.getCityName());

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
           Log.v("Data: ", String.valueOf(weather.currentCondition.getIcon()));
           Log.v("date:",timeformot.getTimeFormot(weather.place.getSunrise()));

           URL url = null;
           try {
               url = new URL(Utils.ICON_URL +String.valueOf(weather.currentCondition.getIcon())+".png");
               Log.v("url: ", url.toString());
           } catch (MalformedURLException e) {
               e.printStackTrace();
           }

           try {
               bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
           } catch (IOException e) {
               e.printStackTrace();
           }
           return weather;
       }
       @Override
       protected void onPostExecute(Weather weather) {
           super.onPostExecute(weather);

           cityName.setText(weather.place.getCity()+"'"+weather.place.getCountry());
           temp.setText(""+weather.currentCondition.getTemperature()+  (char) 0x00B0+"C");
           humidity.setText("Humidity: "+weather.currentCondition.getHumidity()+"%");
           pressure.setText("Pressure: "+weather.currentCondition.getPressure()+"hPa");
           wind.setText("Wind: "+weather.wind.getSpeed()+"mps");
           sunrise.setText("Sunrise: "+timeformot.getTimeFormot(weather.place.getSunrise()));
           sunset.setText("Sunset: "+timeformot.getTimeFormot(weather.place.getSuntset()));
           updated.setText("Last Updated: "+timeformot.getTimeFormot(weather.place.getLastupdate()));
           description.setText("Condition: "+weather.currentCondition.getCondition()+"("+weather.currentCondition.getDescription()+")");
           iconView.setImageBitmap(bmp);
       }

   }private  void showInputDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("change city");
        final EditText cityInput=new EditText(MainActivity.this);
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        cityInput.setHint("Raikal,India");
        builder.setView(cityInput);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CityPreferences cityPreferences=new CityPreferences(MainActivity.this);
                cityPreferences.setCityName(cityInput.getText().toString());
                String newCity=cityPreferences.getCityName();
                renderWeatherData(newCity);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.change_cityId){
           showInputDialog();
        }
        return super.onOptionsItemSelected(item);
    }

}

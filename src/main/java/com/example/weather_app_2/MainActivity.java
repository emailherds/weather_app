package com.example.weather_app_2;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.weather_app_2.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static java.nio.file.Paths.get;
public class MainActivity extends AppCompatActivity {
    JSONObject data;
    EditText editLat, editLong;
    Button get;
    String latitude;
    String longitude;
    String tempor1, tempor2;
    TextView temp1,temp2,temp3,name1,name2,name3,time1,time2,time3,date1,date2,date3,weather1,weather2,weather3;
    ImageView img1,img2,img3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        temp1 = findViewById(R.id.temp1);
        temp2 = findViewById(R.id.temp2);
        temp3 = findViewById(R.id.temp3);
        name1 = findViewById(R.id.name1);
        name2 = findViewById(R.id.name2);
        name3 = findViewById(R.id.name3);
        time1 = findViewById(R.id.time1);
        time2 = findViewById(R.id.time2);
        time3 = findViewById(R.id.time3);
        date1 = findViewById(R.id.date1);
        date2 = findViewById(R.id.date2);
        date3 = findViewById(R.id.date3);
        weather1 = findViewById(R.id.weather1);
        weather2 = findViewById(R.id.weather2);
        weather3 = findViewById(R.id.weather3);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        get = findViewById(R.id.get);
        editLat = findViewById(R.id.enterlat);
        editLong = findViewById(R.id.enterlong);

        longitude = "";
        latitude = "";

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempor1 = editLat.getText().toString();
                tempor2 = editLong.getText().toString();
                longitude = tempor2;
                latitude = tempor1;
                new AsyncThread().execute("bruh");
            }
        });

    }

    public class AsyncThread extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            longitude = tempor2;
            latitude = tempor1;
            String urlString = "http://api.openweathermap.org/data/2.5/find?lat="+latitude+"&lon="+longitude+"&cnt=3&appid=3e9f731ea0fcc41cd51f7e52f8e4d03a&units=imperial";
            Log.d("thing",urlString);
            try{
                URL url = new URL(urlString);
                URLConnection conn = url.openConnection();
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String weatherData = rd.readLine();

                Log.d("lol",weatherData+"no");

                data = new JSONObject(weatherData);
                String temp = data.getJSONArray("list").getJSONObject(0).getString("name");
                Log.d("lol", temp);

            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                name1.setText(data.getJSONArray("list").getJSONObject(0).getString("name"));
                name2.setText(data.getJSONArray("list").getJSONObject(1).getString("name"));
                name3.setText(data.getJSONArray("list").getJSONObject(2).getString("name"));

                temp1.setText(data.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("temp")+"°F");
                temp2.setText(data.getJSONArray("list").getJSONObject(1).getJSONObject("main").getString("temp")+"°F");
                temp3.setText(data.getJSONArray("list").getJSONObject(2).getJSONObject("main").getString("temp")+"°F");

                weather1.setText(data.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("description"));
                weather2.setText(data.getJSONArray("list").getJSONObject(1).getJSONArray("weather").getJSONObject(0).getString("description"));
                weather3.setText(data.getJSONArray("list").getJSONObject(2).getJSONArray("weather").getJSONObject(0).getString("description"));

                img1.setImageResource(getImage(data.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("icon")));
                img2.setImageResource(getImage(data.getJSONArray("list").getJSONObject(1).getJSONArray("weather").getJSONObject(0).getString("icon")));
                img3.setImageResource(getImage(data.getJSONArray("list").getJSONObject(2).getJSONArray("weather").getJSONObject(0).getString("icon")));

                date1.setText(getFormat("M/d/y", timeConverter(data.getJSONArray("list").getJSONObject(0).getInt("dt"))));
                date2.setText(getFormat("M/d/y", timeConverter(data.getJSONArray("list").getJSONObject(1).getInt("dt"))));
                date3.setText(getFormat("M/d/y", timeConverter(data.getJSONArray("list").getJSONObject(2).getInt("dt"))));

                time1.setText(getFormat("hh:mm a z", timeConverter(data.getJSONArray("list").getJSONObject(0).getInt("dt"))));
                time2.setText(getFormat("hh:mm a z", timeConverter(data.getJSONArray("list").getJSONObject(1).getInt("dt"))));
                time3.setText(getFormat("hh:mm a z", timeConverter(data.getJSONArray("list").getJSONObject(2).getInt("dt"))));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public Date timeConverter(int dt)
    {
        long unixTime = (long)dt;
        Date date = new Date(unixTime*1000);
        return date;
    }

    public String getFormat(String format, Date date)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        String dateformat = simpleDateFormat.format(date);
        return dateformat;
    }

    public int getImage(String icon)
    {
        int image = 0;

        switch(icon) {
            case "01d":
                image = R.drawable.clear_sky_day;
                break;
            case "01n":
                image = R.drawable.clear_sky_night;
                break;
            case "02d":
                image = R.drawable.few_clouds_day;
                break;
            case "02n":
                image = R.drawable.few_clouds_night;
                break;
            case "03d":
                image = R.drawable.scattered_clouds_day;
                break;
            case "03n":
                image = R.drawable.scattered_clouds_night;
                break;
            case "04d":
                image = R.drawable.broken_clouds_day;
                break;
            case "04n":
                image = R.drawable.broken_clouds_night;
                break;
            case "09d":
                image = R.drawable.rain_day;
                break;
            case "09n":
                image = R.drawable.rain_night;
                break;
            case "10d":
                image = R.drawable.rain_day;
                break;
            case "10n":
                image = R.drawable.rain_night;
                break;
            case "11d":
                image = R.drawable.thunderstorm_day;
                break;
            case "11n":
                image = R.drawable.thunderstorm_night;
                break;
            case "13d":
                image = R.drawable.snow_day;
                break;
            case "13n":
                image = R.drawable.snow_night;
            case "50d":
                image = R.drawable.mist_day;
                break;
            case "50n":
                image = R.drawable.mist_night;
            default:
                break;
        }
        return image;
    }

}
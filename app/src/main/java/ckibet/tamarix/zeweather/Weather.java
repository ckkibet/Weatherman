package ckibet.tamarix.zeweather;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;


public class Weather extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private TextView mTemperatureView;
    private TextView mHumidityView;
    private TextView mWindSpeedView;
    private TextView mPressureView;
    private TextView mCloudinessView;
    private TextView mSunriseView;
    private TextView mSunsetView;
    private TextView mIconWindView;
    private TextView mIconHumidityView;
    private TextView mIconPressureView;
    private TextView mIconSunriseView;
    private TextView mIconSunsetView;
    private String city, country;
    private long sunrise, sunset;

    private String mIconWind;
    private String mIconHumidity;
    private String mIconPressure;
    private String mIconSunrise;
    private String mIconSunset;

    private Toolbar mToolbar;
    private ActionBar mActionbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        mAuth = FirebaseAuth.getInstance();

        mTemperatureView = findViewById(R.id.main_temperature);
        mPressureView = findViewById(R.id.main_pressure);
        mHumidityView = findViewById(R.id.main_humidity);
        mWindSpeedView = findViewById(R.id.main_wind_speed);
        mCloudinessView = findViewById(R.id.main_cloudiness);
        mSunriseView = findViewById(R.id.main_sunrise);
        mSunsetView = findViewById(R.id.main_sunset);
        mIconSunsetView =  findViewById(R.id.main_sunset_icon);
        mIconWindView =  findViewById(R.id.main_wind_icon);
        mIconHumidityView =  findViewById(R.id.main_humidity_icon);
        mIconPressureView =  findViewById(R.id.main_pressure_icon);
        mIconSunriseView =  findViewById(R.id.main_sunrise_icon);
        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        mActionbar = getSupportActionBar();

        defaultWeather();
        weatherConditionsIcons();
        initializeTextView();
    }

    private void defaultWeather() {

        if (mActionbar != null){
            mActionbar.setDisplayHomeAsUpEnabled(false);
            mActionbar.setTitle(R.string.default_city);
        }

        mCloudinessView.setText(getString(R.string.desc));
        mTemperatureView.setText(String.format(getResources().getString(R.string.temperature), 21.11, getResources().getString(R.string.degrees_celcuis)));
        mWindSpeedView.setText(String.format(getResources().getString(R.string.wind), 7.73, getResources().getString(R.string.wind_speed_meters)));
        mHumidityView.setText(String.format(getResources().getString(R.string.humidity), 15, getResources().getString(R.string.percent_sign)));
        mPressureView.setText(String.format(getResources().getString(R.string.pressure), 1019.3, getResources().getString(R.string.pressure_measurement)));
        mSunriseView.setText(String.format(getResources().getString(R.string.sunrise) , "06:36"));
        mSunsetView.setText(String.format(getResources().getString(R.string.sunset) , "18:41"));
    }


    private void initializeTextView() {

        final Typeface weatherFont = ResourcesCompat.getFont(this,
                R.font.weathericonsregularwebfont);


        final Typeface robotoThin = ResourcesCompat.getFont(this,
                R.font.robotothin);


        final Typeface robotoLight = ResourcesCompat.getFont(this,
                R.font.robotolight);


        mTemperatureView.setTypeface(robotoThin);
        mWindSpeedView.setTypeface(robotoLight);
        mHumidityView.setTypeface(robotoLight);
        mPressureView.setTypeface(robotoLight);
        mCloudinessView.setTypeface(robotoLight);
        mSunriseView.setTypeface(robotoLight);;
        mSunsetView.setTypeface(robotoLight);


        /**
         * Initialize and configure weather icons
         */

        mIconWindView.setTypeface(weatherFont);
        mIconWindView.setText(mIconWind);

        mIconHumidityView.setTypeface(weatherFont);
        mIconHumidityView.setText(mIconHumidity);

        mIconPressureView.setTypeface(weatherFont);
        mIconPressureView.setText(mIconPressure);

        mIconSunriseView.setTypeface(weatherFont);
        mIconSunriseView.setText(mIconSunrise);
        mIconSunsetView.setTypeface(weatherFont);
        mIconSunsetView.setText(mIconSunset);

    }

    private void weatherConditionsIcons() {
        mIconWind = getString(R.string.icon_wind);
        mIconHumidity = getString(R.string.icon_humidity);
        mIconPressure = getString(R.string.icon_barometer);
        mIconSunrise = getString(R.string.icon_sunrise);
        mIconSunset = getString(R.string.icon_sunset);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.app_bar_search:
                inflateSearchDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void inflateSearchDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Enter City's Name");

        final EditText city_name = new EditText(this);
        city_name.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialog.setView(city_name);


        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String citys_name = city_name.getText().toString().replaceAll("\\s+", "%20");
                callLocationAPI(citys_name);
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void callLocationAPI(String citys_name) {
        RequestQueue location_queue = Volley.newRequestQueue(this);

        final String location_url = "https://us1.locationiq.com/v1/search.php?key=756c290bdfa6dd&q="+citys_name+"&format=json";

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Location Coordinates");
        progressDialog.show();
        JsonArrayRequest location_request = new JsonArrayRequest(Request.Method.GET,
                location_url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            JSONObject jsonObject = response.getJSONObject(0);
                            String latitude = jsonObject.getString("lat");
                            String longitude = jsonObject.getString("lon");

                            apiRequest(latitude, longitude);

                        } catch (JSONException e) {
                            System.out.println("Exception in getting location details" + e);
                        }
                    progressDialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Weather.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
        );

        location_queue.add(location_request);

    }


    private void apiRequest(String latitude, String longitude) {

        RequestQueue queue = Volley.newRequestQueue(this);

        final ProgressDialog anotherOne = new ProgressDialog(this);
        anotherOne.setMessage("Fetching Weather Details");
        anotherOne.show();

        final String url = "https://fcc-weather-api.glitch.me/api/current?lat="+latitude+"&lon="+longitude;
        
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                RecentDataModel recentDataModel;
                   try {
                       JSONArray array = response.getJSONArray("weather");
                       JSONObject jsonObject = array.getJSONObject(0);
                       String description = jsonObject.getString("description");

                       //returns city's name
                       city = response.getString("name");

                       //returns temperature, pressure and humidity
                       JSONObject main = response.getJSONObject("main");
                       Double temp = main.getDouble("temp");
                       Double pressure = main.getDouble("pressure");
                       int humidity = main.getInt("humidity");

                       //returns wind speed
                       JSONObject wind_speed = response.getJSONObject("wind");
                       Double speed = wind_speed.getDouble("speed");

                       //returns country initials
                       JSONObject sys = response.getJSONObject("sys");
                       country = sys.getString("country");
                       sunrise = sys.getLong("sunrise");
                       sunset = sys.getLong("sunset");



                       recentDataModel = new RecentDataModel(-1, temp, pressure, humidity, speed, country, description,  city);

                       DBhelper dBhelper = new DBhelper(Weather.this);
                       dBhelper.addRecord(recentDataModel);


                       mTemperatureView.setText(String.format(getResources().getString(R.string.temperature),
                               temp,
                               getResources().getString(R.string.degrees_celcuis )));

                       mCloudinessView.setText(description);

                       mWindSpeedView.setText(String.format(getResources().getString(R.string.wind),
                               speed,
                               getResources().getString(R.string.wind_speed_meters)));

                       mPressureView.setText(String.format(getResources().getString(R.string.pressure),
                               pressure,
                               getResources().getString(R.string.pressure_measurement)));

                       mHumidityView.setText(String.format(getResources().getString(R.string.humidity),
                               humidity,
                               getResources().getString(R.string.percent_sign)));

                       if (mActionbar != null){
                           mActionbar.setDisplayHomeAsUpEnabled(false);
                           mActionbar.setTitle(city+", "+country);
                       }

                       DateTimeFormatter dtf =
                               DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                                       .withLocale(Locale.UK)
                                       .withZone(ZoneId.systemDefault());


                       Instant instant = Instant.ofEpochSecond(sunrise);
                       String sunrise_output = dtf.format(instant);

                       mSunriseView.setText(String.format(getResources().getString(R.string.sunset),
                               sunrise_output));

                       DateTimeFormatter dtf_sunset =
                               DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
                                       .withLocale(Locale.UK)
                                       .withZone(ZoneId.systemDefault());


                       Instant isinstant = Instant.ofEpochSecond(sunset);
                       String sunset_output = dtf_sunset.format(isinstant);

                       mSunsetView.setText(String.format(getResources().getString(R.string.sunset),
                               sunset_output));



                   } catch (JSONException e) {
                       System.out.println("Exception in weather details" + e);
                   }
                   anotherOne.dismiss();
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error Response", "Error");
                anotherOne.dismiss();
            }
        });

        queue.add(getRequest);

    }


    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);    }
}

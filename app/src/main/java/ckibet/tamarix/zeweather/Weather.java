package ckibet.tamarix.zeweather;

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


public class Weather extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private TextView mIconWeatherView;
    private TextView mTemperatureView;
    private TextView mDescriptionView;
    private TextView mHumidityView;
    private TextView mWindSpeedView;
    private TextView mPressureView;
    private TextView mCloudinessView;
    private TextView mLastUpdateView;
    private TextView mSunriseView;
    private TextView mSunsetView;
    private TextView mIconWindView;
    private TextView mIconHumidityView;
    private TextView mIconPressureView;
    private TextView mIconCloudinessView;
    private TextView mIconSunriseView;
    private TextView mIconSunsetView;
    private String city, country;

    private String mSpeedScale;
    private String mIconWind;
    private String mIconHumidity;
    private String mIconPressure;
    private String mIconCloudiness;
    private String mIconSunrise;
    private String mIconSunset;
    private String mPercentSign;
    private String mPressureMeasurement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        mAuth = FirebaseAuth.getInstance();
        setupActionbar();
        weatherConditionsIcons();
        initializeTextView();

    }

    private void initializeTextView() {

        final Typeface weatherFont = ResourcesCompat.getFont(this,
                R.font.weathericonsregularwebfont);


        final Typeface robotoThin = ResourcesCompat.getFont(this,
                R.font.robotothin);


        final Typeface robotoLight = ResourcesCompat.getFont(this,
                R.font.robotolight);

        mTemperatureView = findViewById(R.id.main_temperature);
        mPressureView = findViewById(R.id.main_pressure);
        mHumidityView = findViewById(R.id.main_humidity);
        mWindSpeedView = findViewById(R.id.main_wind_speed);
        mCloudinessView = findViewById(R.id.main_cloudiness);
        mIconSunriseView = findViewById(R.id.main_sunrise);
        mIconSunsetView = findViewById(R.id.main_sunset);

        mTemperatureView.setTypeface(robotoThin);
        mWindSpeedView.setTypeface(robotoLight);
        mHumidityView.setTypeface(robotoLight);
        mPressureView.setTypeface(robotoLight);
        mCloudinessView.setTypeface(robotoLight);


        /**
         * Initialize and configure weather icons
         */
        mIconWindView = (TextView) findViewById(R.id.main_wind_icon);
        mIconWindView.setTypeface(weatherFont);
        mIconWindView.setText(mIconWind);
        mIconHumidityView = (TextView) findViewById(R.id.main_humidity_icon);
        mIconHumidityView.setTypeface(weatherFont);
        mIconHumidityView.setText(mIconHumidity);
        mIconPressureView = (TextView) findViewById(R.id.main_pressure_icon);
        mIconPressureView.setTypeface(weatherFont);
        mIconPressureView.setText(mIconPressure);
        mIconSunriseView = (TextView) findViewById(R.id.main_sunrise_icon);
        mIconSunriseView.setTypeface(weatherFont);
        mIconSunriseView.setText(mIconSunrise);
        mIconSunsetView = (TextView) findViewById(R.id.main_sunset_icon);
        mIconSunsetView.setTypeface(weatherFont);
        mIconSunsetView.setText(mIconSunset);

    }

    private void weatherConditionsIcons() {
        mIconWind = getString(R.string.icon_wind);
        mIconHumidity = getString(R.string.icon_humidity);
        mIconPressure = getString(R.string.icon_barometer);
        mIconCloudiness = getString(R.string.icon_cloudiness);
        mPercentSign = getString(R.string.percent_sign);
        mPressureMeasurement =getString(R.string.pressure_measurement);
        mIconSunrise = getString(R.string.icon_sunrise);
        mIconSunset = getString(R.string.icon_sunset);
    }

    private void setupActionbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.forgot_password:
                forgotPassword();
                return true;
            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            case R.id.app_bar_search:
                inflateSearchDialog();
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
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Weather.this, "things are burning", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        location_queue.add(location_request);

    }

    private void forgotPassword() {
    }


    private void apiRequest(String latitude, String longitude) {

        RequestQueue queue = Volley.newRequestQueue(this);

        final String url = "https://fcc-weather-api.glitch.me/api/current?lat="+latitude+"&lon="+longitude;
        System.out.println(url);

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

                       //returns temperature, feels_like, pressure and humidity
                       JSONObject main = response.getJSONObject("main");
                       Double temp = main.getDouble("temp");
                       Double feels_like = main.getDouble("feels_like");
                       Double pressure = main.getDouble("pressure");
                       int humidity = main.getInt("humidity");

                       //returns wind speed
                       JSONObject wind_speed = response.getJSONObject("wind");
                       Double speed = wind_speed.getDouble("speed");

                       //returns country initials
                       JSONObject sys = response.getJSONObject("sys");
                       country = sys.getString("country");

                       recentDataModel = new RecentDataModel(-1, temp, feels_like, pressure, humidity, speed, country, description,  city);

                       DBhelper dBhelper = new DBhelper(Weather.this);
                       boolean success = dBhelper.addRecord(recentDataModel);


                       String temp_string = Double.toString(temp);
                       mTemperatureView.setText(temp_string+"°C");

                       mCloudinessView.setText(description);
//                       m.setText(city);

//                       wind,pressure, humidity;
                       String weather_wind = Double.toString(speed);
                       mWindSpeedView.setText("Wind: "+weather_wind + getString(R.string.meter_per_second));

                       String weather_pressure = Double.toString(pressure);
                       mPressureView.setText("Pressure: "+weather_pressure+"hPa");

                       String weather_humidity = Double.toString(humidity);
                       mHumidityView.setText("Humidity: "+weather_humidity+mPercentSign);

                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error Response", "Error");
            }
        });

        queue.add(getRequest);

    }

    private void updateRecent(String description, String city, Double temp, Double feels_like, Double pressure, int humidity, Double speed, String contry) {



    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);    }
}
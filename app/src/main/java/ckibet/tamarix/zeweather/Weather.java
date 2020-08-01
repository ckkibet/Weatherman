package ckibet.tamarix.zeweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Weather extends AppCompatActivity {


    private FirebaseAuth mAuth;
    TextView temperature, description, city, wind,pressure, humidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        mAuth = FirebaseAuth.getInstance();
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
                       String city = response.getString("name");

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
                       String country = sys.getString("country");

                       recentDataModel = new RecentDataModel(-1, temp, feels_like, pressure, humidity, speed, country, description,  city);
                       Toast.makeText(Weather.this, recentDataModel.toString(), Toast.LENGTH_SHORT).show();

                       DBhelper dBhelper = new DBhelper(Weather.this);
                       boolean success = dBhelper.addRecord(recentDataModel);

                       Toast.makeText(Weather.this, "Success="+success, Toast.LENGTH_SHORT).show();

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


}
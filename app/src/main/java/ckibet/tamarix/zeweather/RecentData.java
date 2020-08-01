package ckibet.tamarix.zeweather;

public class RecentData {

    public static final String TABLE_NAME = "weather_data";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_WIND = "wind";
    public static final String COLUMN_HUMIDITY = "humidity";
    public static final String COLUMN_TEMP = "temperature";

    private int id;
    private String city;
    private String country;
    private float wind;
    private float humidity;
    private float temperature;


    public static final String CREATE_TABLE =
            "CREATE TABLE" + TABLE_NAME + "("
                    +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +COLUMN_TEMP+" FLOAT,"
                    +COLUMN_CITY+" TEXT,"
                    +COLUMN_COUNTRY+" TEXT,"
                    +COLUMN_WIND+" FLOAT"
                    +COLUMN_HUMIDITY+" FLOAT"
                    +")";

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public RecentData(int id, String city, String country, float wind, float humidity, float temperature) {
        this.id = id;
        this.city = city;
        this.country = country;
        this.wind = wind;
        this.humidity = humidity;
        this.temperature = temperature;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public float getWind() {
        return wind;
    }

    public void setWind(float wind) {
        this.wind = wind;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public RecentData(){

    }



}

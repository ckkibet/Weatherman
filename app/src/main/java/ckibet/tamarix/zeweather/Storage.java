package ckibet.tamarix.zeweather;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "storage")
public class Storage {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "city")
    private String city;

    @ColumnInfo(name = "country")
    private String country;

    @ColumnInfo(name = "main")
    private String main;

    @ColumnInfo(name = "wind")
    private Double wind;

    @ColumnInfo(name = "pressure")
    private Double pressure;

    @ColumnInfo(name = "humidity")
    private Double humidity;

    public int getId() {
        return id;
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

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public Double getWind() {
        return wind;
    }

    public void setWind(Double wind) {
        this.wind = wind;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Storage(String city, String country, String main, Double wind, Double pressure, Double humidity) {
        this.city = city;
        this.country = country;
        this.main = main;
        this.wind = wind;
        this.pressure = pressure;
        this.humidity = humidity;
    }




}

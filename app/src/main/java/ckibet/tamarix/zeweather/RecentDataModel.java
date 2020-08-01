package ckibet.tamarix.zeweather;

public class RecentDataModel {

    private  int id;
    private double temperature;
    private double feels_like;
    private double pressure;
    private int humidity;
    private double speed;
    private String country;
    private String description;
    private String city;

    public RecentDataModel(int id, double temperature, double feels_like, double pressure, int humidity, double speed, String country, String description, String city) {
        this.id = id;
        this.temperature = temperature;
        this.feels_like = feels_like;
        this.pressure = pressure;
        this.humidity = humidity;
        this.speed = speed;
        this.country = country;
        this.description = description;
        this.city = city;
    }

    public RecentDataModel() {
    }

    @Override
    public String toString() {
        return "RecentDataModel{" +
                "id=" + id +
                ", temperature=" + temperature +
                ", feels_like=" + feels_like +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", speed=" + speed +
                ", country='" + country + '\'' +
                ", description='" + description + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(double feels_like) {
        this.feels_like = feels_like;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

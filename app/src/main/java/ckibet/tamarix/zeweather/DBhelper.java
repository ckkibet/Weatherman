package ckibet.tamarix.zeweather;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBhelper extends SQLiteOpenHelper {
    public static final String RECENT_WEATHER = "RECENT_WEATHER";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_HUMIDITY = "HUM" + COLUMN_ID + "ITY";
    public static final String COLUMN_TEMPERATURE = "TEMPERATURE";
    public static final String COLUMN_FEELS_LIKE = "FEELS_LIKE";
    public static final String COLUMN_PRESSURE = "PRESSURE";
    public static final String COLUMN_SPEED = "SPEED";
    public static final String COLUMN_COUNTRY = "COUNTRY";
    public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_CITY = "CITY";

    public DBhelper(@Nullable Context context) {
        super(context, "recent_details.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String createTableStatement= "CREATE TABLE " + RECENT_WEATHER + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TEMPERATURE + " DOUBLE, " +
                COLUMN_FEELS_LIKE + " DOUBLE, " +
                COLUMN_PRESSURE + " DOUBLE, " +
                COLUMN_HUMIDITY + " INTEGER, " +
                COLUMN_SPEED + " DOUBLE, " +
                COLUMN_COUNTRY + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_CITY + " TEXT )";

        sqLiteDatabase.execSQL(createTableStatement);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addRecord (RecentDataModel recentDataModel){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TEMPERATURE, recentDataModel.getTemperature());
        contentValues.put(COLUMN_FEELS_LIKE, recentDataModel.getFeels_like());
        contentValues.put(COLUMN_PRESSURE, recentDataModel.getPressure());
        contentValues.put(COLUMN_HUMIDITY, recentDataModel.getHumidity());
        contentValues.put(COLUMN_SPEED, recentDataModel.getSpeed());
        contentValues.put(COLUMN_COUNTRY, recentDataModel.getCountry());
        contentValues.put(COLUMN_DESCRIPTION, recentDataModel.getDescription());
        contentValues.put(COLUMN_CITY, recentDataModel.getCity());

        long insert = sqLiteDatabase.insert(RECENT_WEATHER, null, contentValues);

        if (insert == -1){
            return false;
        }else
        return true;
    }
}

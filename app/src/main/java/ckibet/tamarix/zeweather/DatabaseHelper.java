package ckibet.tamarix.zeweather;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static  final String DATABASE_NAME = "recent_data_db";

    public DatabaseHelper(@Nullable Context context, @Nullable String DATABASE_NAME, @Nullable SQLiteDatabase.CursorFactory factory, int DATABASE_VERSION) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(RecentData.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecentData.TABLE_NAME);

        onCreate(sqLiteDatabase);

    }

    public long insertData(float temperature,String city, String country, float wind, float humidity){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(RecentData.COLUMN_CITY, city);
        values.put(RecentData.COLUMN_COUNTRY, country);
        values.put(RecentData.COLUMN_WIND, wind);
        values.put(RecentData.COLUMN_HUMIDITY, humidity);
        values.put(RecentData.COLUMN_TEMP, temperature);

        long id = sqLiteDatabase.insert(RecentData.TABLE_NAME, null, values);

        sqLiteDatabase.close();

        return id;
    }


}

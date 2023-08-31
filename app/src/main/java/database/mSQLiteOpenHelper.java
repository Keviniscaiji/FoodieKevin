package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class mSQLiteOpenHelper extends SQLiteOpenHelper {
    // create the database for program
    public static String sql1 = "create table Users (id integer primary key autoincrement, username varchar(20) unique, password varchar(20))";
    public static String sql2 = "create table Locations (Location_id integer primary key autoincrement, username varchar(20), location_name varchar(20), location varchar(20), FOREIGN KEY(username) REFERENCES Users(username))";

    public static String sql3 = "create table LocationsInfo (location_id varchar primary key,rate float, longitude double," +
            "latitude double, location_name varchar(20))";
    public static String sql4 = "create table Comments (comment_id integer primary key autoincrement,location_id varchar, " +
            "rate float,username varchar(20), location_name varchar(20), comment_title varchar(50), " +
            "comment varchar(1000), FOREIGN KEY(location_id) REFERENCES LocationsInfo(location_id), " +
            "FOREIGN KEY(username) REFERENCES Users(username))";
    public static String sql5 = "create table Routes (id integer primary key autoincrement, " +
            "title varchar(100),route_points varchar(10000),route_lines varchar(10000),route_notes varchar(10000))";

    public mSQLiteOpenHelper(@Nullable Context context, @Nullable String name,
                             @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sql1);
        sqLiteDatabase.execSQL(sql2);
        sqLiteDatabase.execSQL(sql3);
        sqLiteDatabase.execSQL(sql4);
        sqLiteDatabase.execSQL(sql5);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }
}


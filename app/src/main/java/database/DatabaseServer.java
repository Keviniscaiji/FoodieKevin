package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseServer {
    private SQLiteOpenHelper helper,helper2;
    private Context context;

    public DatabaseServer(Context context){
        this.context =context;
    }

// do some certain operation which can be directly accessed through use the methods inside the class
    public boolean checkUnique(String username){
        helper = new mSQLiteOpenHelper(context,"Users.db", null, 1);
        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        String sql = "select * from Users where username='" + username + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if(cursor.moveToNext()){
            cursor.close();
            return false;
        }
        return true;
    }
    public Boolean addRoute(String points,String lines,String notes,String title){
        try{
        helper = new mSQLiteOpenHelper(context,"Routes.db", null, 1);
        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        sqLiteDatabase.execSQL("insert into Routes(title,route_points, route_lines,route_notes) values(?,?,?,?)" , new Object[]{title,points,lines,notes});
        sqLiteDatabase.close();
        return true;
        }catch (Exception e){
            return false;
        }
    }
    public Cursor getRoute(){
        helper = new mSQLiteOpenHelper(context,"Routes.db", null, 1);
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        String sql = "select * from Routes";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        return cursor;
    }
    public boolean login(String username,String password){
        helper = new mSQLiteOpenHelper(context,"Users.db", null, 1);
        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        String sql = "select * from Users where username='" + username + "' and password='" + password + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if(cursor.moveToNext()){
            cursor.close();
            return true;
        }
        return false;
    }
    public void insertUser(String username, String password){
        helper = new mSQLiteOpenHelper(context,"Users.db", null, 1);
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        sqLiteDatabase.execSQL("insert into Users(username, password) values(?,?)" , new Object[]{username,password});
        sqLiteDatabase.close();
    }
    public void changePassWord(String username,String password){
        helper = new mSQLiteOpenHelper(context,"Users.db", null, 1);
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("password", password);
        sqLiteDatabase.update("Users", cv, "username = ?", new String[]{username});
    }
    public void addLocation(String username,String location_name, String location){
        helper = new mSQLiteOpenHelper(context,"Locations.db", null, 1);
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username",username);
        cv.put("location_name",location_name);
        cv.put("location",location);
        sqLiteDatabase.insertOrThrow("Locations", null, cv);
    }
    public Cursor getLocations(String username){
        helper = new mSQLiteOpenHelper(context,"Locations.db", null, 1);
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        String sql = "select * from Locations where username='" + username + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        return cursor;
    }
    public void removeLcation(int id){
        helper = new mSQLiteOpenHelper(context,"Locations.db", null, 1);
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from Locations where Location_id="+id);
    }
    public Boolean addComment(String lid,String ln,String un, String c, String t,float r){
        helper = new mSQLiteOpenHelper(context,"Comments.db", null, 1);
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        String sql = "select * from Comments where username='" + un + "' and location_id='" + lid + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if(cursor.moveToNext()){
            cursor.close();
            return false;
        }else{
            ContentValues cv = new ContentValues();
            cv.put("location_id",lid);
            cv.put("location_name",ln);
            cv.put("username",un);
            cv.put("comment_title",t);
            cv.put("location_id",lid);
            cv.put("comment",c);
            cv.put("rate",r);
            sqLiteDatabase.insertOrThrow("Comments", null, cv);
            return true;
        }

    }
    public void addLocationInfo(String lid,float r,double ln, double la, String l){
        ContentValues cv = new ContentValues();
        helper = new mSQLiteOpenHelper(context,"LocationsInfo.db", null, 1);
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();

        helper2 = new mSQLiteOpenHelper(context,"Comments.db", null, 1);
        SQLiteDatabase sqLiteDatabase2 = helper2.getWritableDatabase();

        String sql = "select * from LocationsInfo where location_id='" + lid + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
//        calculate the average score of the location
        if(cursor.moveToNext()){
            String sql2 = "select * from Comments where location_id='" + lid + "'";
            Cursor cursor2 = sqLiteDatabase2.rawQuery(sql2, null);
            float i = 0,j = 0,k = 3;
            while(cursor2.moveToNext()){
                j = j + cursor2.getFloat(cursor2.getColumnIndexOrThrow("rate"));
                i = i + 1;
                Log.e("Map", j+"");
                k = j/i;
            }
            cursor.close();
            cv.put("rate", k);
            sqLiteDatabase.update("LocationsInfo", cv, "location_id = ?", new String[]{lid});
            return;
        }else{
        cv.put("location_id",lid);
        cv.put("rate",r);
        cv.put("longitude",ln);
        cv.put("latitude",la);
        cv.put("location_name",l);
        sqLiteDatabase.insert("LocationsInfo", null, cv);
        Log.e("TAG", "addLocationInfo: success" );

    }
    }
    public Cursor getComments(String Lid){
        helper = new mSQLiteOpenHelper(context,"Comments.db", null, 1);
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        String sql = "select * from Comments where location_id='" + Lid + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        return cursor;
    }
    public Cursor getLocationInfo(String locationId){
        helper = new mSQLiteOpenHelper(context,"LocationsInfo.db", null, 1);
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        String sql = "select * from LocationsInfo where location_id='" + locationId + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        return cursor;
    }
    public Cursor getScaleLocation(double la, double lo, double scale){
        double s = scale;
        helper = new mSQLiteOpenHelper(context,"LocationsInfo.db", null, 1);
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
        double lamax = la + s;
        double lamin = la - s;
        double lomax = lo + s;
        double lomin = lo - s;
        String sql = "select * from LocationsInfo where longitude>="+lomin+" and longitude<="+lomax+" and latitude>="+lamin+" and latitude<="+lamax+"" ;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        return cursor;
    }
}


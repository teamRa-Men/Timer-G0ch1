package teamramen.cs103.yoobeecolleges.timergotchi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, "Timergochi.db", null, 1);
    }

    @Override
    //status 0 = current, 1 = removed;

    public void onCreate(SQLiteDatabase db) {
        String query = "create table Tasks(id integer primary key, taskName text, taskIndex integer, taskStatus integer)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion!=newVersion){
            db.execSQL("DROP TABLE IF EXISTS Tasks");
            onCreate(db);
        }
    }

    public void clearTasks(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Tasks");
        onCreate(db);
    }

    //user defined functions

    public long addTask( String taskName, int taskIndex)
    {
        ContentValues value = new ContentValues();

        value.put("taskName", taskName);
        value.put("taskIndex", taskIndex);
        value.put("taskStatus", 0);


        //opening the db into writable mode
        SQLiteDatabase db = this.getWritableDatabase();
        long l = db.insert("Tasks", null, value);

        db.close();
        return l;
    }



    public boolean updateData(long id, String newTaskName, int newIndex, int newStatus)
    {
        ContentValues value = new ContentValues();

        value.put("taskName", newTaskName);
        value.put("taskIndex", newIndex);
        value.put("taskStatus", newStatus);

        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println(id);
        long l = db.update("Tasks", value, "id=?",new String[] {""+id});
        db.close();
        if(l == 0) {
            return false;
        }else
        {
            return true;
        }
    }



/*
    public ArrayList<String> get(int i) {
        // String query = "select * from Person_Profile where firstname =firstname ='\"+first_name+\"' and lastname = '\"+last_name+\"'";
        String query = "select * from Tasks where taskIndex ='" + i + "' and taskStatus = '" + 0 + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        ArrayList<String> result = new ArrayList<String>();
        while (c.moveToNext()) {
            result.add(c.getString(0).toString() + "\t");
        }
        db.close();
        return result;
    }
*/

    public ArrayList<String> fetchCurrentNames() {
        String query = "select * from Tasks where taskStatus ='" + 0 + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<Integer>resultIndex = new ArrayList<Integer>();
        while (c.moveToNext()) {
            result.add(c.getString(1).toString());
        }
        db.close();
        return result;
    }
    public ArrayList<Integer> fetchCurrentIndices() {
        String query = "select * from Tasks where taskStatus ='" + 0 + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        ArrayList<Integer>resultIndices = new ArrayList<Integer>();
        while (c.moveToNext()) {

            resultIndices.add(c.getInt(2));
        }
        db.close();
        return resultIndices;
    }

    public ArrayList<Integer> fetchCurrentId() {
        String query = "select * from Tasks where taskStatus ='" + 0 + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        ArrayList<Integer>resultIndices = new ArrayList<Integer>();
        while (c.moveToNext()) {

            resultIndices.add(c.getInt(0));
        }
        db.close();
        return resultIndices;
    }



    //user defined method
    public ArrayList<String> OnViewAll() {
        // String query = "select * from Person_Profile where firstname =firstname ='\"+first_name+\"' and lastname = '\"+last_name+\"'";

        String query = "select * from Tasks";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(query, null);


        ArrayList<String> result = new ArrayList<String>();
        while (c.moveToNext()) {
            result.add(c.getString(0).toString() + "\t");
        }
        db.close();
        return result;
    }



}

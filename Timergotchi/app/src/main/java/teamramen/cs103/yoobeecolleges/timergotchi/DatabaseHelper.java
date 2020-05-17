package teamramen.cs103.yoobeecolleges.timergotchi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import teamramen.cs103.yoobeecolleges.timergotchi.lists.Task;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, "list.db", null, 1);
    }

    @Override
    //Tasks = morning, Tssks1 = afternoon, Tasks2 = evening
    //status 0 = current, 1 = removed;

    public void onCreate(SQLiteDatabase db) {
        //todo deadline, labels*?,repeat* 7
        String query = "create table Tasks(id integer primary key, taskName text, taskIndex integer, taskStatus integer, list integer, dueDate float, sun int, mon int, tue int, wed int, thu int, fri int, sat int)";
        String query1 = "create table Points(id integer primary key, points integer)";
        String query2 = "create table Lists(id integer primary key, listName text, listNum int)";


        db.execSQL(query);
        db.execSQL(query1);
        db.execSQL(query2);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion!=newVersion){
            db.execSQL("DROP TABLE IF EXISTS Tasks");
            db.execSQL("DROP TABLE IF EXISTS Points");

            onCreate(db);
        }

    }

    public void clearList(int list){
        SQLiteDatabase db = this.getReadableDatabase();

        db.delete("Tasks","list=?", new String[]{"" + list});

        db.close();
    }

    public void clearAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Tasks");
        db.execSQL("DROP TABLE IF EXISTS Points");

        onCreate(db);
    }
    //user defined functions



    public long addTask( String taskName, int taskIndex, int list)
    {
        ContentValues value = new ContentValues();

        value.put("taskName", taskName);
        value.put("taskIndex", taskIndex);
        value.put("taskStatus", 0);
        value.put("list", list);
        value.put("dueDate", 0);
        value.put("sun", 0);
        value.put("mon", 0);
        value.put("tue", 0);
        value.put("wed", 0);
        value.put("thu", 0);
        value.put("fri", 0);
        value.put("sat", 0);

        //opening the db into writable mode
        SQLiteDatabase db = this.getWritableDatabase();
        long l = 0;
        l = db.insert("Tasks", null, value);



        db.close();
        return l;
    }



    public boolean updateData(long id, String newTaskName, int newIndex, int newStatus,int newList, float newDueDate, int[] repeat)
    {
        ContentValues value = new ContentValues();

        value.put("taskName", newTaskName);
        value.put("taskIndex", newIndex);
        value.put("taskStatus", newStatus);
        value.put("list",newList);
        value.put("dueDate",newDueDate);
        value.put("sun", repeat[0]);
        value.put("mon", repeat[1]);
        value.put("tue", repeat[2]);
        value.put("wed", repeat[3]);
        value.put("thu", repeat[4]);
        value.put("fri", repeat[5]);
        value.put("sat", repeat[6]);

        SQLiteDatabase db = this.getWritableDatabase();

        long l = 0;


        l = db.update("Tasks", value, "id=?", new String[]{"" + id});


        db.close();

        if(l == 0) {
            return false;
        }else
        {
            return true;
        }
    }

    public boolean remove(long id)
    {
        ContentValues value = new ContentValues();

        value.put("taskStatus", Task.COMPLETED);

        SQLiteDatabase db = this.getWritableDatabase();

        long l = 0;


        l =  db.delete("Tasks","id=?", new String[]{"" + id});


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
//todo list<string[]{id,name,index,status,deadline,labels,repeat}>


    public ArrayList<Task> fetchTasks(int list) {
        String query = "";

            query = "select * from Tasks where list = '" + list + "'";




            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(query, null);
            ArrayList<Task> tasks = new ArrayList<Task>();

            int i = 0;
            while (c.moveToNext()) {
                int id = c.getInt(0);
                String name = c.getString(1);
                int index = c.getInt(2);
                int status = c.getInt(3);
                float dueDate = c.getFloat(4);
                int[] repeat = new int[]{c.getInt(5),c.getInt(6),c.getInt(7),c.getInt(8),c.getInt(9),c.getInt(10),c.getInt(11)};

                tasks.add(new Task(id,name,index,status,list,repeat, this));


                i++;

            }
           //
        // ////system.out.println(i+"db tasks");

            db.close();

        return tasks;
    }

    public int getPoints(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from 'Points' ",null);

        int pts =0;
        while (c.moveToNext()) {
            pts += c.getInt(1);
        }
        ////system.out.println(c.getCount()+"cursor");
        if(c.getCount()<1){
            //////system.out.println("no entries");
            ContentValues value = new ContentValues();
            value.put("points", 0);
            db.insert("Points", null, value);
        }

        db.close();
        return  pts;
    }
    public void setPoints(int points){
        ContentValues value = new ContentValues();
        value.put("points", points);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update("Points", value, "id=?", new String[]{"" + 1});
        db.close();
    }

}

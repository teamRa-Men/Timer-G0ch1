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

        //name status created dueTime dueDate smtwtfs 0123456
        String query = "create table Tasks(id integer primary key, " +
                "taskName text, " +
                "taskStatus float, " +
                "timeCreated float, "+
                "dueTime float, " +
                "dueDate float, " +
                "sun int, mon int, tue int, wed int, thu int, fri int, sat int, " +
                "label0 int, label1 int, label2 int, label3 int, label4 int, label5 int, label6 int)";



        String query1 = "create table Points(id integer primary key, points integer)";

        String query3 ="create table Backpack(id integer primary key, name text, image integer,type integer)";


        db.execSQL(query);
        db.execSQL(query1);

        db.execSQL(query3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion!=newVersion){
            db.execSQL("DROP TABLE IF EXISTS Tasks");
            db.execSQL("DROP TABLE IF EXISTS Points");

            onCreate(db);
        }

    }

    public void clearAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Tasks");
        db.execSQL("DROP TABLE IF EXISTS Points");

        onCreate(db);
    }
    //user defined functions


    //name status created dueTime dueDate smtwtfs 0123456
    public long addTask( String taskName, float timeCreated)
    {
        ContentValues value = new ContentValues();

        value.put("taskName", taskName);
        value.put("taskStatus", 0);
        value.put("timeCreated", timeCreated);
        value.put("dueTime", 0);
        value.put("dueDate", 0);
        value.put("sun", 0);
        value.put("mon", 0);
        value.put("tue", 0);
        value.put("wed", 0);
        value.put("thu", 0);
        value.put("fri", 0);
        value.put("sat", 0);
        value.put("label0", 0);
        value.put("label1", 0);
        value.put("label2", 0);
        value.put("label3", 0);
        value.put("label4", 0);
        value.put("label5", 0);
        value.put("label6", 0);

        //opening the db into writable mode
        SQLiteDatabase db = this.getWritableDatabase();
        long l = 0;
        l = db.insert("Tasks", null, value);



        db.close();
        return l;
    }


    //name status created dueTime dueDate smtwtfs 0123456
    public boolean updateData(long id, String newTaskName, float newStatus, float newDueTime, float newDueDate, int[] repeat, int[]labels)
    {
        ContentValues value = new ContentValues();

        value.put("taskName", newTaskName);
        value.put("taskStatus", newStatus);
        value.put("dueTime",newDueTime);
        value.put("dueDate",newDueDate);

        value.put("sun", repeat[0]);
        value.put("mon", repeat[1]);
        value.put("tue", repeat[2]);
        value.put("wed", repeat[3]);
        value.put("thu", repeat[4]);
        value.put("fri", repeat[5]);
        value.put("sat", repeat[6]);

        value.put("label0", labels[0]);
        value.put("label1", labels[1]);
        value.put("label2", labels[2]);
        value.put("label3", labels[3]);
        value.put("label4", labels[4]);
        value.put("label5", labels[5]);
        value.put("label6", labels[6]);

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

//todo list<string[]{id,name,index,status,deadline,labels,repeat}>


    public ArrayList<Task> fetchTasks() {
        String query = "";

            query = "select * from Tasks";




            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(query, null);
            ArrayList<Task> tasks = new ArrayList<Task>();

            int i = 0;

        //id name status created dueTime dueDate smtwtfs 0123456
            while (c.moveToNext()) {
                int id = c.getInt(0);
                String name = c.getString(1);
                float status = c.getFloat(2);
                float created = c.getFloat(3);
                int dueTime = c.getInt(4);
                float dueDate = c.getFloat(5);

                int[] repeat = new int[]{c.getInt(6),c.getInt(7),c.getInt(8),c.getInt(9),c.getInt(10),c.getInt(11),c.getInt(12)};

                int[] labels = new int[]{c.getInt(13),c.getInt(14),c.getInt(15),c.getInt(16),c.getInt(17),c.getInt(18),c.getInt(19)};
                tasks.add(new Task(id,name, status,created,dueTime,dueDate,repeat,labels, this));


                i++;

            }

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

    public ArrayList<Petitem> fetchBackpack() {
        String query = "";

        query = "select * from Backpack";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        ArrayList<Petitem> backpack = new ArrayList<Petitem>();

        int i = 0;
        while (c.moveToNext()) {

            String name = c.getString(1);
            int image = c.getInt(2);
            int id = c.getInt(0);
            int type = c.getInt(3);

            backpack.add(new Petitem(name,image,type,id));

            i++;

        }
        //
        // ////system.out.println(i+"db tasks");

        db.close();

        return backpack;
    }

    public long addPetitem( Petitem petitem)
    {
        ContentValues value = new ContentValues();


        value.put("name", petitem.name);
        value.put("image", petitem.image);
        value.put("type", petitem.type);
        //opening the db into writable mode
        SQLiteDatabase db = this.getWritableDatabase();

        long l = 0;
        l = db.insert("Backpack", null, value);

        db.close();
        return l;
    }

    public void removePetitem(int id)
    {

        //opening the db into writable mode
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("Backpack","id=?", new String[]{"" + id});
        db.close();
    }

}

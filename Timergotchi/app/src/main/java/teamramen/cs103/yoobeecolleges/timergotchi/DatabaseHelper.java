package teamramen.cs103.yoobeecolleges.timergotchi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import teamramen.cs103.yoobeecolleges.timergotchi.lists.Task;
import teamramen.cs103.yoobeecolleges.timergotchi.record.FinishedTask;

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
                "label0 int, label1 int, label2 int, label3 int, label4 int, label5 int, label6 int, i int, timeSpent float)";



        String query1 = "create table States(id integer primary key, points integer, affection float, health float, doingID int, petName text,lastUpdate float,backMod int, frontMod int, wearing int)";
        String query2 = "create table Labels (id integer primary key, l text, l0 text,l1 text,l2 text,l3 text,l4 text,l5 text)";
        String query3 ="create table Backpack(id integer primary key, name text, image integer,type integer, health integer, affection integer)";
        String query4 = "create table FinishedTasks(id integer primary key, name text, date float, dd int, mm int , yyyy int, timeSpent float)";

        db.execSQL(query);
        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
        db.execSQL(query4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion!=newVersion){
            db.execSQL("DROP TABLE IF EXISTS Tasks");
            db.execSQL("DROP TABLE IF EXISTS States");
            db.execSQL("DROP TABLE IF EXISTS Labels");
            db.execSQL("DROP TABLE IF EXISTS Backpack");
            db.execSQL("DROP TABLE IF EXISTS FinishedTasks");

            onCreate(db);
        }

    }

    public void clearAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Tasks");
        db.execSQL("DROP TABLE IF EXISTS States");
        db.execSQL("DROP TABLE IF EXISTS Labels");
        db.execSQL("DROP TABLE IF EXISTS Backpack");
        db.execSQL("DROP TABLE IF EXISTS FinishedTasks");

        onCreate(db);
    }
    //user defined functions

    //*********************************************************************************************//
    //Task data
    //*********************************************************************************************//
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
        value.put("i", 0);

        //opening the db into writable mode
        SQLiteDatabase db = this.getWritableDatabase();
        long l = 0;
        l = db.insert("Tasks", null, value);



        db.close();
        return l;
    }


    //name status created dueTime dueDate smtwtfs 0123456 timespent
    public boolean updateData(long id, String newTaskName, float newStatus, float newDueTime, float newDueDate, int[] repeat, int[]labels, int order, float timeSpent)
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

        value.put("i", order);
        value.put("timeSpent", timeSpent);

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

                int order = c.getInt(20);
                float timeSpent = c.getFloat(21);

                tasks.add(new Task(id,name, status,created,dueTime,dueDate,repeat,labels, order, timeSpent, this));


                i++;

            }

            db.close();

        return tasks;
    }

    //*********************************************************************************************//
    //Label data
    //*********************************************************************************************//

    public String[] getLabels(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from 'Labels' ",null);
        String[] l = new String[] {"","","","","","",""};
        while (c.moveToNext()) {
            l[0] = c.getString(1);
            l[1] = c.getString(2);
            l[2] = c.getString(3);
            l[3] = c.getString(4);
            l[4] = c.getString(5);
            l[5] = c.getString(6);
            l[6] = c.getString(7);
        }
        return l;
    }



    public void setLabels(String[] l){
        ContentValues value = new ContentValues();
        value.put("l", l[0]);
        value.put("l0", l[1]);
        value.put("l1", l[2]);
        value.put("l2", l[3]);
        value.put("l3", l[4]);
        value.put("l4", l[5]);
        value.put("l5", l[6]);


        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("Labels",null, value);
        db.close();
    }


    //*********************************************************************************************//
    //Finished tasks data
    //*********************************************************************************************//

    //id integer primary key, name text, date float, d int, m int , y int, timeSpent float
    public void setFinished(String name, float date, float time){
        Date finishedDate = new Date((long)date);

        ContentValues value = new ContentValues();
        value.put("name", name);
        value.put("date", date);
        value.put("dd", finishedDate.getDate());
        value.put("mm", finishedDate.getMonth()+1);
        value.put("yyyy", finishedDate.getYear()+1900);
        value.put("timeSpent", time);



        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("FinishedTasks",null, value);
        db.close();
    }

    public ArrayList<FinishedTask> getFinished(float minDate, float maxDate){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from 'FinishedTasks'",null);
        System.out.println(minDate + " " + maxDate + "dates");
        ArrayList<FinishedTask> finishedTasks = new ArrayList<FinishedTask>();
        while (c.moveToNext()) {
            String name = c.getString(1);
            float date = c.getFloat(2);


            float time = c.getFloat(6);

            if(date >= minDate && date <= maxDate+8.64e7) {
                finishedTasks.add(new FinishedTask(name, date, time));
            }
        }
        db.close();
        return  finishedTasks;
    }

    public int getTasksLeft() {
        String query = "";

        query = "select * from 'Tasks'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        int i = 0;
        while (c.moveToNext()) {
          i++;
        }
        db.close();
        return i;
    }


    //*********************************************************************************************//
    //State data
    //*********************************************************************************************//

    public int getPoints(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from 'States' ",null);

        int pts =0;
        while (c.moveToNext()) {
            pts += c.getInt(1);
        }
        ////system.out.println(c.getCount()+"cursor");
        if(c.getCount()<1){
            //////system.out.println("no entries");
            ContentValues value = new ContentValues();
            value.put("points", 0);

            db.insert("States", null, value);
        }

        db.close();
        return  pts;
    }

    public void setPoints(int points){
        ContentValues value = new ContentValues();
        value.put("points", points);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update("States", value, "id=?", new String[]{"" + 1});
        db.close();
    }

    public float getHealth(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from 'States' ",null);

        float health =0;
        while (c.moveToNext()) {
            health = c.getFloat(3);
        }
        System.out.println(health + " health database");
        ////system.out.println(c.getCount()+"cursor");
        if(c.getCount()<1){
            System.out.println("no entries");
            ContentValues value = new ContentValues();
            value.put("health", 100f);
            db.insert("States", null, value);
        }

        db.close();
        return  health;
    }
    public void setHealth(float health){
        ContentValues value = new ContentValues();
        value.put("health", health);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update("States", value, "id=?", new String[]{"" + 1});
        db.close();
    }

    public int getAffection(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from 'States' ",null);

        int affection =0;
        while (c.moveToNext()) {
            affection = c.getInt(2);
        }
        ////system.out.println(c.getCount()+"cursor");
        if(c.getCount()<1){
            //////system.out.println("no entries");
            ContentValues value = new ContentValues();
            value.put("affection", 50);
            db.insert("States", null, value);
        }

        db.close();
        return  affection;
    }
    public void setAffection(int affection){
        ContentValues value = new ContentValues();
        value.put("affection", affection);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update("States", value, "id=?", new String[]{"" + 1});
        db.close();
    }

    public Task getDoing(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from 'States' ",null);

        int doing =0;
        while (c.moveToNext()) {
            doing = c.getInt(4);
        }
        ////system.out.println(c.getCount()+"cursor");
        if(c.getCount()<1){
            //////system.out.println("no entries");
            ContentValues value = new ContentValues();
            value.put("doingID", 0);
            db.insert("States", null, value);

            System.out.println("none found");
        }
        System.out.println(doing + " found");

        Task doingTask = null;

        String query = "select * from 'Tasks' where id=?"; // where 'id' =" + doing; //bug commas
        c = db.rawQuery(query, new String[]{doing+""});


        //id name status created dueTime dueDate smtwtfs 0123456
        while (c.moveToNext()) {
            int id = c.getInt(0);
            System.out.println(id +" id");

            String name = c.getString(1);
            float status = c.getFloat(2);
            float created = c.getFloat(3);
            int dueTime = c.getInt(4);
            float dueDate = c.getFloat(5);


            int[] repeat = new int[]{c.getInt(6),c.getInt(7),c.getInt(8),c.getInt(9),c.getInt(10),c.getInt(11),c.getInt(12)};

            int[] labels = new int[]{c.getInt(13),c.getInt(14),c.getInt(15),c.getInt(16),c.getInt(17),c.getInt(18),c.getInt(19)};

            int order = c.getInt(20);
            float timeSpent = c.getFloat(21);

            doingTask = new Task(id,name, status,created,dueTime,dueDate,repeat,labels, order, timeSpent, this);
        }
        db.close();
        return  doingTask;
    }

    public void setDoing(int doing){
        ContentValues value = new ContentValues();
        value.put("doingID", doing);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update("States", value, "id=?", new String[]{"" + 1});
        db.close();
        System.out.println(doing + "doing");
    }

    public String getPetName(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from 'States' ",null);

        String name = "";
        while (c.moveToNext()) {
            name = c.getString(5);
        }
        ////system.out.println(c.getCount()+"cursor");
        if(c.getCount()<1){
            //////system.out.println("no entries");
            ContentValues value = new ContentValues();
            value.put("petName", 5);

            db.insert("States", null, value);
        }

        db.close();
        return  name;
    }

    public void setPetName(String name){
        ContentValues value = new ContentValues();
        value.put("petName", name);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update("States", value, "id=?", new String[]{"" + 1});
        db.close();
    }

    public float getLastUpdate(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from 'States' ",null);

        float lastUpdate = 0;
        while (c.moveToNext()) {
            lastUpdate = c.getFloat(6);
        }
        ////system.out.println(c.getCount()+"cursor");
        if(c.getCount()<1){
            System.out.println("no time update entries");
            ContentValues value = new ContentValues();
            value.put("lastUpdate",0);

            db.insert("States", null, value);
        }

        db.close();
        return  lastUpdate;
    }

    public void setLastUpdate(float updateTime){
        ContentValues value = new ContentValues();
        value.put("lastUpdate", updateTime);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update("States", value, "id=?", new String[]{"" + 1});
        db.close();
    }

    public int[] getPetMod(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from 'States' ",null);

        int[] mods = new int[]{0,0,0};
        while (c.moveToNext()) {
            mods[0] = c.getInt(7);
            mods[1] = c.getInt(8);
            mods[2] = c.getInt(9);
        }
        ////system.out.println(c.getCount()+"cursor");
        if(c.getCount()<1){
            ContentValues value = new ContentValues();
            value.put("backMod",0);
            value.put("frontMod",0);
            value.put("wearing",0);

            db.insert("States", null, value);
        }

        db.close();
        return  mods;
    }

    public void setPetMod(int[] mods){
        ContentValues value = new ContentValues();
        value.put("backMod", mods[0]);
        value.put("frontMod", mods[1]);
        value.put("wearing", mods[2]);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update("States", value, "id=?", new String[]{"" + 1});
        db.close();
    }

    //*********************************************************************************************//
    //Item data
    //*********************************************************************************************//

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
            int health = c.getInt(4);
            int affection = c.getInt(5);

            backpack.add(new Petitem(name,image,type,id,health,affection,0));

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
        value.put("health", petitem.health);
        value.put("affection", petitem.affection);

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

package teamramen.cs103.yoobeecolleges.timergotchi.record;

import java.util.Date;

public class FinishedTask {
    public String name;
    public int dayFin, monthFin, yearFin, dayOfWeek;
    public float timeSpent, dateFinished;
    public boolean past7Days;

    public FinishedTask(String name,float dateFinished, float timeSpent){
        this.dateFinished = dateFinished;
        this.name = name;


        Date due = new Date((long)dateFinished);

        this.dayFin = due.getDate()-1;
        this.monthFin = due.getMonth()+1;;
        this.yearFin = yearFin;
        this.timeSpent =  due.getYear();
        this.dayOfWeek = due.getDay();

        System.out.println(dayFin + " /" + monthFin + " /" + yearFin + " week " + dayOfWeek);

        past7Days = (dateFinished-System.currentTimeMillis()+5.99582088e13) < 8.64e+7;
    }
}

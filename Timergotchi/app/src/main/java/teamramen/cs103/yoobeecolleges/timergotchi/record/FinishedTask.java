package teamramen.cs103.yoobeecolleges.timergotchi.record;

import java.util.Date;

public class FinishedTask {
    public String name;
    public int dayFin, monthFin, yearFin, dayOfWeek;
    public float timeSpent, timeFinished, dateTimeFinished;
    public Date dateFinished;
    public boolean past7Days;

    public FinishedTask(String name,float timeFinished, float timeSpent){
        this.timeFinished = (float)(timeFinished);

        this.name = name;


        Date due = new Date((long)timeFinished);

        this.dayFin = due.getDate();
        this.monthFin = due.getMonth();;
        this.yearFin = due.getYear();
        this.timeSpent =  timeSpent;
        this.dayOfWeek = due.getDay();

        this.dateFinished = new Date();
        dateFinished.setDate(dayFin);
        dateFinished.setMonth(monthFin);
        dateFinished.setYear(yearFin);
        dateTimeFinished = dateFinished.getTime();
    }

    public String toString(){
        return(dayFin + " /" + (monthFin+1) + " /" + (yearFin+1900) + " day of the week " + getDay(dayOfWeek));
    }

    public String getDay(int i){
        switch (i){
            case 0: return "Sunday";
            case 1: return "Monday";
            case 2: return "Tuesday";
            case 3: return "Wednesday";
            case 4: return "Thursday";
            case 5: return "Friday";
            case 6: return "Saturday";

        }
        return "noday";
    }

    public boolean dateEquals(FinishedTask t){
        return (t.dayFin == dayFin & t.monthFin == monthFin & t.yearFin == yearFin);

    }
    public boolean dateEquals(Date d){
        return (d.getDate() == dayFin & d.getMonth() == monthFin & d.getYear() == yearFin);

    }
}

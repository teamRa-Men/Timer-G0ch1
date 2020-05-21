package teamramen.cs103.yoobeecolleges.timergotchi.record;

public class FinishedTask {
    public String name;
    public int dayFin, monthFin, yearFin;
    public float timeSpent;

    public FinishedTask(String name,int dayFIn,int monthFin,int yearFin, float timeSpent){
        this.name = name;
        this.dayFin = dayFin;
        this.monthFin = monthFin;
        this.yearFin = yearFin;
        this.timeSpent = timeSpent;
    }
}

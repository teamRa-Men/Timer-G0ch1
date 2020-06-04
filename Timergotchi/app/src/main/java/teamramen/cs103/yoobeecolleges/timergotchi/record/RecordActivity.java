package teamramen.cs103.yoobeecolleges.timergotchi.record;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import teamramen.cs103.yoobeecolleges.timergotchi.DatabaseHelper;
import teamramen.cs103.yoobeecolleges.timergotchi.R;
import teamramen.cs103.yoobeecolleges.timergotchi.pet.PetActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.shop.ShopActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.lists.ListsActivity;
import teamramen.cs103.yoobeecolleges.timergotchi.timer.TimerActivity;

public class RecordActivity extends AppCompatActivity {
    TextView finCount, points,showMin,showMax;
    TextView tasksLeft, timeSpent, recordTasksDone;
    ArrayList<FinishedTask> finishedTasks;
    DatabaseHelper db;
    CalendarView minCalendar, maxCalendar;
    View minCalendarPopup, maxCalendarPopup;
    long minDate, maxDate;
    BarChart taskPlot;

    int chartType = 0; //0=tasks done, 1= timespent
    int[] taskCount = new int[0];
    int[] timeData = new int[0];
    double[] dateCount;
    int maximumTask;
    int maximumTime;
    int totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        finCount = findViewById(R.id.fincount);
        points = findViewById(R.id.recordpoints);
        tasksLeft = findViewById(R.id.leftcount);
        recordTasksDone = findViewById(R.id.showmax);
        timeSpent = findViewById(R.id.totaltime);
        //timeSpent = findViewById(R.id.timespent);

        db = new DatabaseHelper(this);

        showMax = findViewById(R.id.showMaxDate);
        showMin = findViewById(R.id.showMinDate);

        minCalendarPopup = findViewById(R.id.minCalendarPopup);
        maxCalendarPopup = findViewById(R.id.maxCalendarPopup);

        minCalendar = findViewById(R.id.minCalendarView);
        maxCalendar = findViewById(R.id.maxCalendarView);

        setupMaxCalendar();
        setupMinCalendar();

        minDate = (long)(System.currentTimeMillis() - 8.64e7*7);
        maxDate = System.currentTimeMillis();

        maxCalendar.setDate(maxDate);
        minCalendar.setDate(minDate);

        showMin.setText("from " + dateToString(minDate));
        showMax.setText("to " + dateToString(maxDate));

        taskPlot = findViewById(R.id.taskplot);

        System.out.println(dateToString(minDate)+"min");
        System.out.println(dateToString(maxDate)+"max");

        taskPlot.getAxisLeft().setTextColor(this.getResources().getColor(R.color.textColor));
        taskPlot.getAxisRight().setEnabled(false);
        taskPlot.getXAxis().setTextColor(this.getResources().getColor(R.color.textColor));



        Description description = new Description();
        description.setText("");
        taskPlot.setDescription(description);
        taskPlot.setDrawValueAboveBar(true);


        taskPlot.getLegend().setEnabled(false);

        SetData();
        setCharts();
    }

    public void onSetMin(View view){
        minCalendarPopup.setVisibility(View.VISIBLE);
    }
    public void onMinOk(View view){
        minCalendarPopup.setVisibility(View.INVISIBLE);
        setCharts();
    }
    public void onSetMax(View view){
        maxCalendarPopup.setVisibility(View.VISIBLE);
    }
    public void onMaxOk(View view){
        maxCalendarPopup.setVisibility(View.INVISIBLE);
        setCharts();
    }

    public void onSwitchChart(View view){
        if(chartType == 0){
            chartType = 1;
        }
        else {
            chartType = 0;
        }

        setCharts();
    }



    public void toList(View view) {
        Intent i = new Intent(this, ListsActivity.class);
        startActivity(i);
    }

    public void toTimer(View view) {
        Intent i = new Intent(this, TimerActivity.class);
        startActivity(i);
    }

    public void toPet(View view) {
        Intent i = new Intent(this, PetActivity.class);
        startActivity(i);
    }

    public void toShop(View view) {
        Intent i = new Intent(this, ShopActivity.class);
        startActivity(i);
    }

    void SetData(){
        finishedTasks = db.getFinished(minDate,maxDate);

        if(finishedTasks.size()>0) {
            int finished = 0;
            for (int i = 0; i < finishedTasks.size(); i++) {
                System.out.println(finishedTasks.get(i).name);
                finished++;
            }

            //show tasks left and tasks done
            String s = finished + "";
            if (finished == 1) {
                s += " task done";
            } else {
                s += " tasks done";
            }
            finCount.setText(s);


            s = db.getTasksLeft() + "";
            if (db.getTasksLeft() == 1) {
                s += " task left";
            } else {
                s += " tasks left";
            }
            tasksLeft.setText(s);

            points.setText(db.getPoints() + " ");


            //tally task and time data
            double current = finishedTasks.get(0).dateTimeFinished;
            double last = finishedTasks.get(finishedTasks.size() - 1).timeFinished;


            int timePeriod = (int) Math.round((last - current) / 8.64e7 + 1);

            taskCount = new int[timePeriod];
            timeData = new int[timePeriod];

            dateCount = new double[timePeriod];

            //get dates
            for (int i = 0; i < timePeriod; i++) {
                dateCount[i] = current + i * 8.64e7;
            }


            for (int i = 0; i < timePeriod; i++) {
                for (int j = 0; j < finishedTasks.size(); j++) {

                    if (finishedTasks.get(j).dateEquals(new Date((long) dateCount[i]))) {
                        taskCount[i]++;
                        timeData[i] += (int)(finishedTasks.get(j).timeSpent/1000); //tally time spent in minutes
                    }
                }
            }

            maximumTask = 0;
            maximumTime=0;
            totalTime = 0;

            for (int i = 0; i < timePeriod; i++) {
                if (taskCount[i] > maximumTask) {
                    maximumTask = taskCount[i];
                }
                if (timeData[i] > maximumTime) {
                    maximumTime = timeData[i];
                }
                totalTime += timeData[i];
            }
            recordTasksDone.setText("record: " +maximumTask+" done");
            timeSpent.setText((totalTime/60) + " mins used");
        }
    }

    void setCharts(){
        TextView title = findViewById(R.id.charttitle);
        if(chartType == 0){
            title.setText("Tasks Done per Day");
        }
        else{
            title.setText("Time Spent (min) per Day");
        }

        if(taskCount.length>0) {
            taskPlot.setVisibility(View.VISIBLE);
            List<BarEntry> entries = new ArrayList<BarEntry>();


            for (int i = 0; i < taskCount.length; i++) {
                Date d = new Date((long) dateCount[i]);
                if (chartType == 0) {
                    entries.add(new BarEntry(i, taskCount[i]));
                } else {
                    entries.add(new BarEntry(i, (float)(timeData[i])/60));
                }
            }
            //System.out.println(maximum + "max");
            BarDataSet set = new BarDataSet(entries, "BarDataSet");
            set.setColor(this.getResources().getColor(R.color.colorPrimary));
            set.setDrawValues(false);
            BarData data = new BarData(set);
            data.setBarWidth(0.5f);

            taskPlot.setData(data);
            taskPlot.notifyDataSetChanged();
            taskPlot.setFitBars(true);


            final String[] labelString = new String[taskCount.length];
            for (int i = 0; i < taskCount.length; i++) {
                Date d = new Date((long) dateCount[i]);
                labelString[i] = d.getDate() + "/" + (d.getMonth() + 1);
            }

            ValueFormatter formatter = new ValueFormatter() {

                public String getAxisLabel(float value, AxisBase axis) {
                    try {
                        return labelString[(int) value];
                    } catch (Exception e) {
                        return "";
                    }
                }
            };

            XAxis xAxis = taskPlot.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setValueFormatter(formatter);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(true);
            xAxis.setDrawAxisLine(false);

            taskPlot.getAxisLeft().setAxisMinimum(0);
            taskPlot.getAxisLeft().setGranularity(1);
            taskPlot.getAxisLeft().setDrawGridLines(true);
            taskPlot.getAxisLeft().setDrawZeroLine(false);
            taskPlot.getAxisLeft().setDrawAxisLine(true);

            taskPlot.invalidate();
        }
        else{
            taskPlot.setVisibility(View.INVISIBLE);
        }
    }



    void setupMinCalendar() {

        minCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                Date d = new Date();
                d.setDate(dayOfMonth);
                d.setMonth(month);
                d.setYear(year-1900);
                if(d.getTime()<maxDate) {
                    minDate = d.getTime();
                }else{
                    minDate = (long)(maxDate-8.64e7);
                }
                showMin.setText("from "+dateToString(minDate));

            }
        });
    }

    void setupMaxCalendar() {

        maxCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Date d = new Date();
                d.setDate(dayOfMonth);
                d.setMonth(month);
                d.setYear(year-1900);
                if(d.getTime()> minDate) {
                    maxDate = d.getTime();
                }else{
                    maxDate = (long)(minDate+8.64e7);
                }


                showMax.setText("to "+dateToString(maxDate));

            }
        });
    }

    String dateToString(long date){
        Date d = new Date(date);
        String s = d.getDate() + " " + getMonth(d.getMonth()) + " " + (d.getYear()+1900);
        return  s;
    }

    String getMonth(int i){
        String s = "";
        switch (i) {
            case 0:
                s += "Jan";
                break;
            case 1:
                s += "Feb";
                break;
            case 2:
                s += "Mar";
                break;
            case 3:
                s += "Apr";
                break;
            case 4:
                s += "May";
                break;
            case 5:
                s += "Jun";
                break;
            case 6:
                s += "Jul";
                break;
            case 7:
                s += "Aug";
                break;
            case 8:
                s += "Sep";
                break;
            case 9:
                s += "Oct";
                break;
            case 10:
                s += "Nov";
                break;
            case 11:
                s += "Dec";
                break;
        }
        return  s;
    }
    public void clearData(View view){
        db.clearAll();
    }
}

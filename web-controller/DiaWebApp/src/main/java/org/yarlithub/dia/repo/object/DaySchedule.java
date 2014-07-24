package org.yarlithub.dia.repo.object;

import java.text.DateFormatSymbols;
import java.util.List;

/**
 * Created by john on 7/24/14.
 */
public class DaySchedule{
    String day;
    List<Schedule> daySchedule;

    DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
    String[] weekdays = dateFormatSymbols.getWeekdays();

    public DaySchedule(List<Schedule> daySchedule,int n) {
        this.daySchedule = daySchedule;
        day= weekdays[n];
    }

    public List<Schedule> getDaySchedule() { return daySchedule;}

    public void setDaySchedule(List<Schedule> daySchedule) { this.daySchedule = daySchedule;}

    public String getDay() {return day;}

    public void setDay(String day) {this.day = day;}
}

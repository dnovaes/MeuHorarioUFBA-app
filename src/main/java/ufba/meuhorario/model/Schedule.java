package ufba.meuhorario.model;

import android.content.Intent;
import android.util.Log;

/**
 * Created by Diego Novaes on 17/03/2017.
 */

public class Schedule {
    Long id;
    Long day;
    Long startHour;
    Long startMin;
    Long endHour;
    Long endMin;
    String profName;
    Long disciplineClassId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDay() {
        return day;
        /*Integer intDay = Integer.valueOf(String.valueOf(day));
        switch (intDay){
            case 1:
                return "SEG";
            case 2:
                return "TER";
            case 3:
                return "QUA";
            case 4:
                return "QUI";
            case 5:
                return "SEX";
            case 6:
                return "SAB";
            case 7:
                return "DOM";
            default:
                //case 0 or other (im)possible choices...
                Log.e("Schedule", "value of getDay: "+day);
                return "CMB";
        }*/
    }

    public void setDay(Long day) {
        this.day = day;
    }

    public Long getStartHour() {
        return startHour;
    }

    public void setStartHour(Long startHour) {
        this.startHour = startHour;
    }

    public Long getStartMin() {
        return startMin;
    }

    public void setStartMin(Long startMin) {
        this.startMin = startMin;
    }

    public Long getEndHour() {
        return endHour;
    }

    public void setEndHour(Long endHour) {
        this.endHour = endHour;
    }

    public Long getEndMin() {
        return endMin;
    }

    public void setEndMin(Long endMin) {
        this.endMin = endMin;
    }

    public Long getDisciplineClassId() {
        return disciplineClassId;
    }

    public void setDisciplineClassId(Long disciplineClassId) {
        this.disciplineClassId = disciplineClassId;
    }

    public String getProfName() {
        return profName;
    }

    public void setProfName(String profName) {
        this.profName = profName;
    }

    public String getDayString(Long day) {
        switch (String.valueOf(day)) {
            case "1":
                return "SEG";
            case "2":
                return "TER";
            case "3":
                return "QUA";
            case "4":
                return "QUI";
            case "5":
                return "SEX";
            case "6":
                return "SAB";
            case "7":
                return "DOM";
            default:
                return "CMB";
        }
    }
}

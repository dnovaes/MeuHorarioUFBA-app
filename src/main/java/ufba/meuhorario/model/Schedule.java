package ufba.meuhorario.model;

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
    Long profName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDay() {

        Integer dayInt = day.intValue();

        switch (dayInt){
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
                return "CMB";
        }
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

    public Long getProfName() {
        return profName;
    }

    public void setProfName(Long profName) {
        this.profName = profName;
    }
}

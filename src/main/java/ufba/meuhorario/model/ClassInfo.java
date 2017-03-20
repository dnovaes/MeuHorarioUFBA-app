package ufba.meuhorario.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Diego Novaes on 28/02/2017.
 */
public class ClassInfo {
    // It's actually a mix of DisciplineClass, DisciplineClassOffer, ProfessorSchedule, Schedule and Professors
    // It's a class for DisciplineClass with the other variables.

    Long disciplineClassId;
    String classNumber;
    Long vacancy;
    List<Schedule> scheduleList;


    public void setDisciplineClassId(Long disciplineClassId) {
        this.disciplineClassId = disciplineClassId;
    }

    public Long getDisciplineClassId() {
        return disciplineClassId;
    }

    public String getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(String classNumber) {
        this.classNumber = classNumber;
    }

    public Long getVacancy() {
        return vacancy;
    }

    public void setVacancy(Long vacancy) {
        this.vacancy = vacancy;
    }

    public List<Schedule> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }
}

package ufba.meuhorario.model;

/**
 * Created by Diego Novaes on 17/03/2017.
 */

public class ProfessorSchedule {
    Long id;
    Long schedule_id;
    Long professor_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(Long schedule_id) {
        this.schedule_id = schedule_id;
    }

    public Long getProfessor_id() {
        return professor_id;
    }

    public void setProfessor_id(Long professor_id) {
        this.professor_id = professor_id;
    }
}

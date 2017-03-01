package ufba.meuhorario.model;

/**
 * Created by Diego Novaes on 28/02/2017.
 */
public class DisciplineCourse {
    Long id;
    Long semester;
    String nature;
    Long course_id;
    Long discipline_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSemester() {
        return semester;
    }

    public void setSemester(Long semester) {
        this.semester = semester;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public Long getCourse_id() {
        return course_id;
    }

    public void setCourse_id(Long course_id) {
        this.course_id = course_id;
    }

    public Long getDiscipline_id() {
        return discipline_id;
    }

    public void setDiscipline_id(Long discipline_id) {
        this.discipline_id = discipline_id;
    }
}

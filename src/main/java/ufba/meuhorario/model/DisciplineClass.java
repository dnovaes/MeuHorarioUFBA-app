package ufba.meuhorario.model;

import java.util.List;

/**
 * Created by Diego Novaes on 28/02/2017.
 */
public class DisciplineClass {

    Long id;
    Long discipline_id;
    String class_number;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDiscipline_id() {
        return discipline_id;
    }

    public void setDiscipline_id(Long discipline_id) {
        this.discipline_id = discipline_id;
    }

    public String getClass_number() {
        return class_number;
    }

    public void setClass_number(String class_number) {
        this.class_number = class_number;
    }

}

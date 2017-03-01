package ufba.meuhorario.model;

import java.io.Serializable;

/**
 * Created by Diego Novaes on 26/02/2017.
 */
public class Course implements Serializable{
    //" (id INTEGER PRIMARY KEY, name TEXT NOT NULL, code INTEGER, curriculum INTEGER, area_id INTEGER)"
    Long id;
    String name;
    Long code;
    Long curriculum;
    Long area_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Long getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(Long curriculum) {
        this.curriculum = curriculum;
    }

    public Long getArea_id() {
        return area_id;
    }

    public void setArea_id(Long area_id) {
        this.area_id = area_id;
    }

}

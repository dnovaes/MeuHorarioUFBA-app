package ufba.meuhorario.model;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Created by Diego Novaes on 24/02/2017.
 */

public class Profile implements Serializable{
    Long id;
    String name;
    String nmatricula;
    String courseyearcurriculum;
    String semester;
    String course;
    String imageurl;

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

    public String getNmatricula() {
        return nmatricula;
    }

    public void setNmatricula(String nmatricula) {
        this.nmatricula = nmatricula;
    }

    public String getCourseyearcurriculum() {
        return courseyearcurriculum;
    }

    public void setCourseyearcurriculum(String courseyearcurriculum) {
        this.courseyearcurriculum = courseyearcurriculum;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public boolean checkNull() throws IllegalAccessException {
        for (Field f : getClass().getDeclaredFields())
            if (f.get(this) != null)
                return false;
        return true;
    }
}

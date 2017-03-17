package ufba.meuhorario.model;

import java.io.Serializable;

/**
 * Created by Diego Novaes on 28/02/2017.
 */
public class Discipline implements Serializable{
    Long id;
    String code;
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

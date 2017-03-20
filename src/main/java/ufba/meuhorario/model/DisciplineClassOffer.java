package ufba.meuhorario.model;

/**
 * Created by Diego Novaes on 28/02/2017.
 */
public class DisciplineClassOffer {

    Long id;
    Long discipline_class_id;
    Long vacancies;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDiscipline_class_id() {
        return discipline_class_id;
    }

    public void setDiscipline_class_id(Long discipline_class_id) {
        this.discipline_class_id = discipline_class_id;
    }

    public Long getVacancies() {
        return vacancies;
    }

    public void setVacancies(Long vacancies) {
        this.vacancies = vacancies;
    }

}

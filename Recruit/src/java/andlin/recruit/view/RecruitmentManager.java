/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.view;

import andlin.recruit.controller.RecruitmentController;
import andlin.recruit.model.dto.CompetenceDTO;
import andlin.recruit.model.dto.PersonDTO;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.inject.Named;

/**
 *
 * @author pinballmilitia
 */
@Named(value = "recruitmentManager")
@SessionScoped
public class RecruitmentManager implements Serializable {

    @EJB
    private RecruitmentController recruitmentController;
    private DataModel<PersonDTO> persons;
    private PersonDTO currentPerson;
    //Search variables
    private String name;
    private List<CompetenceDTO> competences;
    private CompetenceDTO competence;

    /**
     * Creates a new instance of TestManager
     */
    public RecruitmentManager() {
    }

    @PostConstruct
    public void init() {
        competences = recruitmentController.getCompetences();
    }

    public void find() {
        if (name == null) {
            persons = recruitmentController.find();
        } else {
            persons = recruitmentController.find(name);
        }
    }

    /**
     * Allows caller to view all details of application
     *
     * @return
     */
    public String show() {
        currentPerson = persons.getRowData();
        return "success";
    }

    /**
     * Accept application
     */
    public void accept() {
        recruitmentController.accept(currentPerson);
    }

    /**
     * Reject application
     */
    public void reject() {
        recruitmentController.reject(currentPerson);
    }

    public DataModel<PersonDTO> getPersons() {
        return persons;
    }

    public void setPersons(DataModel<PersonDTO> persons) {
        this.persons = persons;
    }

    public PersonDTO getCurrentPerson() {
        return currentPerson;
    }

    public void setCurrentPerson(PersonDTO currentPerson) {
        this.currentPerson = currentPerson;
    }

    public CompetenceDTO getCompetence() {
        return competence;
    }

    public void setCompetence(CompetenceDTO competence) {
        this.competence = competence;
    }

    public List<CompetenceDTO> getCompetences() {
        return competences;
    }

    public void setCompetences(List<CompetenceDTO> competences) {
        this.competences = competences;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

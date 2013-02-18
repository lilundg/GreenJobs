/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.view;

import andlin.recruit.controller.RecruitmentController;
import andlin.recruit.model.dto.CompetenceDTO;
import andlin.recruit.model.dto.PersonDTO;
import andlin.recruit.model.dto.RecruitmentQueryDTO;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.inject.Named;

/**
 * This backing bean forwards calls from client to controller
 */
@Named(value = "recruitmentManager")
@SessionScoped
public class RecruitmentManager implements Serializable {

    @EJB
    private RecruitmentController recruitmentController;
    private DataModel<PersonDTO> persons;
    private PersonDTO currentPerson;
    private List<CompetenceDTO> competences;
    private Date fromDate;
    private Date toDate;
    //Search variables
    private String firstName;
    private String surName;
    private CompetenceDTO competence;

    /**
     * Creates a new instance of TestManager
     */
    public RecruitmentManager() {
    }

    /**
     * Fetches all available competences
     */
    @PostConstruct
    public void init() {
        competences = recruitmentController.getCompetences();
    }

    
    /**
     * 
     */
    public void search(){
        persons = recruitmentController.search(new RecruitmentQueryDTO(firstName, surName, fromDate, toDate, competence));
    }

    /**
     * Allows caller to view all details of application
     *
     * @return 'success'....always
     */
    public String show() {
        currentPerson = persons.getRowData();
        return "success";
    }

    /**
     * Accept application of currentPerson
     */
    public void accept() {
        recruitmentController.accept(currentPerson);
    }

    /**
     * Reject application of currentPerson
     */
    public void reject() {
        recruitmentController.reject(currentPerson);
    }

    
    /***********************
     * GETTERS AND SETTERS *
     ***********************/
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
    
    
}

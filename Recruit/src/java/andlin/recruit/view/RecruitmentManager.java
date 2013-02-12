/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.view;

import andlin.recruit.controller.RecruitmentController;
import andlin.recruit.model.Person;
import andlin.recruit.model.dto.CompetenceDTO;
import andlin.recruit.model.dto.PersonDTO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;

/**
 *
 * @author pinballmilitia
 */
@Named(value = "recruitmentManager")
@SessionScoped
public class RecruitmentManager {
    private Date fromDate;
    private Date toDate;
    private Date registrationDate;
    private List<CompetenceDTO> competences;
    private CompetenceDTO competence;
    private String name;
    
    private DataModel<PersonDTO> persons;
    private PersonDTO currentPerson;
    
    
    private String testname;
    
    @EJB
    RecruitmentController controller;
    /**
     * Creates a new instance of RecruitmentManager
     */
    public RecruitmentManager() {
    }
    
    @PostConstruct
    public void init() {
        competences = controller.getCompetences();
    }
    
    public void findPersons() {
        setPersons(controller.findPersons());
    }

    public String showDetails(PersonDTO person) throws Exception {

        setTestname("APORNA PLAB");
        
        setCurrentPerson(person);     
        
        if (getCurrentPerson() == null) {
            return "failure";
        } else {
            return "success";
        }       
    }

    public List<CompetenceDTO> getCompetences() {
        return competences;
    }

    public void setCompetences(List<CompetenceDTO> competences) {
        this.competences = competences;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataModel<PersonDTO> getPersons() {
        return controller.findPersons();
    }

    public void setPersons(DataModel<PersonDTO> persons) {
        this.persons = persons;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public CompetenceDTO getCompetence() {
        return competence;
    }

    public void setCompetence(CompetenceDTO competence) {
        this.competence = competence;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public PersonDTO getCurrentPerson() {
        return currentPerson;
    }

    public void setCurrentPerson(PersonDTO currentPerson) {
        this.currentPerson = currentPerson;
    }
    
    //test

    public String getTestname() {
        return testname;
    }

    public void setTestname(String testname) {
        this.testname = testname;
    }
    
}

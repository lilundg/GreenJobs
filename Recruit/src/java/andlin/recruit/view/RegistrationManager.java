/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.view;

import andlin.recruit.controller.ApplicationFacade;
import andlin.recruit.model.Availability;
import andlin.recruit.model.dto.AvailabilityDTO;
import andlin.recruit.model.Competence;
import andlin.recruit.model.dto.CompetenceDTO;
import andlin.recruit.validation.ValidDOB;
import andlin.recruit.validation.ValidEmail;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;

/**
 *
 * @author pinballmilitia
 */
@Named(value = "registrationManager")
@SessionScoped
public class RegistrationManager implements Serializable {

    //Controller
    @EJB
    private ApplicationFacade applicationFacade;
    @NotNull
    @Size(min = 2, max = 255, message = "{register.firstName.size}")
    private String firstName;
    @NotNull
    @Size(min = 2, max = 255, message = "{register.surName.size}")
    private String surName;
    @ValidDOB
    private String dateOfBirth;
    @ValidEmail
    private String email;
    private List<CompetenceDTO> competences;
    private CompetenceDTO competence;
    @NotNull(message = "{register.years.empty}")
    @Size(min = 0, max = 99, message = "{register.years.size}")
    private String yearsOfExperience;
    private List<AvailabilityDTO> availabilities;
    private Date availableFrom;
    private Date availableTo;
    
    
    
    private List<CompetenceDTO> selectedCompetences;  
    
    /**
     * Creates a new instance of RegistrationManager
     */
    public RegistrationManager() {
    }
    
    @PostConstruct
    public void init() {
        selectedCompetences = new LinkedList<CompetenceDTO>();
        competences = applicationFacade.getCompetences();
    }

    public List<CompetenceDTO> getCompetences() {
        return competences;
    }

    public void setCompetences(List<CompetenceDTO> competenceList) {
        this.competences = competenceList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public CompetenceDTO getCompetence() {
        return competence;
    }

    public void setCompetence(CompetenceDTO competence) {
        this.competence = competence;
    }

    public String getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(String yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public Date getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(Date availableFrom) {
        this.availableFrom = availableFrom;
    }

    public Date getAvailableTo() {
        return availableTo;
    }

    public void setAvailableTo(Date availableTo) {
        this.availableTo = availableTo;
    }

    public String createPerson() {
        return applicationFacade.createPerson(firstName, surName, dateOfBirth, email);
    }

    public void addCompetence() {   
        applicationFacade.addCompetence(competence,yearsOfExperience);

        //Clear input field
        yearsOfExperience = "";
    }

    public String doneAddCompetence() {
        return applicationFacade.doneAddCompetence();
    }

    public String registerApplication() {
        return applicationFacade.registerApplication();
    }

    public List<CompetenceDTO> getSelectedCompetences() {
        return applicationFacade.getSelectedCompetences();
    }

    public void setSelectedCompetences(List<CompetenceDTO> selectedCompetences) {
        this.selectedCompetences = selectedCompetences;
    }
    
    public void addAvailability() {

       applicationFacade.addAvailability(availableFrom, availableTo);       
        
    }
    
    public String doneAddAvailability() {

       return applicationFacade.doneAddAvailability();       
        
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<AvailabilityDTO> getAvailabilities() {
        return applicationFacade.getAvailabilities();
    }

    public void SetAvailabilities(List<AvailabilityDTO> avail) {
        this.availabilities = availabilities;
    }
    
    
}

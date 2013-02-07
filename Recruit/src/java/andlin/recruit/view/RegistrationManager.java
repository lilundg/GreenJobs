/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.view;

import andlin.recruit.controller.RegistrationController;
import andlin.recruit.model.dto.AvailabilityDTO;
import andlin.recruit.model.dto.CompetenceDTO;
import andlin.recruit.validation.ValidEmail;
import andlin.recruit.validation.ValidSSN;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author pinballmilitia
 */
@Named(value = "registrationManager")
@SessionScoped
public class RegistrationManager implements Serializable {

    @EJB
    private RegistrationController registrationController;
    @NotNull(message = "{register.firstname.null}")
    @Size(min = 2, max = 255, message = "{register.firstName.size}")
    private String firstName;
    @NotNull(message = "{register.lastname.null}")
    @Size(min = 2, max = 255, message = "{register.surName.size}")
    private String surName;
    @ValidSSN
    private String ssn;
    @ValidEmail
    private String email;
    private List<CompetenceDTO> competences;
    private CompetenceDTO competence;
    @NotNull(message = "{register.years.empty}")
    @Size(min = 0, max = 99, message = "{register.years.size}")
    @Digits(fraction=2,integer=2, message = "{register.years.notanumber}")
    private String yearsOfExperience;
    private List<AvailabilityDTO> availabilities;
    @NotNull
    @Future
    private Date availableFrom;
    @NotNull
    @Future
    private Date availableTo;   
    private List<CompetenceDTO> selectedCompetences;  
    
    /**
     * Creates a new instance of RegistrationManager
     */
    public RegistrationManager() {
    }
    
    @PostConstruct
    public void init() {
        //pre-fetch selectable competences
        competences = registrationController.getCompetences();
    }

    public String newPerson() {
        return registrationController.newPerson(firstName, surName, ssn, email);
    }

    public List<CompetenceDTO> getSelectedCompetences() {
        try {
            return registrationController.getSelectedCompetences();
        } catch (Exception e) {
            handleException(e);
        }
        
        return null;
        
    }

    public void addCompetence() {
        registrationController.addCompetence(competence, yearsOfExperience);
        yearsOfExperience = "";
    }

    public String doneAddCompetence() {

        String status = registrationController.doneAddCompetence();

        if (status.equals("failure")) {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("ValidationMessages");
            String error_message = resourceBundle.getString("register.competence.size");
            FacesMessage facesMessage = new FacesMessage(error_message);
            facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        }
        return status;
    }

    public void setSelectedCompetences(List<CompetenceDTO> selectedCompetences) {
        this.selectedCompetences = selectedCompetences;
    }

    public void addAvailability() {
        registrationController.addAvailability(availableFrom, availableTo);
    }

    public String doneAddAvailability() {
        return registrationController.doneAddAvailability();
    }

    public String registerApplication() {
        return registrationController.registerApplication();
    }
    
    private void handleException(Exception e) {
        e.printStackTrace(System.err);
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public List<AvailabilityDTO> getAvailabilities() {
        return registrationController.getAvailabilities();
    }

    public void SetAvailabilities(List<AvailabilityDTO> availabilities) {
        this.availabilities = availabilities;
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
    
}

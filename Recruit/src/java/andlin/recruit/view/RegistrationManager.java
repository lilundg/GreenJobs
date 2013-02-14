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
 * JSF Managed Bean representing client on server
 */
//@Named(value = "registrationManager")
//@SessionScoped
public class RegistrationManager implements Serializable {

    @EJB
    private RegistrationController registrationController;
    @NotNull(message = "{register.firstname.null}")
    @Size(min = 1, max = 255, message = "{register.firstName.size}")
    private String firstName;
    @NotNull(message = "{register.lastname.null}")
    @Size(min = 1, max = 255, message = "{register.surName.size}")
    private String surName;
    @ValidSSN
    private String ssn;
    @ValidEmail
    private String email;
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
    //Available competences
    private List<CompetenceDTO> competences;
    //Selected competences
    private List<CompetenceDTO> selectedCompetences;
    //The selected competence
    private CompetenceDTO competence;

    /**
     * Creates a new instance of RegistrationManager
     */
    public RegistrationManager() {
    }
    
    /**
     * Called after construction
     */
    @PostConstruct
    public void init() {
        //pre-fetch selectable competences
        competences = registrationController.getCompetences();
    }

    /**
     * Calls controller starting a new registration with a new person
     * @return "success" if successful, else "failure"
     */
    public String newApplication() throws Exception {
        return registrationController.newApplication(firstName, surName, ssn, email);
    }

    /**
     * Fetches a list representing the available competence choices
     * @return List of competenceDTO's
     */
    public List<CompetenceDTO> getSelectedCompetences() {
        return registrationController.getSelectedCompetences();
    }

    //Setter
    public void setSelectedCompetences(List<CompetenceDTO> selectedCompetences) {
        this.selectedCompetences = selectedCompetences;
    }

    /*
     * Add the selected competence to list of selected competences
     */
    public void addCompetence() {
        registrationController.addCompetence(competence, yearsOfExperience);
        yearsOfExperience = "";
    }

    /**
     * Called to mark the end of competence selection
     * @return "success" if done, otherwise "failure"
     */
    public String doneAddCompetence() {

        String status = registrationController.doneAddCompetence();

        if (status.equals("failure")) {
            notifyError("register.competence.size");
        }
        return status;
    }

    /**
     * Adds a time period the applicant is available for work.
     */
    public void addAvailability() {
        registrationController.addAvailability(availableFrom, availableTo);
    }

    /**
     * Called when we are done adding available time periods
     * @return 
     */
    public String doneAddAvailability() {
        String status = registrationController.doneAddAvailability();
        if (status.equals("failure")) {
            notifyError("register.availability.size");
        }

        return status;
    }

    public String registerApplication() {
        return registrationController.registerApplication();
    }

    private void notifyError(String bundleString) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("ValidationMessages");
        String error_message = resourceBundle.getString(bundleString);
        FacesMessage facesMessage = new FacesMessage(error_message);
        facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
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

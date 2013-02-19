package andlin.recruit.view;

import andlin.recruit.controller.RegistrationController;
import andlin.recruit.model.*;
import andlin.recruit.model.dto.AvailabilityDTO;
import andlin.recruit.model.dto.CompetenceDTO;
import andlin.recruit.model.dto.CompetenceProfileDTO;
import andlin.recruit.validation.ValidEmail;
import andlin.recruit.validation.ValidSSN;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
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
 * JSF Managed Bean representing view on server
 */
@Named(value = "registrationManager")
@SessionScoped
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
    @NotNull
    @Future
    private Date availableFrom;
    @NotNull
    @Future
    private Date availableTo;
    //Available competences
    private List<CompetenceDTO> competences;
    //The selected competence
    private CompetenceDTO competence;
    //
    private Person person;
    private List<Availability> availabilityList;
    private ArrayList<CompetenceProfile> competenceProfileList;

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
        if (person == null) {
            person = new Person();
        }
        Role role = registrationController.getSeekerRole();
        if(role == null){
            return "failure";
        }else{
            person.setName(firstName);
            person.setSurname(surName);
            person.setSsn(ssn);
            person.setEmail(email);
            person.setRoleId(role);

            return "success";
        }
    }

    /*
     * Add the selected competence to list of selected competences
     */
    public void addCompetence() {
        if (competenceProfileList == null) {
            competenceProfileList = new ArrayList<CompetenceProfile>();
        }

        Competence comp = (Competence) competence;

        //Check for duplicates/if already selected
        for (CompetenceProfile competenceProfile : competenceProfileList) {
            if (competenceProfile.getCompetenceId().equals(comp)) {
                return;
            }
        }

        //Create CompetenceProfile instance from Competence and yearsOfExperience       
        CompetenceProfile competenceProfile = new CompetenceProfile();
        competenceProfile.setCompetenceId(comp);
        competenceProfile.setYearsOfExperience(new BigDecimal(yearsOfExperience));
        competenceProfile.setPersonId(person);

        competenceProfileList.add(competenceProfile);
        yearsOfExperience = "";
    }

    /**
     * Called to mark the end of competence selection
     * @return "success" if done, otherwise "failure"
     */
    public String doneAddCompetence() {

        String status = "failure";
        //User must have selected atleast one competence
        if ((competenceProfileList != null) && !(competenceProfileList.isEmpty())) {
            status = "success";
        }
        if (status.equals("failure")) {
            notifyError("register.competence.size");
        }
        return status;
    }

    /**
     * Adds a time period the applicant is available for work.
     */
    public void addAvailability() {
        if (availabilityList == null) {
            availabilityList = new LinkedList<Availability>();
        }

        Availability availability = new Availability();
        availability.setFromDate(availableFrom);
        availability.setToDate(availableTo);
        availability.setPersonId(person);

        //Avoid duplicates
        for (Availability entry : availabilityList) {
            if (entry.equals(availability)) {
                return;
            }
        }

        availabilityList.add(availability);
    }

    /**
     * Called when we are done adding available time periods
     * @return 
     */
    public String doneAddAvailability() {
        String status = "failure";
        //User must provide at least one time period
        if (availabilityList != null) {
            status = "success";
        }
        if (status.equals("failure")) {
            notifyError("register.availability.size");
        }
        return status;
    }

    /**
     * Adds the stored competence profile list and availability list to the person object
     * and sends it to the controller to persist.
     * Invalidates the current session.
     * 
     * @return success if no error
     */
    public String registerApplication() {
        person.setCompetenceProfileCollection(competenceProfileList);
        person.setAvailabilityCollection(availabilityList);
        registrationController.persist(person);

        Locale loc = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        //We are done with registration, invalidate session.
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        
        FacesContext.getCurrentInstance().getViewRoot().setLocale(loc);

        return "success";
    }

    private void notifyError(String bundleString) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("ValidationMessages");
        String error_message = resourceBundle.getString(bundleString);
        FacesMessage facesMessage = new FacesMessage(error_message);
        facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }

    /***********************
     * GETTERS AND SETTERS *
     ***********************/
    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public List<AvailabilityDTO> getAvailabilities() {
        return (List<AvailabilityDTO>) (List<?>) availabilityList;
    }

    public void SetAvailabilities(List<AvailabilityDTO> availabilities) {
    }
    
    public List<CompetenceProfileDTO> getSelectedCompetences() {
        
        return (List<CompetenceProfileDTO>) (List<?>) competenceProfileList;
    }

    public void setSelectedCompetences(List<CompetenceProfileDTO> selectedCompetences) {
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
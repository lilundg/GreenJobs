/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.controller;

import andlin.recruit.model.*;
import andlin.recruit.model.dto.AvailabilityDTO;
import andlin.recruit.model.dto.CompetenceDTO;
import andlin.recruit.validation.ValidEmail;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * This controller handles a session where a user registers his/hers
 * application. No data is persisted until registerApplication is called which
 * should be done after creating a person and adding competences and
 * availabilities.
 */
@Stateful
@LocalBean
public class RegistrationController {

    private Person person;
    private List<Competence> selectedCompetenceList;
    private List<CompetenceProfile> competenceProfileList;
    private List<Availability> availabilityList;
    @PersistenceContext(unitName = "RecruitPU")
    private EntityManager em;

    /**
     * Persist entity object
     *
     * @param object
     */
    public void persist(Object object) {
        em.persist(object);

    }

    /**
     * Creates a new instance of entity class Person and sets some of its
     * properties. To set property 'role' an instance of Role class with name
     * property 'job_seeker' is retrieved from database. If no such entity
     * exists an exception is thrown.
     *
     * @param name
     * @param surName
     * @param ssn
     * @param email
     */
    public String newPerson(String name, String surName, String ssn, @ValidEmail String email) {

        if (person == null) {
            person = new Person();
        }

        person.setName(name);
        person.setSurname(surName);
        person.setSsn(ssn);
        person.setEmail(email);

        Role role = null;
        //Fetch role from db
        try {
            role = (Role) em.createNamedQuery("Role.findByName").setParameter("name", "job_seeker").getSingleResult();
        } catch (NoResultException e) {
            handleException("messages", "register.exception.dberror.role");
            return "failure";
        }

        person.setRoleId(role);

        return "success";

    }

    /**
     * Add a time period that a 'person' is able to work to his/her list of time
     * periods(availabilites)
     *
     * @param fromDate start of time period
     * @param toDate end of time period
     */
    public void addAvailability(Date fromDate, Date toDate) {
        if (availabilityList == null) {
            availabilityList = new LinkedList<Availability>();
        }

        Availability availability = new Availability();   
        availability.setFromDate(fromDate);
        availability.setToDate(toDate);
        availability.setPersonId(person);
        
        //Avoid duplicates
        for(Availability entry : availabilityList) {
            if(entry.equals(availability)) 
                return;
        }

        availabilityList.add(availability);
    }

    /**
     * Called when client is done adding available time periods.
     *
     * @return 'success' if at least one availability has been added, otherwise
     * 'failure'
     */
    public String doneAddAvailability() {
        //User must provide at least one time period
        if (availabilityList == null) {

            ResourceBundle resourceBundle = ResourceBundle.getBundle("ValidationMessages");
            String error_message = resourceBundle.getString("register.availability.size");
            FacesMessage fm = new FacesMessage(error_message);
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, fm);
            return "failure";
        } else {
            return "success";
        }
    }

    /**
     * Called to get a list of AvailabilityDTO's representing the supplied time
     * periods client can work.
     *
     * @return
     */
    public List<AvailabilityDTO> getAvailabilities() {
        return (List<AvailabilityDTO>) (List<?>) availabilityList;
    }

    /**
     * Add competence to list of selected competences along with the specified
     * length of time of the applicants experience
     *
     * @param competence An instance of CompetenceDTO representing the selected
     * Competence
     * @param yearsOfExperience String representing the years of experience
     */
    public void addCompetence(CompetenceDTO competenceDTO, String yearsOfExperience) {
        if (selectedCompetenceList == null) {
            selectedCompetenceList = new LinkedList<Competence>();
        }

        Competence competence = new Competence();
        competence.setCompetenceId(competenceDTO.getCompetenceId());
        competence.setName(competenceDTO.getName());

        //If not already selected
        if (!selectedCompetenceList.contains(competence)) {
            selectedCompetenceList.add(competence);
            addCompetenceProfile(competence, yearsOfExperience);
        }
    }

    /**
     * Called when client is done adding competences
     *
     * @return 'success' if at least one competence has been selected, otherwise
     * 'failure'
     */
    public String doneAddCompetence() {

        //User must have selected atleast one competence
        if (selectedCompetenceList == null) {
            return "failure";
        } else {
            return "success";
        }
    }

    /**
     *
     * @param comp
     * @param yearsOfExperience
     */
    public void addCompetenceProfile(Competence competence, String yearsOfExperience) {
        if (competenceProfileList == null) {
            competenceProfileList = new LinkedList<CompetenceProfile>();
        }

        CompetenceProfile competenceProfile = new CompetenceProfile();
        competenceProfile.setCompetenceId(competence);
        competenceProfile.setYearsOfExperience(new BigDecimal(yearsOfExperience));
        competenceProfile.setPersonId(person);

        competenceProfileList.add(competenceProfile);
    }

    /**
     * This is the final call that persists the person applying for work
     */
    public String registerApplication() {
        person.setCompetenceProfileCollection(competenceProfileList);
        person.setAvailabilityCollection(availabilityList);
        persist(person);

        //We are done with registration, invalidate session.
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        return "success";
    }

    /**
     *
     * @return
     */
    public List<CompetenceDTO> getCompetences() {

        Query query = em.createNamedQuery("Competence.findAll");

        try {
            return (List<CompetenceDTO>) query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Returns a list of CompetenceDTO objects representing the Component
     * objects selected by user
     *
     * @return List of CompetenceDTO objects
     */
    public List<CompetenceDTO> getSelectedCompetences() {
        return (List<CompetenceDTO>) (List<?>) selectedCompetenceList;
    }

    /**
     * Returns an instance of 'Competence' with name attribute == 'name' or null
     * if no such Competence objects exists.
     *
     * @param name name attribute of Competence object
     * @return Competence instance or null if no matching instance found
     */
    public CompetenceDTO findCompetenceByName(String name) {

        Query query = em.createNamedQuery("Competence.findByName").setParameter("name", name);

        try {
            return (CompetenceDTO) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    private void handleException(String bundleName, String bundleMessage) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleName);
        String error_message = resourceBundle.getString(bundleMessage);
        FacesMessage fm = new FacesMessage(error_message);
        fm.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext.getCurrentInstance().addMessage(null, fm);
    }
}

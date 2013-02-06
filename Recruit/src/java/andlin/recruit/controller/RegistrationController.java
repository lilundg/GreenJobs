/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.controller;

import andlin.recruit.model.*;
import andlin.recruit.model.dto.AvailabilityDTO;
import andlin.recruit.model.dto.CompetenceDTO;
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
 *
 * @author Andlind
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
     *
     * @param object
     */
    public void persist(Object object) {
        em.persist(object);

    }

    /**
     * 
     * @param name
     * @param surName
     * @param ssn
     * @param email
     */
    public String newPerson(String name, String surName, String ssn, String email) {

        if (person == null) {
            person = new Person();
        }
        
        person.setName(name);
        person.setSurname(surName);
        person.setSsn(ssn);
        person.setEmail(email);

        Role role;
        try {
            role = (Role) em.createNamedQuery("Role.findByName").setParameter("name", "job_seeker").getSingleResult();
        } catch (NoResultException e) {
            //TODO Handle "role not found" in database..... perhaps output = "DB error." 
            return "failure";
        }

        person.setRoleId(role);

        return "success";

    }

    /**
     * Sets time periods jobseeker is able to work
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

        availabilityList.add(availability);
    }

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
     *
     * @return
     */
    public List<AvailabilityDTO> getAvailabilities() {
        return (List<AvailabilityDTO>) (List<?>) availabilityList;
    }

    /**
     * Add competence to list of selected competences along with the specified
     * length of time of the applicants experience
     * @param competence An instance of CompetenceDTO representing the selected Competence
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

    public String doneAddCompetence() {

        //User must have selected atleast one competence
        if (selectedCompetenceList == null) {

            ResourceBundle resourceBundle = ResourceBundle.getBundle("ValidationMessages");
            String error_message = resourceBundle.getString("register.competence.size");
            FacesMessage facesMessage = new FacesMessage(error_message);
            facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
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
     * This is the final call that persists the application 
     */
    public String registerApplication() {
        person.setCompetenceProfileCollection(competenceProfileList);
        person.setAvailabilityCollection(availabilityList);
        persist(person);

        //We are done with registration, invalidate session.
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        return "registration_success";
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
}

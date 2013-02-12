/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.controller;

import andlin.recruit.model.*;
import andlin.recruit.model.dto.AvailabilityDTO;
import andlin.recruit.model.dto.CompetenceDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
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
     * exists an exception is thrown. This should be first method called when
     * creating an application.
     *
     * @param name
     * @param surName
     * @param ssn
     * @param email
     */
    public String newApplication(String name, String surName, String ssn, String email) throws Exception {

        if (person == null) {
            person = new Person();
        }

        person.setName(name);
        person.setSurname(surName);
        person.setSsn(ssn);
        person.setEmail(email);

        Query query = em.createNamedQuery("Role.findByName").setParameter("name", "job_seeker");

        if (query.getResultList().isEmpty()) {
            return "failure";
        }

        person.setRoleId((Role) query.getResultList().get(query.getFirstResult()));

        return "success";

    }

    /**
     * Add a time period that a 'person' is able to work to his/her list of time
     * periods(availabilities)
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
        for (Availability entry : availabilityList) {
            if (entry.equals(availability)) {
                return;
            }
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
        if (competenceProfileList == null) {
            competenceProfileList = new ArrayList<CompetenceProfile>();
        }

        //Create Competence instance from CompetenceDTO
        Competence competence = new Competence();
        competence.setCompetenceId(competenceDTO.getCompetenceId());
        competence.setName(competenceDTO.getName());

        //Check for duplicates/if already selected
        for (CompetenceProfile competenceProfile : competenceProfileList) {
            if (competenceProfile.getCompetenceId().equals(competence)) {
                return;
            }
        }

        //Create CompetenceProfile instance from Competence and yearsOfExperience       
        CompetenceProfile competenceProfile = new CompetenceProfile();
        competenceProfile.setCompetenceId(competence);
        competenceProfile.setYearsOfExperience(new BigDecimal(yearsOfExperience));
        competenceProfile.setPersonId(person);

        competenceProfileList.add(competenceProfile);
    }

    /**
     * Called when client is done adding competences
     *
     * @return 'success' if at least one competence has been selected, otherwise
     * 'failure'
     */
    public String doneAddCompetence() {

        //User must have selected atleast one competence
        if (competenceProfileList == null) {
            return "failure";
        } else if (competenceProfileList.isEmpty()) {
            return "failure";
        } else {
            return "success";
        }
    }

    /**
     * This is the final call that persists the person applying for work
     *
     * @return "success" when successful
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
     * Returns a list representing all available competences
     *
     * @return list of CompetenceDTO's or null if none found in db
     */
    public List<CompetenceDTO> getCompetences() {
        Query query = em.createNamedQuery("Competence.findAll");
        return (List<CompetenceDTO>) query.getResultList();
    }

    /**
     * Returns a list of CompetenceDTO objects representing the Component
     * objects selected by user
     *
     * @return List of CompetenceDTO objects
     */
    public List<CompetenceDTO> getSelectedCompetences() {
        if (competenceProfileList == null) {
            competenceProfileList = new ArrayList<CompetenceProfile>();
        }
        ArrayList<Competence> competences = new ArrayList<Competence>();
        for (CompetenceProfile competenceProfile : competenceProfileList) {
            competences.add(competenceProfile.getCompetenceId());
        }
        return (List<CompetenceDTO>) (List<?>) competences;
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
        if (query.getResultList().isEmpty()) {
            return null;
        }
        return (CompetenceDTO) query.getResultList().get(query.getFirstResult());
    }
}
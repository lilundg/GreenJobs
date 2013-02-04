/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.controller;

import andlin.recruit.model.dto.CompetenceDTO;
import andlin.recruit.model.dto.AvailabilityDTO;
import andlin.recruit.model.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
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
public class ApplicationFacade {

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
    public void createPerson(String name, String surName, String ssn, String email) {
        if (person == null) {
            person = new Person();
        }
        person.setName(name);
        person.setSurname(surName);
        person.setSsn(ssn);
        person.setEmail(email);

        //Applicant is jobseeker
        Role role = (Role) em.createNamedQuery("Role.findByName").setParameter("name", "jobseeker").getSingleResult();
        person.setRoleId(role);
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

    /**
     *
     * @return
     */
    public List<AvailabilityDTO> getAvailabilities() {
        return (List<AvailabilityDTO>) (List<?>) availabilityList;
    }

    /**
     *
     * @param competence
     * @param yearsOfExperience
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
     *
     * @param firstName
     * @param surName
     * @param email
     * @param selectedExpertise
     * @param selectedAvailability
     */
    public void registerApplication() {
        person.setCompetenceProfileCollection(competenceProfileList);
        person.setAvailabilityCollection(availabilityList);
        persist(person);
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

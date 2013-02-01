/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.controller;

import andlin.recruit.model.*;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import javax.persistence.NoResultException;

/**
 *
 * @author Andlind
 */
@Stateless
@LocalBean
public class ApplicationFacade {

    private Person person;
    private List<Competence> competences;
    private List<CompetenceProfile> competenceProfiles;
    private List<Availability> availabilities;
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

        if (availabilities == null) {
            availabilities = new LinkedList<Availability>();
        }

        Availability availability = new Availability();
        availability.setFromDate(fromDate);
        availability.setToDate(toDate);
        availability.setPersonId(person);

        availabilities.add(availability);
    }

    /**
     *
     * @return
     */
    public List<AvailabilityDTO> getAvailabilities() {

        return (List<AvailabilityDTO>) (List<?>) availabilities;

    }

    /**
     *
     * @param competence
     * @param yearsOfExperience
     */
    public void addCompetence(CompetenceDTO competenceDTO, String yearsOfExperience) {
        if (competences == null) {
            competences = new LinkedList<Competence>();
        }

        Competence competence = new Competence();
        competence.setCompetenceId(competenceDTO.getCompetenceId());
        competence.setName(competenceDTO.getName());

        competences.add(competence);

        addCompetenceProfile(competence, yearsOfExperience);
    }

    /**
     *
     * @param comp
     * @param yearsOfExperience
     */
    public void addCompetenceProfile(Competence competence, String yearsOfExperience) {
        if (competenceProfiles == null) {
            competenceProfiles = new LinkedList<CompetenceProfile>();
        }

        //CompetenceProfile competenceProfile = null;
        //Query query = null;

        //query = em.createNamedQuery("Competence.findByName").setParameter("name", comp.getName());

        //Competence competence = (Competence) query.getSingleResult();

        CompetenceProfile competenceProfile = new CompetenceProfile();
        competenceProfile.setCompetenceId(competence);
        competenceProfile.setYearsOfExperience(new BigDecimal(yearsOfExperience));
        competenceProfile.setPersonId(person);

        competenceProfiles.add(competenceProfile);
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
        person.setCompetenceProfileCollection(competenceProfiles);
        person.setAvailabilityCollection(availabilities);
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
     * @return List of CompetenceDTO objects
     */
    public List<CompetenceDTO> getSelectedCompetences() {

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

        try {
            return (CompetenceDTO) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}

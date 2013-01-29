/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.controller;

import andlin.recruit.model.Competence;
import andlin.recruit.model.CompetenceProfile;
import javax.ejb.Stateful;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import andlin.recruit.model.Person;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author pinballmilitia
 */
@Stateless
@LocalBean
public class PersonFacade {
    
    private List<CompetenceProfile> competence_profile;
    
    @PersistenceContext(unitName = "RecruitPU2")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public void createPerson(String firstName, String surName, String email) {
        Person person = new Person();
        person.setName(firstName);
        person.setSurname(surName);
        person.setEmail(email);
        person.setPersonId(Long.MIN_VALUE);
        em.persist(person);
    }
    
    

    public void persist(Object object) {
        em.persist(object);
        
    }

    public List<Competence> getCompetenceList() {
        Query query = em.createNamedQuery("Competence.findAll");
        return query.getResultList();
    }

    
    //Hårdkodat - anropas när användaren väljer en kompetens och erfarenhetstid
    public void addCompetenceProfile(String expertise, String yearsOfExperience) {
        if (competence_profile == null) {
            competence_profile = new LinkedList<CompetenceProfile>();
        }
        
        CompetenceProfile profile = new CompetenceProfile();
        
        //TODO figure out how to get hold of Competence id 
        profile.setCompetenceId(BigInteger.ZERO);
        profile.setYearsOfExperience(new BigDecimal(20));
        
        competence_profile.add(profile);
    }

    //Denna metod anropas när användaren är klar med att lägga till kompetens/expertis 
    public void createCompetence() {
        
        //Vilkor för att gå vidare till nästa vy?
        //Maybe final check here?
        if(true)
            return;
    }

    public List<CompetenceProfile> getSelectedExpertise() {
        return competence_profile;
    }

    public void addAvailability(String availableFrom, String availableTo) {
        
    }
}

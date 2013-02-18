/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.controller;

import andlin.recruit.model.Competence;
import andlin.recruit.model.Person;
import andlin.recruit.model.Role;
import andlin.recruit.model.dto.CompetenceDTO;
import andlin.recruit.model.dto.PersonDTO;
import andlin.recruit.model.dto.RecruitmentQueryDTO;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *  This is the controller for handling applications
 */
@Stateful
@LocalBean
public class RecruitmentController {

    @PersistenceContext(unitName = "RecruitPU")
    private EntityManager em;
    private List<Person> persons;
    /*
    @Inject
    Logger logger;
    */
    public void persist(Object object) {
        em.persist(object);
    }

    /**
     * Query database for all available applicants i.e. persons
     * with role = 'job_seeker'
     * 
     * @return a list of job seekers (Person)
     */
    public DataModel<PersonDTO> find() {
        //persons = (List<Person>) em.createNamedQuery("Person.findAll").getResultList();

        //get role 
        Query query = em.createNamedQuery("Role.findByName").setParameter("name", "job_seeker");

        Role role = (Role) query.getResultList().get(query.getFirstResult());      
        
        Query queryPersons = em.createQuery("select p from Person p where p.roleId = :roleId");
        
        queryPersons.setParameter("roleId", role);
        //persons = em.createQuery("select p from Person p where p.roleId = :roleId").getResultList();        

        persons = queryPersons.getResultList();
        
        DataModel model = new ListDataModel((List<PersonDTO>) (List<?>) persons);
        return model;
    }

        /**
     * Query database for applicants by name
     * 
     * @return a list of job seekers with matching name (Person)
     */
    public DataModel<PersonDTO> find(String name) {
        Query query = em.createNamedQuery("Person.findByName");
        query.setParameter("name", name);
        persons = (List<Person>) query.getResultList();
        DataModel model = new ListDataModel((List<PersonDTO>) (List<?>) persons);
        return model;
    }
    
    public DataModel<PersonDTO> search(RecruitmentQueryDTO queryValues){
        String firstName = "";
        String surName = "";
        Date fromDate = null;
        Date toDate = null;
        String competence = "";
        if(queryValues.getFirstName() != null){
            firstName = queryValues.getFirstName();
        }
        if(queryValues.getSurName() != null){
            surName = queryValues.getSurName();
        }
        if(queryValues.getCompetence() != null){
            competence = queryValues.getCompetence().getName();
        }
        try {
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            if(queryValues.getFrom() != null){
                fromDate = queryValues.getFrom();
            }else{
                    fromDate = df.parse("01-01-2000");

            }
            if(queryValues.getTo() != null){
                toDate = queryValues.getTo();
            }else{
                toDate = df.parse("01-01-2100");
            }
        } catch (ParseException ex) {
            //logger.log(Level.SEVERE, null, ex);
        }
        //Query query = em.createQuery("SELECT DISTINCT p FROM Person p, CompetenceProfile c WHERE p = c.personId AND c.competenceId.name LIKE ?2 AND (p.name LIKE ?1 OR p.surname LIKE ?1)");
        Query query = em.createQuery("SELECT DISTINCT p FROM Person p, CompetenceProfile c, Availability a WHERE p = c.personId AND p = a.personId AND c.competenceId.name LIKE ?3 AND p.name LIKE ?1 AND p.surname LIKE ?2 AND a.fromDate >= ?4 AND a.toDate <= ?5");
        query.setParameter(1, "%" + firstName + "%");
        query.setParameter(2, "%" + surName + "%");
        query.setParameter(3, "%" + competence + "%");
        query.setParameter(4, fromDate);
        query.setParameter(5, toDate);
        
        List<Person> result = (List<Person>) query.getResultList();
        DataModel model = new ListDataModel((List<PersonDTO>) (List<?>) result);
        return model;
    }
    
    

    /**
     * Changes role of person from 'job_seeker' to 'accepted'
     *
     * @param person
     */
    public void accept(PersonDTO person) {
        Query query = em.createNamedQuery("Role.findByName");
        query.setParameter("name", "accepted");
        Role accepted_role = (Role) query.getResultList().get(0);
               
        Person searchPerson = new Person(person.getPersonId());
        
        Query pquery = em.createNamedQuery("Person.findByPersonId");
        pquery.setParameter("personId", person.getPersonId());
        
        Person acceptedPerson = (Person) pquery.getResultList().get(0);
        
        acceptedPerson.setRoleId(accepted_role);
        em.flush();
    }

    /**
     * Changes role of person from 'job_seeker' to 'rejected'
     *
     * @param person
     */
    public void reject(PersonDTO person) {
        Query query = em.createNamedQuery("Role.findByName");
        query.setParameter("name", "rejected");
        Role rejected_role = (Role) query.getResultList().get(0);
        
        Person searchPerson = new Person(person.getPersonId());
        
        Query pquery = em.createNamedQuery("Person.findByPersonId");
        pquery.setParameter("personId", person.getPersonId());
        
        Person rejectedPerson = (Person) pquery.getResultList().get(0);
        
        rejectedPerson.setRoleId(rejected_role);
        em.flush();
    }

    /**
     * Returns a list representing all available competences
     *
     * @return list of CompetenceDTO's or null if none found in db
     */
    public List<CompetenceDTO> getCompetences() {
        Query query = em.createNamedQuery("Competence.findAll");
        List<Competence> resultList = query.getResultList();
        
        Competence empty_competence = new Competence();
        empty_competence.setName("-");
        resultList.add(0, empty_competence);
        return (List<CompetenceDTO>) (List<?>) resultList;
    }
}

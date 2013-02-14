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
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
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
    
    public DataModel<PersonDTO> search(String name, Date date, CompetenceDTO comp){
        if(name == null){
            name = "";
        }
        String competence = "";
        if(comp != null){
            competence = comp.getName();
        }
        Query query = em.createQuery("SELECT DISTINCT p FROM Person p, CompetenceProfile c WHERE p = c.personId AND c.competenceId.name LIKE ?2 AND (p.name LIKE ?1 OR p.surname LIKE ?1)");
        //Query query = em.createQuery("SELECT p FROM Person p WHERE p.name LIKE ?1 OR p.surname LIKE ?1");
        query.setParameter(1, "%" + name + "%");
        query.setParameter(2, "%" + competence + "%");
        
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

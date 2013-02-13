/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.controller;

import andlin.recruit.model.Person;
import andlin.recruit.model.Role;
import andlin.recruit.model.dto.CompetenceDTO;
import andlin.recruit.model.dto.PersonDTO;
import java.util.List;
import java.util.Vector;
import javax.ejb.Stateful;
import javax.ejb.LocalBean;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.metamodel.Metamodel;

/**
 *
 * @author pinballmilitia
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

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public DataModel<PersonDTO> find() {
        //Query query = em.createNamedQuery("Role.findByName");
        persons = (List<Person>) em.createNamedQuery("Person.findAllJobSeekers").getResultList();
        DataModel model = new ListDataModel((List<PersonDTO>) (List<?>) persons);
        return model;
    }

    public DataModel<PersonDTO> find(String name) {
        Query query = em.createNamedQuery("Person.findByName");
        query.setParameter("name", name);
        persons = (List<Person>) query.getResultList();
        DataModel model = new ListDataModel((List<PersonDTO>) (List<?>) persons);
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
        Role role = (Role) query.getResultList().get(0);
        Person acceptedPerson = (Person) person;
        acceptedPerson.setRoleId(role);
        persist(acceptedPerson);
    }

    /**
     * Changes role of person from 'job_seeker' to 'rejected'
     *
     * @param person
     */
    public void reject(PersonDTO person) {
        Query query = em.createNamedQuery("Role.findByName");
        query.setParameter("name", "rejected");
        Role role = (Role) query.getResultList().get(0);
        Person rejectedPerson = (Person) person;
        rejectedPerson.setRoleId(role);
        persist(rejectedPerson);
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

    public void tester() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        criteriaBuilder.createQuery(Person.class);
        Metamodel metamodel = em.getMetamodel();

    }
}

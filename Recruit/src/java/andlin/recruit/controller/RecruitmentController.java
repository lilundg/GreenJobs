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
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *  This is the controller for handling applications.
 */
@Stateful
@LocalBean
public class RecruitmentController {

    @PersistenceContext(unitName = "RecruitPU")
    private EntityManager em;
    //private List<Person> persons;
    /*
    @Inject
    Logger logger;
    */
    public void persist(Object object) {
        em.persist(object);
    }
    
    /**
     * Query database according to given parameters
     * @param queryValues : search parameters
     * @return : list of PersonDTO's matching search parameters
     */
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
        Query query = em.createQuery("SELECT DISTINCT p FROM Person p, CompetenceProfile c, Availability a WHERE p = c.personId AND p = a.personId AND c.competenceId.name LIKE ?3 AND p.name LIKE ?1 AND p.surname LIKE ?2 AND a.fromDate >= ?4 AND a.toDate <= ?5 AND p.roleId.name = 'job_seeker'");
        query.setParameter(1, firstName + "%");
        query.setParameter(2, surName + "%");
        query.setParameter(3, "%" + competence + "%");
        query.setParameter(4, fromDate);
        query.setParameter(5, toDate);
        
        List<Person> result = (List<Person>) query.getResultList();
        DataModel model = new ListDataModel((List<PersonDTO>) (List<?>) result);
        return model;
    }
    
    

    /**
     * Changes role of person from 'job_seeker' to 'accepted'. This
     * means applicant is hired.
     *
     * @param person : person who's application is accepted
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
     * @param person: person who's application is rejected
     */
    public void reject(PersonDTO person) {
        Query query = em.createNamedQuery("Role.findByName");
        query.setParameter("name", "rejected");
        Role rejected_role = (Role) query.getResultList().get(0);
        
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

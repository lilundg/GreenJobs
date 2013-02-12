/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.controller;

import andlin.recruit.model.Competence;
import andlin.recruit.model.dto.CompetenceDTO;
import andlin.recruit.model.dto.PersonDTO;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Controller used when managing applications.
 */
@Stateless
@LocalBean
public class RecruitmentController {

    @PersistenceContext(unitName = "RecruitPU")
    private EntityManager em;

    /**
     * Fetch list with all persons according 
     * to search terms.
     * @return list of PersonDTO's
     */
    public DataModel<PersonDTO> findPersons() {
        Query query = em.createNamedQuery("Person.findAll");
        //DataModel dm = new ListDataModel((List<PersonDTO>) query.getResultList())
        return new ListDataModel((List<PersonDTO>) query.getResultList());
    }

    /**
     * Returns a list representing all available competences + one 
     * empty competence named 'none'
     *
     * @return list of CompetenceDTO's or null if none found in db
     */
    public List<CompetenceDTO> getCompetences() {
        Query query = em.createNamedQuery("Competence.findAll");
        Competence competence = new Competence();
        competence.setName("-none-");
        List<Competence> list = query.getResultList();
        list.add(competence);
        return (List<CompetenceDTO>) (List<?>) list;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public void persist(Object object) {
        em.persist(object);
    }
}

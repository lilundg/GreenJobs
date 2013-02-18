package andlin.recruit.controller;

import andlin.recruit.model.Competence;
import andlin.recruit.model.Role;
import andlin.recruit.model.dto.CompetenceDTO;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * This controller handles a session where a user registers his/hers
 * application. No data is persisted until registerApplication is called which
 * should be done after creating a person and adding competences and
 * availabilities.
 */
@Stateless
@LocalBean
public class RegistrationController {
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

    public Role getSeekerRole(){
        
        Query query = em.createNamedQuery("Role.findByName").setParameter("name", "job_seeker");

        if (query.getResultList().isEmpty()) {
            return null;
        }else{
            return (Role) query.getResultList().get(query.getFirstResult());
        }
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
     * Returns an instance of 'Competence' with name attribute == 'name' or null
     * if no such Competence objects exists.
     *
     * @param name name attribute of Competence object
     * @return Competence instance or null if no matching instance found
     */
    public Competence findCompetenceByName(String name) {
        Query query = em.createNamedQuery("Competence.findByName").setParameter("name", name);
        if (query.getResultList().isEmpty()) {
            return null;
        }
        return (Competence) query.getResultList().get(query.getFirstResult());
    }
}
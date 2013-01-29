/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.controller;

import javax.ejb.Stateful;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import andlin.recruit.model.Person;

/**
 *
 * @author pinballmilitia
 */
@Stateless
@LocalBean
public class PersonFacade {
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
}

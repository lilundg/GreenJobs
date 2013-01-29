/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.view;

import andlin.recruit.controller.PersonFacade;
import andlin.recruit.model.Competence;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author pinballmilitia
 */
@Named(value = "registrationManager")
@SessionScoped
public class RegistrationManager implements Serializable {
    @EJB
    private PersonFacade personFacade;
    
    private String firstName;
    private String surName;
    private String email;
    private List<andlin.recruit.model.Competence> expertise;
    /**
     * Creates a new instance of RegistrationManager
     */
    public RegistrationManager() {
        
    }

    public List<Competence> getExpertise() {
        return expertise;
    }

    public void setExpertise(List<Competence> expertise) {
        this.expertise = expertise;
    }
    
    
    
    public void createPerson() {
        personFacade.createPerson(firstName, surName, email);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }
    
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.view;

import andlin.recruit.controller.PersonFacade;
import andlin.recruit.model.Availability;
import andlin.recruit.model.Competence;
import andlin.recruit.model.CompetenceProfile;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.LinkedList;
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
    
    private List<Competence> competenceList;
    private List<CompetenceProfile> selectedExpertise;
    private List<Availability> selectedAvailability;
    
    private String expertise;
    private String yearsOfExperience;
    
    private String availableFrom;
    private String availableTo;
    
    
    /**
     * Creates a new instance of RegistrationManager
     */
    public RegistrationManager() {
        
    }

    public void createPerson() {
        personFacade.createPerson(firstName, surName, email);
    }

    public void addCompetence() {
        personFacade.addCompetenceProfile(expertise, yearsOfExperience);
    }
    
        public void addAvailability() {
        personFacade.addAvailability(availableFrom, availableTo);
    }
    
    public void createCompetence() {
        personFacade.createCompetence();
    }

    public List<Competence> getCompetenceList() {
        return personFacade.getCompetenceList();
    }

    public void setCompetenceList(List<Competence> competenceList) {
        this.competenceList = competenceList;
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

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(String yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public List<CompetenceProfile> getSelectedExpertise() {
        
        selectedExpertise = personFacade.getSelectedExpertise();
        return selectedExpertise;
    }

    public void setSelectedExpertise(List<CompetenceProfile> selectedExpertise) {
        this.selectedExpertise = selectedExpertise;
    }

    public String getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(String availableFrom) {
        this.availableFrom = availableFrom;
    }

    public String getAvailableTo() {
        return availableTo;
    }

    public void setAvailableTo(String availableTo) {
        this.availableTo = availableTo;
    }

    public List<Availability> getSelectedAvailability() {
        return selectedAvailability;
    }

    public void setSelectedAvailability(List<Availability> selectedAvailability) {
        this.selectedAvailability = selectedAvailability;
    }
    

    
    
}

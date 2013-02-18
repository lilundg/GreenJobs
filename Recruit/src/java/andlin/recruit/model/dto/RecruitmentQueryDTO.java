/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.model.dto;

import java.util.Date;

/**
 * This class represents an object used to store query
 * parameters when searching for a person. It encapsulates
 * name, work period and a competence. 
 */
public class RecruitmentQueryDTO {
    
    private String firstName, surName;
    private Date from, to;
    private CompetenceDTO competence;
    
    /**
     * Constructor
     * @param firstName
     * @param surName
     * @param from : start of time period
     * @param to : end of time period
     * @param comp : competence
     */
    public RecruitmentQueryDTO(String firstName, String surName, Date from, Date to, CompetenceDTO comp){
        this.firstName = firstName;
        this.surName = surName;
        this.from = from;
        this.to = to;
        this.competence = comp;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurName() {
        return surName;
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

    public CompetenceDTO getCompetence() {
        return competence;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.model.dto;

import java.util.Date;

/**
 *
 * @author Linus
 */
public class RecruitmentQueryDTO {
    
    private String firstName, surName;
    private Date from, to;
    private CompetenceDTO competence;
    
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

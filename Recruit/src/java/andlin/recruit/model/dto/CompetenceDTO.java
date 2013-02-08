/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.model.dto;

/**
* Represents a read only view of the Competence entity class
 */
public interface CompetenceDTO {

    /**
     * 
     * @return 
     */
    public Long getCompetenceId();

    /**
     * 
     * @return 
     */
    public String getName();
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.model.dto;

import andlin.recruit.model.Competence;
import java.math.BigDecimal;

/**
 *  Represents a read-only view of CompetenceProfile class.
 * Provides getters only.
 */
public interface CompetenceProfileDTO {
    
    public BigDecimal getYearsOfExperience();

    public Competence getCompetenceId();
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.model.dto;

import andlin.recruit.model.Competence;
import java.math.BigDecimal;

/**
 *
 * @author Linus
 */
public interface CompetenceProfileDTO {
    
    /**
     * 
     * @return 
     */
    public BigDecimal getYearsOfExperience();
    
    /**
     * 
     * @return 
     */
    public Competence getCompetenceId();
    
}

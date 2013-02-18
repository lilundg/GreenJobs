/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.model.dto;

import andlin.recruit.model.Availability;
import andlin.recruit.model.CompetenceProfile;
import andlin.recruit.model.Role;
import java.util.Collection;

/**
 * This represents a read only view of the Person entity class.
 * Only Person getters are exposed via this interface. 
 */
public interface PersonDTO {

    public Long getPersonId();

    public String getName();

    public String getSurname();

    public String getSsn();

    public String getEmail();

    public String getPassword();

    public String getUsername();

    public Role getRoleId();

    public Collection<CompetenceProfile> getCompetenceProfileCollection();

    public Collection<Availability> getAvailabilityCollection();

}

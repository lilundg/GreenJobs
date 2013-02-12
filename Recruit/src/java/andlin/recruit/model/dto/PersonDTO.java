/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.model.dto;

import andlin.recruit.model.Availability;
import andlin.recruit.model.CompetenceProfile;
import andlin.recruit.model.Role;
import java.util.Collection;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author pinballmilitia
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

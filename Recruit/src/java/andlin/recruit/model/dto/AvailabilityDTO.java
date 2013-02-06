/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.model.dto;

import andlin.recruit.model.Person;
import java.util.Date;

/**
 *
 * @author pinballmilitia
 */
public interface AvailabilityDTO {

    public Long getAvailabilityId();

    public Date getFromDate();

    public Date getToDate();

    public Person getPersonId();
}

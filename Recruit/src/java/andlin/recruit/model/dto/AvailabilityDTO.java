/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.model.dto;

import andlin.recruit.model.Person;
import java.util.Date;

/**
 * Represents a read only view of the Availability entity class
 */
public interface AvailabilityDTO {

    public Long getAvailabilityId();

    public Date getFromDate();

    public Date getToDate();

    public Person getPersonId();
}

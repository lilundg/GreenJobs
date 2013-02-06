/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.view.conversion;

import andlin.recruit.controller.RegistrationController;
import andlin.recruit.model.Competence;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Named;

/**
 *
 * @author pinballmilitia
 */
@Named(value = "availabilityConverter")
@RequestScoped
public class AvailabilityConverter implements Converter {

    //Controller
    @EJB
    private RegistrationController personFacade;

    /**
     * Creates .a new instance of NewJSFManagedBean
     */
    public AvailabilityConverter() {
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            return personFacade.findCompetenceByName(value);
        } catch (Exception e) {
            throw new ConverterException(new FacesMessage(String.format("Cannot convert %s to Competence", value)), e);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (!(value instanceof Competence)) {
            return null;
        }

        return ((Competence) value).getName();
    }
}
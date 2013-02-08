/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.view.conversion;

import andlin.recruit.controller.RegistrationController;
import andlin.recruit.model.Competence;
import andlin.recruit.model.dto.CompetenceDTO;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Named;

/**
 *  Converts from CompetenceDTO -> String and vice versa
 */
@Named(value = "competenceDTOConverter")
@RequestScoped
public class CompetenceDTOConverter implements Converter {

    //Controller
    @EJB
    private RegistrationController personFacade;

    /**
     * Creates .a new instance of NewJSFManagedBean
     */
    public CompetenceDTOConverter() {
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            return personFacade.findCompetenceByName(value);
        } catch (Exception e) {
            throw new ConverterException(new FacesMessage(String.format("Cannot convert %s to CompetenceDTO", value)), e);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (!(value instanceof CompetenceDTO)) {
            return null;
        }

        return ((CompetenceDTO) value).getName();
    }
}
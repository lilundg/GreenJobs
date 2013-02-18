package andlin.recruit.view;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

/**
 * Session bean storing the chosen locale and allowing the user
 * to change locale
 * 
 * @author Linus
 */
@Named("language")
@SessionScoped
public class LocaleBean implements Serializable{
    private String locale;
    private Map<String,Object> countries;
    
    /**
     * Constructor getting the current locale and creating a HashMap of available locales.
     */
    public LocaleBean(){
        locale = FacesContext.getCurrentInstance().getViewRoot().getLocale().toString();
        countries = new LinkedHashMap<String,Object>();
        countries.put("English", new Locale("en"));
        countries.put("Svenska", new Locale("sv"));
    }
    
    // Getter
    public Map<String,Object> getCountries(){
        return countries;
    }
    
    // Setter
    public void setLocale(String locale){
        this.locale = locale;
    }
    
    // Getter
    public String getLocale(){
        return locale;
    }
    
    /**
     * Changes the locale to a user specified locale
     * 
     * @param e ValueChangedEvent containing the new locale
     */
    public void localeChanged(ValueChangeEvent e){
        String newLocale = e.getNewValue().toString();
        
        for(Map.Entry<String,Object> entry: countries.entrySet()){
            if(entry.getValue().toString().equals(newLocale)){
                FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale)entry.getValue());
            }
        }
    }
}

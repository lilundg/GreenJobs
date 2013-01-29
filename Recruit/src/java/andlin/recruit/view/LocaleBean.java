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
 *
 * @author Linus
 */

@Named("language")
@SessionScoped
public class LocaleBean implements Serializable{
    private String locale;
    private Map<String,Object> countries;
    
    public LocaleBean(){
        countries = new LinkedHashMap<String,Object>();
        countries.put("English", new Locale("en"));
        countries.put("Svenska", new Locale("sv"));
    }
    
    public Map<String,Object> getCountries(){
        return countries;
    }
    
    public void setLocale(String locale){
        this.locale = locale;
    }
    
    public String getLocale(){
        return locale;
    }
    
    public void localeChanged(ValueChangeEvent e){
        String newLocale = e.getNewValue().toString();
        
        for(Map.Entry<String,Object> entry: countries.entrySet()){
            if(entry.getValue().toString().equals(newLocale)){
                FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale)entry.getValue());
            }
        }
    }
}

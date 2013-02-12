/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.util;

import java.util.logging.Logger;
import javax.enterprise.inject.Produces;

/**
 *
 * @author Linus
 */
public class LoggerFactory {
    
    @Produces public Logger createLogger(){
        return Logger.getLogger("andlin.recruit.logger");
    }
    
}

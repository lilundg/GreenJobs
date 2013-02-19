        /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.view;

import andlin.recruit.controller.RecruitmentController;
import andlin.recruit.model.Availability;
import andlin.recruit.model.CompetenceProfile;
import andlin.recruit.model.dto.AvailabilityDTO;
import andlin.recruit.model.dto.CompetenceDTO;
import andlin.recruit.model.dto.PersonDTO;
import andlin.recruit.model.dto.RecruitmentQueryDTO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPHeaderCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

/**
 * This backing bean forwards calls from client to controller
 */
@Named(value = "recruitmentManager")
@SessionScoped
public class RecruitmentManager implements Serializable {

    @EJB
    private RecruitmentController recruitmentController;
    private DataModel<PersonDTO> persons;
    private PersonDTO currentPerson;
    private List<CompetenceDTO> competences;
    private Date fromDate;
    private Date toDate;
    //Search variables
    private String firstName;
    private String surName;
    private CompetenceDTO competence;

    /**
     * Creates a new instance of TestManager
     */
    public RecruitmentManager() {
    }

    /**
     * Fetches all available competences
     */
    @PostConstruct
    public void init() {
        competences = recruitmentController.getCompetences();
    }

    
    /**
     * 
     */
    public void search(){
        persons = recruitmentController.search(new RecruitmentQueryDTO(firstName, surName, fromDate, toDate, competence));
    }

    /**
     * Allows caller to view all details of application
     *
     * @return 'success'....always
     */
    public String show() {
        currentPerson = persons.getRowData();
        return "success";
    }

    /**
     * Accept application of currentPerson
     */
    public void accept() {
        recruitmentController.accept(currentPerson);
    }

    /**
     * Reject application of currentPerson
     */
    public void reject() {
        recruitmentController.reject(currentPerson);
    }
    
    public void createPDF(){
        FacesContext context = FacesContext.getCurrentInstance(); 
        HttpServletResponse response = (HttpServletResponse)context.getExternalContext().getResponse();  
        response.setContentType("application/pdf");  
        response.setHeader("Content-disposition",  "inline=filename=file.pdf");
        try {
            Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            ResourceBundle rb = ResourceBundle.getBundle("andlin.recruit.locale.messages", locale);
            PersonDTO person = persons.getRowData();
            Iterator<CompetenceProfile> competence = person.getCompetenceProfileCollection().iterator();
            Iterator<Availability> available = person.getAvailabilityCollection().iterator();
            // step 1: create document
            Document document = new Document();
            // step 2: Create temporary memory storage
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            // step 3: Open the document
            document.open();
            // step 4: Format the PDF
            // Personal info
            document.add(new Paragraph(String.format("%s: %s", rb.getString("register.firstName"), person.getName())));
            document.add(new Paragraph(String.format("%s: %s", rb.getString("register.lastName"), person.getSurname())));
            document.add(new Paragraph(String.format("%s: %s", rb.getString("register.ssn"), person.getSsn())));
            document.add(new Paragraph(String.format("%s: %s", rb.getString("register.email"), person.getEmail())));
            document.add(new Paragraph(" "));
            // tables
            PdfPTable table;
            PdfPCell cell;
            // Competence table
            table = new PdfPTable(2);
            cell = new PdfPCell(new Phrase(rb.getString("manage.table.competenceprofile")));
            cell.setColspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(rb.getString("register.competence")));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(rb.getString("register.yearsofexperience")));
            table.addCell(cell);
            CompetenceProfile comp;
            while(competence.hasNext()){
                comp = competence.next();
                cell = new PdfPCell(new Phrase(comp.getCompetenceId().getName()));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(comp.getYearsOfExperience().toString()));
                table.addCell(cell);
            }
            document.add(table);
            document.add(new Paragraph(" "));
            // Availability table
            table = new PdfPTable(2);
            cell = new PdfPCell(new Phrase(rb.getString("manage.table.availability")));
            cell.setColspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(rb.getString("register.from")));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(rb.getString("register.to")));
            table.addCell(cell);
            Availability availability;
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            while(available.hasNext()){
                availability = available.next();
                cell = new PdfPCell(new Phrase(formatter.format(availability.getFromDate())));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(formatter.format(availability.getToDate())));
                table.addCell(cell);
            }
            document.add(table);
            // step 5: Finish the document
            document.close();

            // setting some response headers
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control",
                "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            // setting the content type
            response.setContentType("application/pdf");
            // the contentlength
            response.setContentLength(baos.size());
            // write ByteArrayOutputStream to the ServletOutputStream
            OutputStream os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
            os.close();
          //  log.debug("flushed and closed the outputstream");

        }
        catch(DocumentException e) {
        //    log.error("error: "+e);
        }
        catch (IOException e) {
      //      log.error("error: "+e);
        }
        catch (Exception ex) {
    //        log.debug("error: " + ex.getMessage());
        }
    context.responseComplete();


    }

    
    /***********************
     * GETTERS AND SETTERS *
     ***********************/
    public DataModel<PersonDTO> getPersons() {
        return persons;
    }

    public void setPersons(DataModel<PersonDTO> persons) {
        this.persons = persons;
    }

    public PersonDTO getCurrentPerson() {
        return currentPerson;
    }

    public void setCurrentPerson(PersonDTO currentPerson) {
        this.currentPerson = currentPerson;
    }

    public CompetenceDTO getCompetence() {
        return competence;
    }

    public void setCompetence(CompetenceDTO competence) {
        this.competence = competence;
    }

    public List<CompetenceDTO> getCompetences() {
        return competences;
    }

    public void setCompetences(List<CompetenceDTO> competences) {
        this.competences = competences;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
    
    
}

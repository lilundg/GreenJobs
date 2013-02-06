/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package andlin.recruit.model;

import andlin.recruit.model.dto.CompetenceDTO;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author pinballmilitia
 */
@Entity
@Table(name = "competence")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Competence.findAll", query = "SELECT c FROM Competence c"),
    @NamedQuery(name = "Competence.findByCompetenceId", query = "SELECT c FROM Competence c WHERE c.competenceId = :competenceId"),
    @NamedQuery(name = "Competence.findByName", query = "SELECT c FROM Competence c WHERE c.name = :name")})
public class Competence implements CompetenceDTO, Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    //@NotNull
    @Column(name = "competence_id")
    private Long competenceId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "competenceId")
    private Collection<CompetenceProfile> competenceProfileCollection;

    public Competence() {
    }

    public Competence(Long competenceId) {
        this.competenceId = competenceId;
    }

    public Competence(Long competenceId, String name) {
        this.competenceId = competenceId;
        this.name = name;
    }

    public Long getCompetenceId() {
        return competenceId;
    }

    public void setCompetenceId(Long competenceId) {
        this.competenceId = competenceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<CompetenceProfile> getCompetenceProfileCollection() {
        return competenceProfileCollection;
    }

    public void setCompetenceProfileCollection(Collection<CompetenceProfile> competenceProfileCollection) {
        this.competenceProfileCollection = competenceProfileCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (competenceId != null ? competenceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Competence)) {
            return false;
        }
        Competence other = (Competence) object;
        if ((this.competenceId == null && other.competenceId != null) || (this.competenceId != null && !this.competenceId.equals(other.competenceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getName();
    }
    
}

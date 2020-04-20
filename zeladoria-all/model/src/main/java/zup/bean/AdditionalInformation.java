package zup.bean;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "additional_information")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AdditionalInformation.findAll", query = "SELECT a FROM AdditionalInformation a"),
    @NamedQuery(name = "AdditionalInformation.findByAdditionalInformationId", query = "SELECT a FROM AdditionalInformation a WHERE a.additionalInformationId = :additionalInformationId"),
    @NamedQuery(name = "AdditionalInformation.findByInformation", query = "SELECT a FROM AdditionalInformation a WHERE a.information = :information")})
public class AdditionalInformation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "additional_information_id")
    private Integer additionalInformationId;
    @Basic(optional = false)
    @Column(name = "information")
    private String information;

    public AdditionalInformation() {
    }
    public AdditionalInformation(AdditionalInformation ai) {
    this.additionalInformationId=ai.getAdditionalInformationId();
    this.information=ai.getInformation();
    }
    public AdditionalInformation(Integer additionalInformationId) {
        this.additionalInformationId = additionalInformationId;
    }

    public AdditionalInformation(Integer additionalInformationId, String information) {
        this.additionalInformationId = additionalInformationId;
        this.information = information;
    }

    public Integer getAdditionalInformationId() {
        return additionalInformationId;
    }

    public void setAdditionalInformationId(Integer additionalInformationId) {
        this.additionalInformationId = additionalInformationId;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (additionalInformationId != null ? additionalInformationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AdditionalInformation)) {
            return false;
        }
        AdditionalInformation other = (AdditionalInformation) object;
        if ((this.additionalInformationId == null && other.additionalInformationId != null) || (this.additionalInformationId != null && !this.additionalInformationId.equals(other.additionalInformationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication3.AdditionalInformation[ additionalInformationId=" + additionalInformationId + " ]";
    }
    
}

package zup.bean;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "configuration")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Configuration.findAll", query = "SELECT c FROM Configuration c"),
    @NamedQuery(name = "Configuration.findByCityId", query = "SELECT c FROM Configuration c WHERE c.configurationId = :configurationId"),
    @NamedQuery(name = "Configuration.findByName", query = "SELECT c FROM Configuration c WHERE c.name = :name")})
public class Configuration implements Serializable {
	
	private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "configuration_id")
    private Integer configurationId;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "slogan")
    private String slogan;
    
    @Column(name = "logo")
    private String logo;
    
    @Column(name = "prefeito")
    private String prefeito;
    
    @Column(name = "vice_prefeito")
    private String vicePrefeito;
    
    @Column(name = "emergency_number")
    private Integer emergencyNumber;

	public Integer getConfigurationId() {
		return configurationId;
	}

	public void setConfigurationId(Integer configurationId) {
		this.configurationId = configurationId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getPrefeito() {
		return prefeito;
	}

	public void setPrefeito(String prefeito) {
		this.prefeito = prefeito;
	}

	public String getVicePrefeito() {
		return vicePrefeito;
	}

	public void setVicePrefeito(String vicePrefeito) {
		this.vicePrefeito = vicePrefeito;
	}

	public Integer getEmergencyNumber() {
		return emergencyNumber;
	}

	public void setEmergencyNumber(Integer emergencyNumber) {
		this.emergencyNumber = emergencyNumber;
	}    
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (configurationId != null ? configurationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Configuration)) {
            return false;
        }
        Configuration other = (Configuration) object;
        if ((this.configurationId == null && other.configurationId != null) || (this.configurationId != null && !this.configurationId.equals(other.configurationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication3.Address[ addressId=" + configurationId + " ]";
    }
	
}

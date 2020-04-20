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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "address")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Address.findAll", query = "SELECT a FROM Address a"),
    @NamedQuery(name = "Address.findByAddressId", query = "SELECT a FROM Address a WHERE a.addressId = :addressId"),
    @NamedQuery(name = "Address.findByStreetName", query = "SELECT a FROM Address a WHERE a.streetName = :streetName"),
    @NamedQuery(name = "Address.findByAddressNumber", query = "SELECT a FROM Address a WHERE a.addressNumber = :addressNumber"),
    @NamedQuery(name = "Address.findByGeograficalCoordinates", query = "SELECT a FROM Address a WHERE a.geograficalCoordinates = :geograficalCoordinates")})
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "address_id")
    private Integer addressId;
    @Column(name = "street_name")
    private String streetName;
    @Column(name = "address_number")
    private Integer addressNumber;
    @Column(name = "geografical_coordinates")
    private String geograficalCoordinates;
    @Column(name = "complement")
    private String complement;  
//    @JsonIgnore
 //   @OneToMany(cascade = CascadeType.ALL, mappedBy = "addressId")
  //  private Collection<Citizen> citizenCollection;
    @JoinColumn(name = "neighborhood_id", referencedColumnName = "neighborhood_id")
    @ManyToOne(optional = false)
    private Neighborhood neighborhoodId;
    public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "addressId")
    private Collection<SolvedCall> solvedCallCollection;

    public Address() {
    }

    public Address(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public Integer getAddressNumber() {
        return addressNumber;
    }

    public void setAddressNumber(Integer addressNumber) {
        this.addressNumber = addressNumber;
    }

    public String getGeograficalCoordinates() {
        return geograficalCoordinates;
    }

    public void setGeograficalCoordinates(String geograficalCoordinates) {
        this.geograficalCoordinates = geograficalCoordinates;
    }

//    @XmlTransient
//    public Collection<Citizen> getCitizenCollection() {
//        return citizenCollection;
//    }
//
//    public void setCitizenCollection(Collection<Citizen> citizenCollection) {
//        this.citizenCollection = citizenCollection;
//    }

    public Neighborhood getNeighborhoodId() {
        return neighborhoodId;
    }

    public void setNeighborhoodId(Neighborhood neighborhoodId) {
        this.neighborhoodId = neighborhoodId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (addressId != null ? addressId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Address)) {
            return false;
        }
        Address other = (Address) object;
        if ((this.addressId == null && other.addressId != null) || (this.addressId != null && !this.addressId.equals(other.addressId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication3.Address[ addressId=" + addressId + " ]";
    }
    
}

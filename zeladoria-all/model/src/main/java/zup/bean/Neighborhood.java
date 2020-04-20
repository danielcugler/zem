/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

import zup.enums.Enabled;

/**
 *
 * @author Labitec01
 */
@Entity
@Table(name = "neighborhood")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Neighborhood.findAll", query = "SELECT n FROM Neighborhood n"),
    @NamedQuery(name = "Neighborhood.findByNeighborhoodId", query = "SELECT n FROM Neighborhood n WHERE n.neighborhoodId = :neighborhoodId"),
    @NamedQuery(name = "Neighborhood.findByName", query = "SELECT n FROM Neighborhood n WHERE n.name = :name"),
    @NamedQuery(name = "Neighborhood.findByCityAndNeighborhood", query = "SELECT n FROM Neighborhood n WHERE n.cityId.cityId = :cityId AND upper(unaccent(n.name)) = :name"),
    @NamedQuery(name = "Neighborhood.findByNomeAbrev", query = "SELECT n FROM Neighborhood n WHERE n.nomeAbrev = :nomeAbrev")})
public class Neighborhood implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "neighborhood_id")
    private Integer neighborhoodId;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "nome_abrev")
    private String nomeAbrev;
    @OneToMany(mappedBy = "neighborhoodId")
    private Collection<Citizen> citizenCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "neighborhoodId")
    private Collection<Address> addressCollection;
    @JoinColumn(name = "city_id", referencedColumnName = "city_id")
    @ManyToOne(optional = false)
    private City cityId;

    public Neighborhood() {
    }

    public Neighborhood(Integer neighborhoodId) {
        this.neighborhoodId = neighborhoodId;
    }

    public Neighborhood(Integer neighborhoodId, String name) {
        this.neighborhoodId = neighborhoodId;
        this.name = name;
    }

    public Integer getNeighborhoodId() {
        return neighborhoodId;
    }

    public void setNeighborhoodId(Integer neighborhoodId) {
        this.neighborhoodId = neighborhoodId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNomeAbrev() {
        return nomeAbrev;
    }

    public void setNomeAbrev(String nomeAbrev) {
        this.nomeAbrev = nomeAbrev;
    }

    @XmlTransient
    public Collection<Citizen> getCitizenCollection() {
        return citizenCollection;
    }

    public void setCitizenCollection(Collection<Citizen> citizenCollection) {
        this.citizenCollection = citizenCollection;
    }

    @XmlTransient
    public Collection<Address> getAddressCollection() {
        return addressCollection;
    }

    public void setAddressCollection(Collection<Address> addressCollection) {
        this.addressCollection = addressCollection;
    }

    public City getCityId() {
        return cityId;
    }

    public void setCityId(City cityId) {
        this.cityId = cityId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (neighborhoodId != null ? neighborhoodId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Neighborhood)) {
            return false;
        }
        Neighborhood other = (Neighborhood) object;
        if ((this.neighborhoodId == null && other.neighborhoodId != null) || (this.neighborhoodId != null && !this.neighborhoodId.equals(other.neighborhoodId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.Neighborhood[ neighborhoodId=" + neighborhoodId + " ]";
    }
    
	public String makeLog(){
		return "ID Bairro: "+this.getNeighborhoodId()+ "\nNome: "+ this.getName() + "\nCidade: "+this.getCityId().getName();
	}
    
}

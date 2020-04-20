package bean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Labitec01
 */
@Entity
@Table(name = "city")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "City.findAll", query = "SELECT c FROM City c"),
		@NamedQuery(name = "City.findByCityId", query = "SELECT c FROM City c WHERE c.cityId = :cityId"),
		@NamedQuery(name = "City.findByName", query = "SELECT c FROM City c WHERE c.name = :name") })
public class City implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "city_id")
	private Integer cityId;
	@Column(name = "name")
	private String name;
	@Column(name = "web_address")
	private String webAddress;
	@JoinColumn(name = "state_id", referencedColumnName = "state_id")
	@ManyToOne
	private State stateId;

	public City() {
	}

	public City(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWebAddress() {
		return webAddress;
	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}

	public State getStateId() {
		return stateId;
	}

	public void setStateId(State stateId) {
		this.stateId = stateId;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (cityId != null ? cityId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof City)) {
			return false;
		}
		City other = (City) object;
		if ((this.cityId == null && other.cityId != null)
				|| (this.cityId != null && !this.cityId.equals(other.cityId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "zem.admin.City[ cityId=" + cityId + " ]";
	}

}

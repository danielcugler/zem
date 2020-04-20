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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "system_parameter")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "SystemParameter.findAll", query = "SELECT s FROM SystemParameter s"),
		@NamedQuery(name = "SystemParameter.findBySystemParameterId", query = "SELECT s FROM SystemParameter s WHERE s.systemParameterId = :systemParameterId"),
		@NamedQuery(name = "SystemParameter.findByNumberOfDaysToSolveACall", query = "SELECT s FROM SystemParameter s WHERE s.numberOfDaysToSolveACall = :numberOfDaysToSolveACall"),
		@NamedQuery(name = "SystemParameter.findByCityHallName", query = "SELECT s FROM SystemParameter s WHERE s.cityHallName = :cityHallName"),
		@NamedQuery(name = "SystemParameter.findByCityName", query = "SELECT s FROM SystemParameter s WHERE s.cityName = :cityName"),
		@NamedQuery(name = "SystemParameter.findByStateName", query = "SELECT s FROM SystemParameter s WHERE s.stateName = :stateName"),
		@NamedQuery(name = "SystemParameter.findByStateAcronym", query = "SELECT s FROM SystemParameter s WHERE s.stateAcronym = :stateAcronym"),
		@NamedQuery(name = "SystemParameter.findByGoogleMapsKey", query = "SELECT s FROM SystemParameter s WHERE s.googleMapsKey = :googleMapsKey") })

public class SystemParameter implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "system_parameter_id")
	private Integer systemParameterId;
	@Basic(optional = false)
	@NotNull
	@Column(name = "number_of_days_to_solve_a_call")
	private int numberOfDaysToSolveACall;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 60)
	@Column(name = "city_hall_name")
	private String cityHallName;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 30)
	@Column(name = "city_name")
	private String cityName;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 30)
	@Column(name = "state_name")
	private String stateName;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2)
	@Column(name = "state_acronym")
	private String stateAcronym;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 200)
	@Column(name = "google_maps_key")
	private String googleMapsKey;

	public SystemParameter() {
	}

	public SystemParameter(Integer systemParameterId) {
		this.systemParameterId = systemParameterId;
	}

	public SystemParameter(Integer systemParameterId, int numberOfDaysToSolveACall, String cityHallName,
			String cityName, String stateName, String stateAcronym, String googleMapsKey) {
		this.systemParameterId = systemParameterId;
		this.numberOfDaysToSolveACall = numberOfDaysToSolveACall;
		this.cityHallName = cityHallName;
		this.cityName = cityName;
		this.stateName = stateName;
		this.stateAcronym = stateAcronym;
		this.googleMapsKey = googleMapsKey;
	}

	public Integer getSystemParameterId() {
		return systemParameterId;
	}

	public void setSystemParameterId(Integer systemParameterId) {
		this.systemParameterId = systemParameterId;
	}

	public int getNumberOfDaysToSolveACall() {
		return numberOfDaysToSolveACall;
	}

	public void setNumberOfDaysToSolveACall(int numberOfDaysToSolveACall) {
		this.numberOfDaysToSolveACall = numberOfDaysToSolveACall;
	}

	public String getCityHallName() {
		return cityHallName;
	}

	public void setCityHallName(String cityHallName) {
		this.cityHallName = cityHallName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getStateAcronym() {
		return stateAcronym;
	}

	public void setStateAcronym(String stateAcronym) {
		this.stateAcronym = stateAcronym;
	}

	public String getGoogleMapsKey() {
		return googleMapsKey;
	}

	public void setGoogleMapsKey(String googleMapsKey) {
		this.googleMapsKey = googleMapsKey;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (systemParameterId != null ? systemParameterId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof SystemParameter)) {
			return false;
		}
		SystemParameter other = (SystemParameter) object;
		if ((this.systemParameterId == null && other.systemParameterId != null)
				|| (this.systemParameterId != null && !this.systemParameterId.equals(other.systemParameterId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "banco.SystemParameter[ systemParameterId=" + systemParameterId + " ]";
	}

}

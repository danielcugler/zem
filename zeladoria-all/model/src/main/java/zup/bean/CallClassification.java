package zup.bean;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import zup.enums.Enabled;

@Entity
@Table(name = "call_classification")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "CallClassification.findAll", query = "SELECT c FROM CallClassification c"),
		@NamedQuery(name = "CallClassification.findByCallClassificationId", query = "SELECT c FROM CallClassification c WHERE c.callClassificationId = :callClassificationId"),
		@NamedQuery(name = "CallClassification.findByName", query = "SELECT c FROM CallClassification c WHERE c.name = :name") })

public class CallClassification implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "call_classification_id")
	private Integer callClassificationId;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "name")
	private String name;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "address_required")
	private Boolean addressRequired;
	@Basic(optional = false)
	@NotNull
	@Column(name = "enabled")
	@Enumerated(EnumType.ORDINAL)
	private Enabled enabled;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "callClassificationId")
	private Collection<UnsolvedCall> unsolvedCallCollection;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "callClassificationId")
	private Collection<SolvedCall> solvedCallCollection;

	public CallClassification() {
	}

	public CallClassification(Integer callClassificationId) {
		this.callClassificationId = callClassificationId;
	}

	public CallClassification(Integer callClassificationId, String name) {
		this.callClassificationId = callClassificationId;
		this.name = name;
	}

	public Integer getCallClassificationId() {
		return callClassificationId;
	}

	public void setCallClassificationId(Integer callClassificationId) {
		this.callClassificationId = callClassificationId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getAddressRequired() {
		return addressRequired;
	}

	public void setAddressRequired(Boolean addressRequired) {
		this.addressRequired = addressRequired;
	}

	@XmlTransient
	public Collection<UnsolvedCall> getUnsolvedCallCollection() {
		return unsolvedCallCollection;
	}

	public void setUnsolvedCallCollection(Collection<UnsolvedCall> unsolvedCallCollection) {
		this.unsolvedCallCollection = unsolvedCallCollection;
	}

	@XmlTransient
	public Collection<SolvedCall> getSolvedCallCollection() {
		return solvedCallCollection;
	}

	public void setSolvedCallCollection(Collection<SolvedCall> solvedCallCollection) {
		this.solvedCallCollection = solvedCallCollection;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (callClassificationId != null ? callClassificationId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof CallClassification)) {
			return false;
		}
		CallClassification other = (CallClassification) object;
		if ((this.callClassificationId == null && other.callClassificationId != null)
				|| (this.callClassificationId != null
						&& !this.callClassificationId.equals(other.callClassificationId))) {
			return false;
		}
		return true;
	}
	
	public String makeLog(){
		return "ID Call Classification: "+this.getCallClassificationId()+ "\nNome: "+ this.getName()+"\nRequerimento endereço: "+ this.getAddressRequired();
	}

	@Override
	public String toString() {
		return "banco.CallClassification[ callClassificationId=" + callClassificationId + " ]";
	}

}

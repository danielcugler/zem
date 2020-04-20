package zup.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import zup.enums.Enabled;
import zup.hateoas.Link;
import zup.messages.IMessages;

@Entity
@Table(name = "citizen")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Citizen.findAll", query = "SELECT c FROM Citizen c"),
		@NamedQuery(name = "Citizen.findByCitizenCpf", query = "SELECT c FROM Citizen c WHERE c.citizen_cpf = :citizen_cpf"),
		@NamedQuery(name = "Citizen.findByName", query = "SELECT c FROM Citizen c WHERE c.name = :name"),
		@NamedQuery(name = "Citizen.findByPhone_number", query = "SELECT c FROM Citizen c WHERE c.phone_number = :phone_number"),
		@NamedQuery(name = "Citizen.findByEmail", query = "SELECT c FROM Citizen c WHERE c.email = :email"),
		@NamedQuery(name = "Citizen.findByEnabled", query = "SELECT c FROM Citizen c WHERE c.enabled = :enabled"),
		@NamedQuery(name = "Citizen.findByPassword", query = "SELECT c FROM Citizen c WHERE c.password = :password"),
		@NamedQuery(name = "Citizen.findByFacebookId", query = "SELECT c FROM Citizen c WHERE c.facebookId = :facebookId"),
		@NamedQuery(name = "Citizen.findByGender", query = "SELECT c FROM Citizen c WHERE c.gender = :gender"),
		@NamedQuery(name = "Citizen.countCitizen", query = "SELECT COUNT(*) as total FROM Citizen") })
public class Citizen implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Size(min = 11, max = 12)
	@Column(name = "citizen_cpf")
	private String citizen_cpf;
	@NotEmpty(message = IMessages.NULL_FIELDS)
	@Size(min = 3, max = 40, message = IMessages.SIZE_LIMIT_EXCEEDS)
	@Column(name = "name")
	private String name;
	@Size(min = 10, max = 11)
	@Column(name = "phone_number")
	private String phone_number;
	@Size(min = 1, max = 100)
	@Column(name = "email")
	private String email;
	@Column(name = "enabled")
	@Enumerated(EnumType.ORDINAL)
	private Enabled enabled;
	@Size(min = 32, max = 32)
	@Column(name = "password")
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	@Column(name = "facebook_id")
	private String facebookId;
	@Column(name = "gender")
	private Integer gender;
	@Column(name = "public_key")
	@Size(min = 32, max = 42)
	private String publicKey;
	@Transient
	@JsonProperty("_links")
	List<Link> links = new ArrayList<Link>();
	@JoinColumn(name = "neighborhood_id", referencedColumnName = "neighborhood_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Neighborhood neighborhoodId;
	@ManyToMany
	@JoinTable(name = "read_broadcast_message", joinColumns = {
			@JoinColumn(name = "citizen_cpf") }, inverseJoinColumns = { @JoinColumn(name = "broadcast_message_id") })
	private List<BroadcastMessage> broadcastMessageCollection;

	public Citizen() {
	}

	public Citizen(String citizen_cpf, String name, String phone_number, String email, Enabled enabled, String password,
			String facebookId, Integer gender, Neighborhood neighborhoodId) {
		super();
		this.citizen_cpf = citizen_cpf;
		this.name = name;
		this.phone_number = phone_number;
		this.email = email;
		this.enabled = enabled;
		this.password = password;
		this.facebookId = facebookId;
		this.gender = gender;
		this.neighborhoodId = neighborhoodId;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCitizen_cpf() {
		return citizen_cpf;
	}

	public void setCitizen_cpf(String citizen_cpf) {
		this.citizen_cpf = citizen_cpf;
	}

	// @JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Enabled getEnabled() {
		return enabled;
	}

	public void setEnabled(Enabled enabled) {
		this.enabled = enabled;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public void changeEnabled() {
		if (this.enabled == Enabled.ENABLED)
			this.enabled = Enabled.DISABLED;
		else if (this.enabled == Enabled.DISABLED)
			this.enabled = Enabled.ENABLED;
	}

	public Neighborhood getNeighborhoodId() {
		return neighborhoodId;
	}

	@JsonIgnore
	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public void setNeighborhoodId(Neighborhood neighborhoodId) {
		this.neighborhoodId = neighborhoodId;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public void addLinks(String rel, String link) {
		Link l = new Link(rel, link);
		this.links.add(l);
	}

	public List<BroadcastMessage> getBroadcastMessageCollection() {
		return broadcastMessageCollection;
	}

	public void setBroadcastMessageCollection(List<BroadcastMessage> broadcastMessageCollection) {
		this.broadcastMessageCollection = broadcastMessageCollection;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (citizen_cpf != null ? citizen_cpf.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Citizen)) {
			return false;
		}
		Citizen other = (Citizen) object;
		if ((this.citizen_cpf == null && other.citizen_cpf != null)
				|| (this.citizen_cpf != null && !this.citizen_cpf.equals(other.citizen_cpf))) {
			return false;
		}
		return true;
	}

	public String makeLog() {
		return "Nome: " + name + "\nCPF: " + citizen_cpf + "\nEmail: " + email + "\nHabilitado: "
				+ (enabled.equals(Enabled.ENABLED) ? "ativo" : "inativo");
	}

}

package zup.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import zup.enums.Enabled;
import zup.hateoas.Link;

@Entity
@Table(name = "system_user")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "SystemUser.findAll", query = "SELECT s FROM SystemUser s"),
		@NamedQuery(name = "SystemUser.findBySystemUserUsername", query = "SELECT s FROM SystemUser s WHERE s.systemUserUsername = :systemUserUsername"),
		@NamedQuery(name = "SystemUser.findByPassword", query = "SELECT s FROM SystemUser s WHERE s.password = :password"),
		@NamedQuery(name = "SystemUser.findByName", query = "SELECT s FROM SystemUser s WHERE s.name = :name"),
		@NamedQuery(name = "SystemUser.findByEmail", query = "SELECT s FROM SystemUser s WHERE s.email = :email"),
		@NamedQuery(name = "SystemUser.findByCommercialPhone", query = "SELECT s FROM SystemUser s WHERE s.commercialPhone = :commercialPhone"),
		@NamedQuery(name = "SystemUser.findByPersonalPhone", query = "SELECT s FROM SystemUser s WHERE s.personalPhone = :personalPhone"),
		@NamedQuery(name = "SystemUser.findByEnabled", query = "SELECT s FROM SystemUser s WHERE s.enabled = :enabled"),
		@NamedQuery(name = "SystemUser.findBySector", query = "SELECT s FROM SystemUser s WHERE s.sector = :sector"),
		@NamedQuery(name = "SystemUser.findByJobPosition", query = "SELECT s FROM SystemUser s WHERE s.jobPosition = :jobPosition"),
		@NamedQuery(name = "SystemUser.findByProfile", query = "SELECT s FROM SystemUser s WHERE s.systemUserProfileId IN(SELECT systemUserProfileId FROM SystemUserProfile where systemUserProfileId = :systemUserProfileId)") })

public class SystemUser implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id

	@Basic(optional = false)
	@Size(min = 3, max = 20)
	@Column(name = "system_user_username")
	private String systemUserUsername;
	@Basic(optional = false)
	@NotNull
	@Size(min = 32, max = 32)
	@Column(name = "password")
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "name")
	private String name;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Email
	@Column(name = "email")
	private String email;
	@Basic(optional = false)
	@NotNull
	@Pattern(regexp = "(^$|[0-9]{10,11})")
	@Size(min = 1, max = 11)
	@Column(name = "commercial_phone")
	private String commercialPhone;
	@Size(max = 11)
	@Pattern(regexp = "(^$|[0-9]{10,11})")
	@Column(name = "personal_phone")
	private String personalPhone;
	@Basic(optional = false)
	@NotNull
	@Column(name = "enabled")
	@Enumerated(EnumType.ORDINAL)
	private Enabled enabled;
	@Size(max = 100)
	@Column(name = "sector")
	private String sector;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "job_position")
	private String jobPosition;

	private transient String photoPath;

	@Transient
	private byte[] photo;
	@Transient
	@JsonProperty("_links")
	List<Link> links = new ArrayList<Link>();
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "createdBy")
	private Collection<BroadcastMessage> broadcastMessageCollection;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "updatedOrModeratedBy")
	private Collection<UnsolvedCall> unsolvedCallCollection;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "updatedOrModeratedBy")
	private Collection<SolvedCall> solvedCallCollection;
	@JoinColumn(name = "works_at_entity", referencedColumnName = "entity_id")
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private zup.bean.Entity worksAtEntity;
	@JoinColumn(name = "system_user_profile_id", referencedColumnName = "system_user_profile_id")
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private SystemUserProfile systemUserProfileId;

	public SystemUser() {
	}

	public SystemUser(String systemUserUsername) {
		this.systemUserUsername = systemUserUsername;
	}

	public SystemUser(String systemUserUsername, String password, String name, String email, String commercialPhone,
			String personalPhone, Enabled enabled, String sector, String jobPosition, String photoPath,
			zup.bean.Entity worksAtSecretary, SystemUserProfile systemUserProfileId) {
		super();
		this.systemUserUsername = systemUserUsername;
		this.password = password;
		this.name = name;
		this.email = email;
		this.commercialPhone = commercialPhone;
		this.personalPhone = personalPhone;
		this.enabled = Enabled.ENABLED;
		this.sector = sector;
		this.jobPosition = jobPosition;
		this.photoPath = photoPath;
		this.worksAtEntity = worksAtSecretary;
		this.systemUserProfileId = systemUserProfileId;
	}

	public SystemUser(String systemUserUsername, String password, String name, String email, String commercialPhone,
			String personalPhone, int enabled, String sector, String jobPosition) {
		super();
		this.systemUserUsername = systemUserUsername;
		this.password = password;
		this.name = name;
		this.email = email;
		this.commercialPhone = commercialPhone;
		this.personalPhone = personalPhone;
		this.enabled = Enabled.fromValue(enabled);
		this.sector = sector;
		this.jobPosition = jobPosition;
	}

	public SystemUser(String name, String email, String commercialPhone, String password,
			SystemUserProfile systemUserProfileId, String jobPosition, String systemUserUsername,
			String personalPhone) {
		this.enabled = Enabled.ENABLED;
		this.name = name;
		this.email = email;
		this.commercialPhone = commercialPhone;
		this.systemUserProfileId = systemUserProfileId;
		this.jobPosition = jobPosition;
		this.systemUserUsername = systemUserUsername;
		this.password = password;
		this.personalPhone = personalPhone;
	}

	public SystemUser(String systemUserUsername, String password, String name, String email, String commercialPhone,
			String personalPhone, Enabled enabled, String sector, String jobPosition, byte[] photo,
			zup.bean.Entity worksAtSecretary, SystemUserProfile systemUserProfileId) {

		this.systemUserUsername = systemUserUsername;
		this.password = password;
		this.name = name;
		this.email = email;
		this.commercialPhone = commercialPhone;
		this.personalPhone = personalPhone;
		this.enabled = Enabled.ENABLED;
		this.sector = sector;
		this.jobPosition = jobPosition;
		this.photo = photo;
		this.worksAtEntity = worksAtSecretary;
		this.systemUserProfileId = systemUserProfileId;
	}

	public SystemUser(String name, String email, String commercialPhone, String password,
			SystemUserProfile systemUserProfileId, String jobPosition, String systemUserUsername) {
		this.enabled = Enabled.ENABLED;
		this.name = name;
		this.email = email;
		this.commercialPhone = commercialPhone;
		this.systemUserProfileId = systemUserProfileId;
		this.jobPosition = jobPosition;
		this.systemUserUsername = systemUserUsername;
		this.password = password;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public String getSystemUserUsername() {
		return systemUserUsername;
	}

	public void setSystemUserUsername(String systemUserUsername) {
		this.systemUserUsername = systemUserUsername;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCommercialPhone() {
		return commercialPhone;
	}

	public void setCommercialPhone(String commercialPhone) {
		this.commercialPhone = commercialPhone;
	}

	public String getPersonalPhone() {
		return personalPhone;
	}

	public void setPersonalPhone(String personalPhone) {
		this.personalPhone = personalPhone;
	}

	public Enabled getEnabled() {
		return enabled;
	}

	public void setEnabled(Enabled enabled) {
		this.enabled = enabled;
	}

	public void changeEnabled() {
		if (this.enabled == Enabled.ENABLED)
			this.enabled = Enabled.DISABLED;
		else if (this.enabled == Enabled.DISABLED)
			this.enabled = Enabled.ENABLED;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getJobPosition() {
		return jobPosition;
	}

	public void setJobPosition(String jobPosition) {
		this.jobPosition = jobPosition;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public Collection<BroadcastMessage> getBroadcastMessageCollection() {
		return broadcastMessageCollection;
	}

	public void setBroadcastMessageCollection(Collection<BroadcastMessage> broadcastMessageCollection) {
		this.broadcastMessageCollection = broadcastMessageCollection;
	}

	public Collection<UnsolvedCall> getUnsolvedCallCollection() {
		return unsolvedCallCollection;
	}

	public void setUnsolvedCallCollection(Collection<UnsolvedCall> unsolvedCallCollection) {
		this.unsolvedCallCollection = unsolvedCallCollection;
	}

	public Collection<SolvedCall> getSolvedCallCollection() {
		return solvedCallCollection;
	}

	public void setSolvedCallCollection(Collection<SolvedCall> solvedCallCollection) {
		this.solvedCallCollection = solvedCallCollection;
	}

	public zup.bean.Entity getWorksAtEntity() {
		return worksAtEntity;
	}

	public void setWorksAtEntity(zup.bean.Entity worksAtEntity) {
		this.worksAtEntity = worksAtEntity;
	}

	public SystemUserProfile getSystemUserProfileId() {
		return systemUserProfileId;
	}

	public void setSystemUserProfileId(SystemUserProfile systemUserProfileId) {
		this.systemUserProfileId = systemUserProfileId;
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

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (systemUserUsername != null ? systemUserUsername.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof SystemUser)) {
			return false;
		}
		SystemUser other = (SystemUser) object;
		if ((this.systemUserUsername == null && other.systemUserUsername != null)
				|| (this.systemUserUsername != null && !this.systemUserUsername.equals(other.systemUserUsername))) {
			return false;
		}
		return true;
	}

	public String makeLog() {
		String entidade = worksAtEntity != null ? worksAtEntity.getName() : "Todas";

		return "Usuário: " + systemUserUsername + 		"\nsenha: " + "****" + "\nNome: " + name
				+ "\nE-mail: " + email + "\nTelefone Comercial: " + commercialPhone + "\nTelefone Pessoal: " + personalPhone
				+ "\nHabilitado: " +(enabled.equals(Enabled.ENABLED)?"ativo":"inativo") + "\nSetor: " + sector + "\nCargo: " + jobPosition + "\nEntidade: " + entidade + "\nPerfil: "
				+ systemUserProfileId.getName();
	}
}

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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import zup.enums.Enabled;
import zup.hateoas.Link;

@Entity
@Table(name = "system_user_profile")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "SystemUserProfile.findAll", query = "SELECT s FROM SystemUserProfile s"),
	@NamedQuery(name = "SystemUserProfile.findRoles", query = "SELECT sp.role FROM SystemUserProfile s JOIN s.systemUserProfilePermissionCollection sp WHERE s.systemUserProfileId = :systemUserProfileId"),
	
	@NamedQuery(name = "SystemUserProfile.findBySystemUserProfileId", query = "SELECT s FROM SystemUserProfile s WHERE s.systemUserProfileId = :systemUserProfileId"),
		@NamedQuery(name = "SystemUserProfile.findByName", query = "SELECT s FROM SystemUserProfile s WHERE s.name = :name"),
		@NamedQuery(name = "SystemUserProfile.findByEnabled", query = "SELECT s FROM SystemUserProfile s WHERE s.enabled = :enabled") })

public class SystemUserProfile implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "system_user_profile_id")
	private Integer systemUserProfileId;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "name")
	private String name;
	@Basic(optional = false)
	@NotNull
	@Column(name = "enabled")
	@Enumerated(EnumType.ORDINAL)
	private Enabled enabled;
	@Transient
	@JsonProperty("_links")
	List<Link> links = new ArrayList<Link>();
	@JoinTable(name = "system_user_profile_system_user_profile_permission_maps", joinColumns = {
			@JoinColumn(name = "system_user_profile_id", referencedColumnName = "system_user_profile_id") }, inverseJoinColumns = {
					@JoinColumn(name = "system_user_profile_permission_id", referencedColumnName = "system_user_profile_permission_id") })
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Collection<SystemUserProfilePermission> systemUserProfilePermissionCollection;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "systemUserProfileId")
	private Collection<SystemUser> systemUserCollection;

	public SystemUserProfile() {
	}

	public SystemUserProfile(Integer systemUserProfileId) {
		this.systemUserProfileId = systemUserProfileId;
	}

	public SystemUserProfile(String name, Enabled enabled) {

		this.name = name;
		this.enabled = enabled;
	}

	public SystemUserProfile(Integer systemUserProfileId, String name, Enabled enabled) {
		this.systemUserProfileId = systemUserProfileId;
		this.name = name;
		this.enabled = enabled;
	}

	public Integer getSystemUserProfileId() {
		return systemUserProfileId;
	}

	public void setSystemUserProfileId(Integer systemUserProfileId) {
		this.systemUserProfileId = systemUserProfileId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Collection<SystemUserProfilePermission> getSystemUserProfilePermissionCollection() {
		return systemUserProfilePermissionCollection;
	}

	public void setSystemUserProfilePermissionCollection(
			Collection<SystemUserProfilePermission> systemUserProfilePermissionCollection) {
		this.systemUserProfilePermissionCollection = systemUserProfilePermissionCollection;
	}

	public void addSystemUserProfilePermission(SystemUserProfilePermission sup) {
		this.systemUserProfilePermissionCollection.add(sup);
	}

	public void removeSystemUserProfilePermission(SystemUserProfilePermission sup) {
		this.systemUserProfilePermissionCollection.remove(sup);
	}

	public Collection<SystemUser> getSystemUserCollection() {
		return systemUserCollection;
	}

	public void setSystemUserCollection(Collection<SystemUser> systemUserCollection) {
		this.systemUserCollection = systemUserCollection;
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
/*	
	public List<String> getRoles(){
		List<String> roles = new ArrayList<String>();
		for(SystemUserProfilePermission permission: this.systemUserProfilePermissionCollection)
			roles.add(permission.getRole());
		return roles;
	}
	*/
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (systemUserProfileId != null ? systemUserProfileId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof SystemUserProfile)) {
			return false;
		}
		SystemUserProfile other = (SystemUserProfile) object;
		if ((this.systemUserProfileId == null && other.systemUserProfileId != null)
				|| (this.systemUserProfileId != null && !this.systemUserProfileId.equals(other.systemUserProfileId))) {
			return false;
		}
		return true;
	}

	public String makeLog(){
		String s= "Id: "+systemUserProfileId+"\nNome: "+name+"\nPermissões: "+"\nHabilitado: "+(enabled.equals(Enabled.ENABLED)?"ativo":"inativo");
	for(SystemUserProfilePermission supp:systemUserProfilePermissionCollection)
	s+="\n\t"+supp.getName();
	return s;
	}
	
	@Override
	public String toString() {
		return "banco.SystemUserProfile[ systemUserProfileId=" + systemUserProfileId + " ]";
	}

}

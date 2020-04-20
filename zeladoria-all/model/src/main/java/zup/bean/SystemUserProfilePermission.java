package zup.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "system_user_profile_permission")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "SystemUserProfilePermission.findAll", query = "SELECT s FROM SystemUserProfilePermission s"),
		@NamedQuery(name = "SystemUserProfilePermission.findBySystemUserProfilePermissionId", query = "SELECT s FROM SystemUserProfilePermission s WHERE s.systemUserProfilePermissionId = :systemUserProfilePermissionId"),
		@NamedQuery(name = "SystemUserProfilePermission.findByName", query = "SELECT s FROM SystemUserProfilePermission s WHERE s.name = :name") })

public class SystemUserProfilePermission implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "system_user_profile_permission_id")
	private Integer systemUserProfilePermissionId;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "name")
	private String name;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 60)
	@Column(name = "role")
	private String role;		
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "systemUserProfilePermissionCollection", cascade = CascadeType.ALL)
	private Collection<SystemUserProfile> systemUserProfileCollection;

	public SystemUserProfilePermission() {
	}

	public SystemUserProfilePermission(Integer systemUserProfilePermissionId) {
		this.systemUserProfilePermissionId = systemUserProfilePermissionId;
	}

	public SystemUserProfilePermission(Integer systemUserProfilePermissionId, String name) {
		this.systemUserProfilePermissionId = systemUserProfilePermissionId;
		this.name = name;
	}

	public Integer getSystemUserProfilePermissionId() {
		return systemUserProfilePermissionId;
	}

	public void setSystemUserProfilePermissionId(Integer systemUserProfilePermissionId) {
		this.systemUserProfilePermissionId = systemUserProfilePermissionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Collection<SystemUserProfile> getSystemUserProfileCollection() {
		return systemUserProfileCollection;
	}

	public void setSystemUserProfileCollection(Collection<SystemUserProfile> systemUserProfileCollection) {
		this.systemUserProfileCollection = systemUserProfileCollection;
	}
	


	@Override
	public int hashCode() {
		int hash = 0;
		hash += (systemUserProfilePermissionId != null ? systemUserProfilePermissionId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof SystemUserProfilePermission)) {
			return false;
		}
		SystemUserProfilePermission other = (SystemUserProfilePermission) object;
		if ((this.systemUserProfilePermissionId == null && other.systemUserProfilePermissionId != null)
				|| (this.systemUserProfilePermissionId != null
						&& !this.systemUserProfilePermissionId.equals(other.systemUserProfilePermissionId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "banco.SystemUserProfilePermission[ systemUserProfilePermissionId=" + systemUserProfilePermissionId
				+ " ]";
	}

}

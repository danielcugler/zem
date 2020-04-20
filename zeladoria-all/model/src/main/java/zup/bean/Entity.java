package zup.bean;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import zup.enums.Enabled;
import zup.hateoas.Link;
import zup.model.utils.HibernateUtil;
import zup.serializer.EntitySerializer;

@javax.persistence.Entity
@Table(name = "entity")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Entity.findAll", query = "SELECT e FROM Entity e"),
		@NamedQuery(name = "Entity.findByEntityId", query = "SELECT e FROM Entity e WHERE e.entityId = :entityId"),
		@NamedQuery(name = "Entity.findByName", query = "SELECT e FROM Entity e WHERE e.name = :name"),
		@NamedQuery(name = "Entity.findByEnabled", query = "SELECT e FROM Entity e WHERE e.enabled = :enabled") })
//@JsonSerialize(using = EntitySerializer.class)
public class Entity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "entity_id")
	private Integer entityId;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "name")
	private String name;
	@Basic(optional = false)
	@NotNull
	@Column(name = "enabled")
	@Enumerated(EnumType.ORDINAL)
	private Enabled enabled;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "icon")
	private String icon;
	@JoinTable(name = "entity_entity_category_maps", joinColumns = {
			@JoinColumn(name = "entity_id", referencedColumnName = "entity_id") }, inverseJoinColumns = {
					@JoinColumn(name = "entity_category_id", referencedColumnName = "entity_category_id") })
	@ManyToMany
	private Collection<EntityCategory> entityCategoryCollection;
	@OneToMany( mappedBy = "worksAtEntity")
	private Collection<SystemUser> systemUserCollection;
	@OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
	private AttendanceTime attendanceTime;
	@Transient
	@JsonProperty("_links")
List<Link> links=new ArrayList<Link>();
	
	public Entity() {
	}



	public Entity(Integer entityId, String name, Enabled enabled) {
		this.entityId = entityId;
		this.name = name;
		this.enabled = Enabled.ENABLED;
	}

	public Entity(String name, Enabled enabled) {
		this.name = name;
		this.enabled = Enabled.ENABLED;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
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
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void changeEnabled() {
		if (this.enabled == Enabled.ENABLED)
			this.enabled = Enabled.DISABLED;
		else if (this.enabled == Enabled.DISABLED)
			this.enabled = Enabled.ENABLED;
	}

	public Collection<EntityCategory> getEntityCategoryCollection() {
		return entityCategoryCollection;
	}

	public void addEntityCategoryCollection(EntityCategory entityCategory) {
		this.entityCategoryCollection.add(entityCategory);
	}

	public void removeEntityCategoryCollection(EntityCategory entityCategory) {
		this.entityCategoryCollection.remove(entityCategory);
	}

	public void removeAllEntityCategoryCollection(Collection<EntityCategory> entityCategory) {
		this.entityCategoryCollection.removeAll(entityCategory);
	}

	public void setEntityCategoryCollection(Collection<EntityCategory> entityCategoryCollection) {
		this.entityCategoryCollection = entityCategoryCollection;
	}

	public Collection<SystemUser> getSystemUserCollection() {
		return systemUserCollection;
	}

	public void setSystemUserCollection(Collection<SystemUser> systemUserCollection) {
		this.systemUserCollection = systemUserCollection;
	}

	public AttendanceTime getAttendanceTime() {
		return attendanceTime;
	}

	public void setAttendanceTime(AttendanceTime attendanceTime) {
		this.attendanceTime = attendanceTime;
	}

	
	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public void addLinks(String rel, String link) {
		Link l=new Link(rel,link);
		this.links.add(l);
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (entityId != null ? entityId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Entity)) {
			return false;
		}
		Entity other = (Entity) object;
		if ((this.entityId == null && other.entityId != null)
				|| (this.entityId != null && !this.entityId.equals(other.entityId))) {
			return false;
		}
		return true;
	}
	
	public String makeLog(){
		String log= "ID de Entidade: "+ entityId+"\nNome: "+name+"\nHabilitado: "+(enabled.equals(Enabled.ENABLED)?"ativo":"inativo")+"\nCategorias: ";
	for(EntityCategory entityCategory:entityCategoryCollection)
		log+="\n\t"+entityCategory.getName();
	return log;
	}

	
	
}

package zup.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "entity_entity_category_maps")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "EntityEntityCategoryMaps.findAll", query = "SELECT e FROM EntityEntityCategoryMaps e"),
		@NamedQuery(name = "EntityEntityCategoryMaps.findByEntityCategoryId", query = "SELECT e FROM EntityEntityCategoryMaps e WHERE e.entityEntityCategoryMapsPK.entityCategoryId = :entityCategoryId"),
		@NamedQuery(name = "EntityEntityCategoryMaps.findByEntityId", query = "SELECT e FROM EntityEntityCategoryMaps e WHERE e.entityEntityCategoryMapsPK.entityId = :entityId"),
		@NamedQuery(name = "EntityEntityCategoryMaps.findEcByEntityId", query = "SELECT ec FROM EntityEntityCategoryMaps e, EntityCategory ec WHERE e.entityEntityCategoryMapsPK.entityCategoryId = ec.entityCategoryId AND e.entityEntityCategoryMapsPK.entityId = :entityId")		
})

public class EntityEntityCategoryMaps implements Serializable {
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected EntityEntityCategoryMapsPK entityEntityCategoryMapsPK;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "entityEntityCategoryMaps")
	private Collection<UnsolvedCall> unsolvedCallCollection;
	@Transient
	private zup.bean.Entity entity;
	@Transient
	private EntityCategory entityCategory;

	public EntityEntityCategoryMaps() {
		this.unsolvedCallCollection = new ArrayList<UnsolvedCall>();
	}

	public EntityEntityCategoryMaps(zup.bean.Entity entity, EntityCategory entityCategory) {
		this.entity = entity;
		this.entityCategory = entityCategory;
		this.unsolvedCallCollection = new ArrayList<UnsolvedCall>();
	}

	public void setEntityEntityCategoryMapsAndPK(zup.bean.Entity entity, EntityCategory entityCategory) {
		this.entity = entity;
		this.entityCategory = entityCategory;
		this.entityEntityCategoryMapsPK = new EntityEntityCategoryMapsPK(entityCategory.getEntityCategoryId(),
				entity.getEntityId());
	}

	public EntityEntityCategoryMaps(EntityEntityCategoryMapsPK entityEntityCategoryMapsPK) {
		this.entityEntityCategoryMapsPK = entityEntityCategoryMapsPK;
	}

	public EntityEntityCategoryMaps(int entityCategoryId, int entityId) {
		this.entityEntityCategoryMapsPK = new EntityEntityCategoryMapsPK(entityCategoryId, entityId);
	}

	public EntityEntityCategoryMapsPK getEntityEntityCategoryMapsPK() {
		return entityEntityCategoryMapsPK;
	}

	public void setEntityEntityCategoryMapsPK(EntityEntityCategoryMapsPK entityEntityCategoryMapsPK) {
		this.entityEntityCategoryMapsPK = entityEntityCategoryMapsPK;
	}

	public Collection<UnsolvedCall> getUnsolvedCallCollection() {
		return unsolvedCallCollection;
	}

	public void setUnsolvedCallCollection(Collection<UnsolvedCall> unsolvedCallCollection) {
		this.unsolvedCallCollection = unsolvedCallCollection;
	}

	public void addUnsolvedCallCollection(UnsolvedCall unsolvedCall) {
		this.unsolvedCallCollection.add(unsolvedCall);
	}

	public zup.bean.Entity getEntity() {
		return entity;
	}

	public void setEntity(zup.bean.Entity entity) {
		this.entity = entity;
	}

	public EntityCategory getEntityCategory() {
		return entityCategory;
	}

	public void setEntityCategory(EntityCategory entityCategory) {
		this.entityCategory = entityCategory;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (entityEntityCategoryMapsPK != null ? entityEntityCategoryMapsPK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof EntityEntityCategoryMaps)) {
			return false;
		}
		EntityEntityCategoryMaps other = (EntityEntityCategoryMaps) object;
		if ((this.entityEntityCategoryMapsPK == null && other.entityEntityCategoryMapsPK != null)
				|| (this.entityEntityCategoryMapsPK != null
						&& !this.entityEntityCategoryMapsPK.equals(other.entityEntityCategoryMapsPK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ternary.EntityEntityCategoryMaps[ entityEntityCategoryMapsPK=" + entityEntityCategoryMapsPK + " ]";
	}

}

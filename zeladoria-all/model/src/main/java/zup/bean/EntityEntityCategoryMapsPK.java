package zup.bean;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class EntityEntityCategoryMapsPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "entity_category_id")
    private int entityCategoryId;
    @Basic(optional = false)
    @Column(name = "entity_id")
    private int entityId;

    public EntityEntityCategoryMapsPK() {
    }

    public EntityEntityCategoryMapsPK(int entityCategoryId, int entityId) {
        this.entityCategoryId = entityCategoryId;
        this.entityId = entityId;
    }

    public void setEntityEntityCategory(int entityCategoryId,int entityId) {
        this.entityCategoryId = entityCategoryId;
        this.entityId = entityId;
    }
    public int getEntityCategoryId() {
        return entityCategoryId;
    }

    
    public void setEntityCategoryId(int entityCategoryId) {
        this.entityCategoryId = entityCategoryId;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) entityCategoryId;
        hash += (int) entityId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EntityEntityCategoryMapsPK)) {
            return false;
        }
        EntityEntityCategoryMapsPK other = (EntityEntityCategoryMapsPK) object;
        if (this.entityCategoryId != other.entityCategoryId) {
            return false;
        }
        if (this.entityId != other.entityId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ternary.EntityEntityCategoryMapsPK[ entityCategoryId=" + entityCategoryId + ", entityId=" + entityId + " ]";
    }
    
}

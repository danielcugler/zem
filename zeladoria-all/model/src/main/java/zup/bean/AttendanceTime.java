package zup.bean;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import zup.enums.Enabled;

@Entity
@Table(name = "attendance_time")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "AttendanceTime.findAll", query = "SELECT a FROM AttendanceTime a"),
		@NamedQuery(name = "AttendanceTime.findByEntityId", query = "SELECT a FROM AttendanceTime a WHERE a.entityId = :entityId"),
		@NamedQuery(name = "AttendanceTime.findByHighPriorityTime", query = "SELECT a FROM AttendanceTime a WHERE a.highPriorityTime = :highPriorityTime"),
		@NamedQuery(name = "AttendanceTime.findByMediumPriorityTime", query = "SELECT a FROM AttendanceTime a WHERE a.mediumPriorityTime = :mediumPriorityTime"),
		@NamedQuery(name = "AttendanceTime.findByLowPriorityTime", query = "SELECT a FROM AttendanceTime a WHERE a.lowPriorityTime = :lowPriorityTime"),
		@NamedQuery(name = "AttendanceTime.findByEntityCategoryId", query = "SELECT a FROM AttendanceTime a WHERE a.entityId IN (SELECT entityId FROM Entity WHERE entityCategoryId =:entityCategoryId)"),
		@NamedQuery(name = "AttendanceTime.findByEnabled", query = "SELECT a FROM AttendanceTime a WHERE a.enabled = :enabled") })

public class AttendanceTime implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "entity_id")
	private Integer entityId;
	@Basic(optional = false)
	@Column(name = "high_priority_time")
	private int highPriorityTime;
	@Basic(optional = false)
	@Column(name = "medium_priority_time")
	private int mediumPriorityTime;
	@Basic(optional = false)
	@Column(name = "low_priority_time")
	private int lowPriorityTime;
	@Basic(optional = false)
	@NotNull
	@Column(name = "enabled")
	@Enumerated(EnumType.ORDINAL)
	private Enabled enabled;
//	@MapsId
//	@JoinColumn(name = "entity_id", referencedColumnName = "entity_id", insertable = false, updatable = false)
	//@JoinColumn(name = "entity_id", insertable = false, updatable = false)
//	@OneToOne( optional = false)
//	@JsonIgnore
//	private zup.bean.Entity entity;

	public AttendanceTime() {
	}

	public AttendanceTime(Integer entityId, int highPriorityTime, int mediumPriorityTime, int lowPriorityTime,
			Enabled enabled) {
		this.entityId = entityId;
		this.highPriorityTime = highPriorityTime;
		this.mediumPriorityTime = mediumPriorityTime;
		this.lowPriorityTime = lowPriorityTime;
		this.enabled = enabled;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public int getHighPriorityTime() {
		return highPriorityTime;
	}

	public void setHighPriorityTime(int highPriorityTime) {
		this.highPriorityTime = highPriorityTime;
	}

	public int getMediumPriorityTime() {
		return mediumPriorityTime;
	}

	public void setMediumPriorityTime(int mediumPriorityTime) {
		this.mediumPriorityTime = mediumPriorityTime;
	}

	public int getLowPriorityTime() {
		return lowPriorityTime;
	}

	public void setLowPriorityTime(int lowPriorityTime) {
		this.lowPriorityTime = lowPriorityTime;
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

/*	public zup.bean.Entity getEntity() {
		return entity;
	}

	public void setEntity(zup.bean.Entity entity) {
		this.entity = entity;
	}*/

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (entityId != null ? entityId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof AttendanceTime)) {
			return false;
		}
		AttendanceTime other = (AttendanceTime) object;
		if ((this.entityId == null && other.entityId != null)
				|| (this.entityId != null && !this.entityId.equals(other.entityId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "javaapplication1.AttendanceTime[ entityId=" + entityId + " ]";
	}

}

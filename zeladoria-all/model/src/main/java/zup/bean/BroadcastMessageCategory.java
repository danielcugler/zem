package zup.bean;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
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
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

import zup.enums.BroadcastMessageCategoryName;

@Entity
@Table(name = "broadcast_message_category")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "BroadcastMessageCategory.findAll", query = "SELECT b FROM BroadcastMessageCategory b"),
		@NamedQuery(name = "BroadcastMessageCategory.findByBroadcastMessageCategoryId", query = "SELECT b FROM BroadcastMessageCategory b WHERE b.broadcastMessageCategoryId = :broadcastMessageCategoryId"),
		@NamedQuery(name = "BroadcastMessageCategory.findByName", query = "SELECT b FROM BroadcastMessageCategory b WHERE b.name = :name") })

public class BroadcastMessageCategory implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "broadcast_message_category_id")
	private Integer broadcastMessageCategoryId;
	@Basic(optional = false)
	@NotNull
	@Column(name = "name")
	@Enumerated(EnumType.ORDINAL)
	private BroadcastMessageCategoryName name;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "broadcastMessageCategoryId")
	@JsonIgnore
	private Collection<BroadcastMessage> broadcastMessageCollection;

	public BroadcastMessageCategory() {
	}

	public BroadcastMessageCategory(Integer broadcastMessageCategoryId) {
		this.broadcastMessageCategoryId = broadcastMessageCategoryId;
	}

	public BroadcastMessageCategory(Integer broadcastMessageCategoryId, BroadcastMessageCategoryName name) {
		this.broadcastMessageCategoryId = broadcastMessageCategoryId;
		this.name = name;
	}

	public BroadcastMessageCategory(BroadcastMessageCategoryName name) {
		this.name = name;
	}

	public Integer getBroadcastMessageCategoryId() {
		return broadcastMessageCategoryId;
	}

	public void setBroadcastMessageCategoryId(Integer broadcastMessageCategoryId) {
		this.broadcastMessageCategoryId = broadcastMessageCategoryId;
	}

	public BroadcastMessageCategoryName getName() {
		return name;
	}

	public void setName(BroadcastMessageCategoryName name) {
		this.name = name;
	}

	public Collection<BroadcastMessage> getBroadcastMessageCollection() {
		return broadcastMessageCollection;
	}

	public void setBroadcastMessageCollection(Collection<BroadcastMessage> broadcastMessageCollection) {
		this.broadcastMessageCollection = broadcastMessageCollection;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (broadcastMessageCategoryId != null ? broadcastMessageCategoryId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof BroadcastMessageCategory)) {
			return false;
		}
		BroadcastMessageCategory other = (BroadcastMessageCategory) object;
		if ((this.broadcastMessageCategoryId == null && other.broadcastMessageCategoryId != null)
				|| (this.broadcastMessageCategoryId != null
						&& !this.broadcastMessageCategoryId.equals(other.broadcastMessageCategoryId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "banco.BroadcastMessageCategory[ broadcastMessageCategoryId=" + broadcastMessageCategoryId + " ]";
	}

}

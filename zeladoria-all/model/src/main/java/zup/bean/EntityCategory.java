package zup.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import zup.enums.Enabled;
import zup.enums.SendMessage;
import zup.hateoas.Link;

@Entity
@Table(name = "entity_category")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "EntityCategory.findAll", query = "SELECT s FROM EntityCategory s"),
		@NamedQuery(name = "EntityCategory.findByEntityCategoryId", query = "SELECT s FROM EntityCategory s WHERE s.entityCategoryId = :entityCategoryId"),
		@NamedQuery(name = "EntityCategory.findByName", query = "SELECT s FROM EntityCategory s WHERE s.name = :name"),
		@NamedQuery(name = "EntityCategory.findByEnabled", query = "SELECT s FROM EntityCategory s WHERE s.enabled = :enabled"),
		@NamedQuery(name = "EntityCategory.findByEnabledAndName", query = "SELECT s FROM EntityCategory s WHERE s.enabled = :enabled AND s.name = :name")})

public class EntityCategory implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "entity_category_id")
	private Integer entityCategoryId;

	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "name")
	private String name;
	@NotNull
	@Column(name = "enabled")
	@Enumerated(EnumType.ORDINAL)
	private Enabled enabled;
	@NotNull
	@Column(name = "send_message")
	@Enumerated(EnumType.ORDINAL)
	private SendMessage send_message;
	@Transient
	@JsonProperty("_links")
	List<Link> links = new ArrayList<Link>();
	
	public EntityCategory() {
	}

	public EntityCategory(String name, Enabled enabled, SendMessage send_message) {
		this.name = name;
		this.enabled = enabled;
		this.send_message = send_message;
	}



	public Integer getEntityCategoryId() {
		return entityCategoryId;
	}

	public void setEntityCategoryId(Integer entityCategoryId) {
		this.entityCategoryId = entityCategoryId;
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

	public SendMessage getSend_message() {
		return send_message;
	}

	public void setSend_message(SendMessage send_message) {
		this.send_message = send_message;
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
		hash += (entityCategoryId != null ? entityCategoryId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof EntityCategory)) {
			return false;
		}
		EntityCategory other = (EntityCategory) object;
		if ((this.entityCategoryId == null && other.entityCategoryId != null)
				|| (this.entityCategoryId != null && !this.entityCategoryId.equals(other.entityCategoryId))) {
			return false;
		}
		return true;
	}
	
	public String makeLog(){
		return "ID Categoria de Entidade: "+ entityCategoryId+"\nNome: "+name+"\nEnvio de mensagem: "+(send_message.equals(SendMessage.ENABLED)?"ativo":"inativo")+"\nHabilitado: "+(enabled.equals(Enabled.ENABLED)?"ativo":"inativo");
	}

}

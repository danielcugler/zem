package zup.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import zup.enums.Enabled;
import zup.hateoas.Link;
import zup.messages.IMessages;
import zup.messages.Messages;

@Entity
@Table(name = "message_model")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "MessageModel.findAll", query = "SELECT m FROM MessageModel m"),
		@NamedQuery(name = "MessageModel.findByMessageModelId", query = "SELECT m FROM MessageModel m WHERE m.messageModelId = :messageModelId"),
		@NamedQuery(name = "MessageModel.findByName", query = "SELECT m FROM MessageModel m WHERE m.name = :name"),
		@NamedQuery(name = "MessageModel.findByNameAndSubject", query = "SELECT m FROM MessageModel m WHERE m.name = :name AND m.subject= :subject ORDER BY m.name"),
		@NamedQuery(name = "MessageModel.findBySubject", query = "SELECT m FROM MessageModel m WHERE m.subject = :subject"),
		@NamedQuery(name = "MessageModel.findByMessageBody", query = "SELECT m FROM MessageModel m WHERE m.messageBody = :messageBody"),
		@NamedQuery(name = "MessageModel.findByEnabled", query = "SELECT m FROM MessageModel m WHERE m.enabled = :enabled ORDER BY m.name"),
		@NamedQuery(name = "MessageModel.findByEnabledAndName", query = "SELECT m FROM MessageModel m WHERE m.enabled = :enabled AND m.name = :name ORDER BY m.name"),
		@NamedQuery(name = "MessageModel.findByEnabledAndNameAndSubject", query = "SELECT m FROM MessageModel m WHERE m.enabled = :enabled AND m.name = :name AND m.subject = :subject ORDER BY m.name"),
		@NamedQuery(name = "MessageModel.findByEnabledAndSubject", query = "SELECT m FROM MessageModel m WHERE m.enabled = :enabled AND m.subject = :subject ORDER BY m.name") })

public class MessageModel implements Serializable, Comparable<MessageModel> {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "message_model_id")
	private Integer messageModelId;
	@NotEmpty(message = ValidationMessages.MESSAGE_MODEL_NAME_NOT_EMPTY)
	@Size(max = 100, message = "Campo nome não pode ter mais que 100 caracteres.")
	@Column(name = "name")
	private String name;
	@NotEmpty(message = "Campo asunto não pode ser vazio!")
	@Size(max = 100, message = "Campo asunto não pode ter mais que 100 caracteres.")
	@Column(name = "subject")
	private String subject;
	@NotEmpty(message = "Campo corpo da menssagem não pode ser vazio!")
	@Size(max = 4000, message = "A menssagem não pode ter mais que 4000 caracteres")
	@Column(name = "message_body")
	private String messageBody;

	@Column(name = "enabled")
	@Enumerated(EnumType.ORDINAL)
	private Enabled enabled;
	@Transient
	@JsonProperty("_links")
	List<Link> links = new ArrayList<Link>();

	public MessageModel() {
	}

	public MessageModel(Integer messageModelId) {
		this.messageModelId = messageModelId;
	}

	public MessageModel(Integer messageModelId, String name, String subject, String messageBody) {
		this.messageModelId = messageModelId;
		this.name = name;
		this.subject = subject;
		this.messageBody = messageBody;
		this.enabled = Enabled.ENABLED;
	}

	public Integer getMessageModelId() {
		return messageModelId;
	}

	public void setMessageModelId(Integer messageModelId) {
		this.messageModelId = messageModelId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public Enabled getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = Enabled.fromValue(Integer.parseInt(enabled));
	}

	public void changeEnabled() {
		if (this.enabled == Enabled.ENABLED)
			this.enabled = Enabled.DISABLED;
		else if (this.enabled == Enabled.DISABLED)
			this.enabled = Enabled.ENABLED;
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

	public void setEnabled(Enabled enabled) {
		this.enabled = enabled;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (messageModelId != null ? messageModelId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof MessageModel)) {
			return false;
		}
		MessageModel other = (MessageModel) object;
		if ((this.messageModelId == null && other.messageModelId != null)
				|| (this.messageModelId != null && !this.messageModelId.equals(other.messageModelId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "banco.MessageModel[ messageModelId=" + messageModelId + " ]";
	}

	public int compareTo(MessageModel other) {
		return name.compareTo(other.name);

	}
	
	public String makeLog(){
		return "ID Modelo de Mensagem: "+this.getMessageModelId()+ "\nNome: "+ this.getName()+"\nAsunto: "+ this.getSubject()+"\nCorpo da mensagem: "+this.getMessageBody()+"\nHabilitado: "+(enabled.equals(Enabled.ENABLED)?"ativo":"inativo");
	}

}

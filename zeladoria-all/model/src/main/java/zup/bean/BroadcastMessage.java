package zup.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import zup.enums.Enabled;
import zup.hateoas.Link;

@Entity
@Table(name = "broadcast_message")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "BroadcastMessage.findAll", query = "SELECT b FROM BroadcastMessage b"),
		@NamedQuery(name = "BroadcastMessage.findByBroadcastMessageId", query = "SELECT b FROM BroadcastMessage b WHERE b.broadcastMessageId = :broadcastMessageId"),
		@NamedQuery(name = "BroadcastMessage.findBySubject", query = "SELECT b FROM BroadcastMessage b WHERE b.subject = :subject"),
		@NamedQuery(name = "BroadcastMessage.findByMessageBody", query = "SELECT b FROM BroadcastMessage b WHERE b.messageBody = :messageBody"),
		@NamedQuery(name = "BroadcastMessage.findByRegistrationDate", query = "SELECT b FROM BroadcastMessage b WHERE b.registrationDate = :registrationDate"),
		@NamedQuery(name = "BroadcastMessage.findByPublicationDate", query = "SELECT b FROM BroadcastMessage b WHERE b.publicationDate = :publicationDate"),
		@NamedQuery(name = "BroadcastMessage.findByEnabled", query = "SELECT b FROM BroadcastMessage b WHERE b.enabled = :enabled"),
		@NamedQuery(name = "BroadcastMessage.findByCreationDate", query = "SELECT b FROM BroadcastMessage b WHERE b.creationDate = :creationDate") })

public class BroadcastMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "broadcast_message_id")
	private Integer broadcastMessageId;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "subject")
	private String subject;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 4000)
	@Column(name = "message_body")
	private String messageBody;
	@Basic(optional = false)
	@NotNull
	@Column(name = "registration_date")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "GMT-3")
	private Date registrationDate;
	@Column(name = "publication_date")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "GMT-3")
	private Date publicationDate;
	@Basic(optional = false)
	@Column(name = "expiration_date")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "GMT-3")
	private Date expirationDate;
	@Column(name = "days_expiration")
	private Integer daysExpiration;
	//@Transient
	private transient  Integer expirationDateVal;
	@Basic(optional = false)
	@NotNull
	@Column(name = "enabled")
	@Enumerated(EnumType.ORDINAL)
	private Enabled enabled;
	// private boolean enabled;
	@Basic(optional = false)
	@NotNull
	@Column(name = "creation_date")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "GMT-3")
	private Date creationDate;
	@JoinColumn(name = "broadcast_message_category_id", referencedColumnName = "broadcast_message_category_id")
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private BroadcastMessageCategory broadcastMessageCategoryId;
	@JoinColumn(name = "created_by", referencedColumnName = "system_user_username")
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private SystemUser createdBy;
	@Transient
	@JsonProperty("_links")
	List<Link> links = new ArrayList<Link>();

	public BroadcastMessage() {
	}

	public BroadcastMessage(Integer broadcastMessageId) {
		this.broadcastMessageId = broadcastMessageId;
	}

	public BroadcastMessage(Integer broadcastMessageId, String subject, String messageBody, Date registrationDate,
			Date publicationDate, boolean enabled, Date creationDate,
			BroadcastMessageCategory broadcastMessageCategoryId, SystemUser createdBy) {
		super();
		this.broadcastMessageId = broadcastMessageId;
		this.subject = subject;
		this.messageBody = messageBody;
		this.registrationDate = registrationDate;
		this.publicationDate = publicationDate;
		this.enabled = Enabled.ENABLED;
		this.creationDate = creationDate;
		this.broadcastMessageCategoryId = broadcastMessageCategoryId;
		this.createdBy = createdBy;
	}

	public BroadcastMessage(String subject, String messageBody, Date registrationDate, boolean enabled,
			Date creationDate) {
		this.subject = subject;
		this.messageBody = messageBody;
		this.registrationDate = registrationDate;
		this.enabled = Enabled.ENABLED;
		this.creationDate = creationDate;
	}

	public BroadcastMessage(String subject, String messageBody, Enabled enabled) {
		this.subject = subject;
		this.messageBody = messageBody;
		this.enabled = Enabled.ENABLED;
	}

	public BroadcastMessage(Integer broadcastMessageId, String subject, String messageBody, Enabled enabled) {
		this.broadcastMessageId = broadcastMessageId;
		this.subject = subject;
		this.messageBody = messageBody;
		this.enabled = Enabled.ENABLED;
	}

	public BroadcastMessage(Integer broadcastMessageId, String subject, String messageBody, Date registrationDate,
			Enabled enabled, Date creationDate) {
		this.broadcastMessageId = broadcastMessageId;
		this.subject = subject;
		this.messageBody = messageBody;
		this.registrationDate = registrationDate;
		this.enabled = Enabled.ENABLED;
		this.creationDate = creationDate;
	}		

	@XmlTransient
	public Integer getExpirationDateVal() {
		return expirationDateVal;
	}

	public void setExpirationDateVal(Integer expirationDateVal) {
		this.expirationDateVal = expirationDateVal;
	}

	public Integer getBroadcastMessageId() {
		return broadcastMessageId;
	}

	public void setBroadcastMessageId(Integer broadcastMessageId) {
		this.broadcastMessageId = broadcastMessageId;
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

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
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

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public BroadcastMessageCategory getBroadcastMessageCategoryId() {
		return broadcastMessageCategoryId;
	}

	public void setBroadcastMessageCategoryId(BroadcastMessageCategory broadcastMessageCategoryId) {
		this.broadcastMessageCategoryId = broadcastMessageCategoryId;
	}

	public SystemUser getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(SystemUser createdBy) {
		this.createdBy = createdBy;
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

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	public Integer getDaysExpiration() {
		return daysExpiration;
	}

	public void setDaysExpiration(Integer daysExpiration) {
		this.daysExpiration = daysExpiration;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (broadcastMessageId != null ? broadcastMessageId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof BroadcastMessage)) {
			return false;
		}
		BroadcastMessage other = (BroadcastMessage) object;
		if ((this.broadcastMessageId == null && other.broadcastMessageId != null)
				|| (this.broadcastMessageId != null && !this.broadcastMessageId.equals(other.broadcastMessageId))) {
			return false;
		}
		return true;
	}

	public String makeLog() {
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String log = "ID Comunicado em massa: " + this.broadcastMessageId + "\nAssunto: " + this.subject + "\nCorpo da mensagem: "
				+ this.messageBody;
		if (this.creationDate != null)
			log += "\nData de criação: " + f.format(this.creationDate);
		if (this.registrationDate != null)
			log += "\nData de registro: " + f.format(this.registrationDate);
		if (this.publicationDate != null)
			log += "\nData de publicação: " + f.format(this.publicationDate);
		log += "\nCategoria de comunicado em massa: " + this.broadcastMessageCategoryId.getName().getStr() + "\nHabilitado: "
				+ (this.enabled.equals(Enabled.ENABLED) ? "ativo" : "inativo");
		return log;
	}
}

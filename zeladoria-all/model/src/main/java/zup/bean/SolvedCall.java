package zup.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import zup.enums.Anonymity;
import zup.enums.CallProgress;
import zup.enums.CallSource;
import zup.enums.CallStatus;
import zup.enums.Priority;
import zup.enums.Secret;
import zup.serializer.SolvedCallSerializer;

@Entity
@Table(name = "solved_call")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "SolvedCall.findAll", query = "SELECT s FROM SolvedCall s"),
		@NamedQuery(name = "SolvedCall.findBySolvedCallId", query = "SELECT s FROM SolvedCall s WHERE s.solvedCallId = :solvedCallId"),
		@NamedQuery(name = "SolvedCall.findByCallStatus", query = "SELECT s FROM SolvedCall s WHERE s.callStatus = :callStatus"),
		@NamedQuery(name = "SolvedCall.findByCallProgress", query = "SELECT s FROM SolvedCall s WHERE s.callProgress = :callProgress"),
		@NamedQuery(name = "SolvedCall.findByCallSource", query = "SELECT s FROM SolvedCall s WHERE s.callSource = :callSource"),
		@NamedQuery(name = "SolvedCall.findByCreationOrUpdateDate", query = "SELECT s FROM SolvedCall s WHERE s.creationOrUpdateDate = :creationOrUpdateDate"),
		@NamedQuery(name = "SolvedCall.findByDescription", query = "SELECT s FROM SolvedCall s WHERE s.description = :description"),
		@NamedQuery(name = "SolvedCall.findByObservation", query = "SELECT s FROM SolvedCall s WHERE s.observation = :observation"),
		@NamedQuery(name = "SolvedCall.findBySecret", query = "SELECT s FROM SolvedCall s WHERE s.secret = :secret"),
		@NamedQuery(name = "SolvedCall.findByAnonymity", query = "SELECT s FROM SolvedCall s WHERE s.anonymity = :anonymity"),	
		@NamedQuery(name = "SolvedCall.findByPriority", query = "SELECT s FROM SolvedCall s WHERE s.priority = :priority"),
		@NamedQuery(name = "SolvedCall.findAllMon", query = "SELECT u.solvedCallId FROM SolvedCall u WHERE u.solvedCallId IN (SELECT MAX(u2.solvedCallId) FROM SolvedCall u2 GROUP BY COALESCE(parent_call_id,solved_call_id))"),
		@NamedQuery(name = "SolvedCall.findAllLastChild", query = "SELECT s.solvedCallId FROM SolvedCall s WHERE s.solvedCallId IN (SELECT MAX(s2.solvedCallId) FROM SolvedCall s2 GROUP BY COALESCE(parent_call_id, solved_call_id))"),
		@NamedQuery(name = "SolvedCall.findAllLastChildId", query = "SELECT s.solvedCallId FROM SolvedCall s WHERE s.solvedCallId IN (SELECT MAX(s2.solvedCallId) FROM SolvedCall s2 WHERE s2.parentCallId = :parentCallId GROUP BY COALESCE(parent_call_id, solved_call_id))"),
		@NamedQuery(name = "SolvedCall.countCallFinalized", query = "SELECT COUNT(*) as total FROM SolvedCall s WHERE s.callProgress = 3"),
		@NamedQuery(name = "SolvedCall.countQualification", query = "SELECT COUNT(s.qualification) as count_qualification, s.qualification FROM SolvedCall s WHERE s.solvedCallId IN (SELECT MAX(s2.solvedCallId) FROM SolvedCall s2 GROUP BY COALESCE(parent_call_id, solved_call_id)) AND s.qualification > 0 AND s.qualification < 6 GROUP BY s.qualification ORDER BY s.qualification ASC")
})

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@JsonSerialize(using = SolvedCallSerializer.class)
public class SolvedCall implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "solved_call_id")
	private Long solvedCallId;
	@Basic(optional = false)
	@NotNull
	@Column(name = "call_status")
	@Enumerated(EnumType.ORDINAL)
	private CallStatus callStatus;
	@Basic(optional = false)
	@NotNull
	@Column(name = "call_progress")
	@Enumerated(EnumType.ORDINAL)
	private CallProgress callProgress;
	@Basic(optional = false)
	@NotNull
	@Column(name = "call_source")
	@Enumerated(EnumType.ORDINAL)
	private CallSource callSource;
	@Basic(optional = false)
	@NotNull
	@Column(name = "creation_or_update_date")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "pt-BR", timezone = "Brazil/East")
	private Date creationOrUpdateDate;
	@Basic(optional = false)
	@NotNull
	@Column(name = "no_midia")
	private boolean noMidia;
	@Basic(optional = false)
	@NotNull
	@Column(name = "secret")
	@Enumerated(EnumType.ORDINAL)
	private Secret secret;
	@Basic(optional = false)
	@NotNull
	@Column(name = "anonymity")
	@Enumerated(EnumType.ORDINAL)
	private Anonymity anonymity;
	@Basic(optional = false)
	@NotNull
	@Column(name = "priority")
	@Enumerated(EnumType.ORDINAL)
	private Priority priority;
	@Transient
	private Collection<byte[]> medias;
	@Transient
	private Collection<String> images;
	@Transient
	@JsonDeserialize(as = ArrayList.class, contentAs = String.class)
	private ArrayList<String> mediasPath;
	@NotNull
	@Column(name = "remove")
	private Boolean remove;

	@Transient
	private String delay;
	@Transient
	private String callReply;
	@Transient
	private String firstPhoto;
	@Transient
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "GMT-3")
	private Date expirationDate;
	@Column(name = "qualification")
	private Short qualification;
	@JoinColumn(name = "observation", referencedColumnName = "additional_information_id")
	@ManyToOne // (cascade = CascadeType.ALL)
	private AdditionalInformation observation;
	@JoinColumn(name = "description", referencedColumnName = "additional_information_id")
	@ManyToOne(optional = false)
	private AdditionalInformation description;
	@JoinColumn(name = "address_id")
	@ManyToOne
	private Address addressId;
	@JoinColumn(name = "call_classification_id", referencedColumnName = "call_classification_id")
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private CallClassification callClassificationId;
	@JoinColumn(name = "citizen_cpf", referencedColumnName = "citizen_cpf")
	@ManyToOne(fetch = FetchType.LAZY)
	private Citizen citizenCpf;
	@JoinColumn(name = "updated_or_moderated_by", referencedColumnName = "system_user_username")
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	private SystemUser updatedOrModeratedBy;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "parentCallId")
	private Collection<SolvedCall> solvedCallCollection;
	@JoinColumn(name = "parent_call_id", referencedColumnName = "solved_call_id")
	@ManyToOne(fetch = FetchType.EAGER)
	private SolvedCall parentCallId;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "solvedCallId")
	private Collection<Media> mediaCollection;
	@OneToOne(mappedBy = "solvedCallId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private WebUser webUserSN;
	@JoinColumns({ @JoinColumn(name = "entity_id", referencedColumnName = "entity_id"),
			@JoinColumn(name = "entity_category_id", referencedColumnName = "entity_category_id") })
	@ManyToOne
	private EntityEntityCategoryMaps entityEntityCategoryMaps;

	/*
	 * @Id
	 * 
	 * @Basic(optional = false)
	 * 
	 * @Column(name = "solved_call_id") private Long solvedCallId;
	 * 
	 * @Basic(optional = false)
	 * 
	 * @NotNull
	 * 
	 * @Column(name = "call_status")
	 * 
	 * @Enumerated(EnumType.ORDINAL) private CallStatus callStatus;
	 * 
	 * @Basic(optional = false)
	 * 
	 * @NotNull
	 * 
	 * @Column(name = "call_progress")
	 * 
	 * @Enumerated(EnumType.ORDINAL) private CallProgress callProgress;
	 * 
	 * @Basic(optional = false)
	 * 
	 * @NotNull
	 * 
	 * @Column(name = "call_source")
	 * 
	 * @Enumerated(EnumType.ORDINAL) private CallSource callSource;
	 * 
	 * @Basic(optional = false)
	 * 
	 * @NotNull
	 * 
	 * @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm",
	 * timezone="GMT-3")
	 * 
	 * @Column(name = "creation_or_update_date")
	 * 
	 * @Temporal(TemporalType.TIMESTAMP) private Date creationOrUpdateDate;
	 * 
	 * @Basic(optional = false)
	 * 
	 * @NotNull
	 * 
	 * @Column(name = "no_midia") private boolean noMidia;
	 * 
	 * @Basic(optional = false)
	 * 
	 * @NotNull
	 * 
	 * @Column(name = "secret")
	 * 
	 * @Enumerated(EnumType.ORDINAL) private Secret secret;
	 * 
	 * @Basic(optional = false)
	 * 
	 * @NotNull
	 * 
	 * @Column(name = "anonymity")
	 * 
	 * @Enumerated(EnumType.ORDINAL) private Anonymity anonymity;
	 * 
	 * @Basic(optional = false)
	 * 
	 * @NotNull
	 * 
	 * @Column(name = "priority")
	 * 
	 * @Enumerated(EnumType.ORDINAL) private Priority priority;
	 * 
	 * @Transient private Collection<byte[]> medias;
	 * 
	 * @Column(name = "qualification") private Short qualification;
	 * 
	 * @JoinColumn(name = "description", referencedColumnName =
	 * "additional_information_id")
	 * 
	 * @ManyToOne(optional = false) private AdditionalInformation description;
	 * 
	 * @JoinColumn(name = "observation", referencedColumnName =
	 * "additional_information_id")
	 * 
	 * @ManyToOne//(cascade = CascadeType.ALL) private AdditionalInformation
	 * observation;
	 * 
	 * @JoinColumn(name = "address_id")
	 * 
	 * @ManyToOne(optional = false) private Address addressId;
	 * 
	 * @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy =
	 * "solvedCallId") private Collection<Media> mediaCollection;
	 * 
	 * @JoinColumn(name = "call_classification_id", referencedColumnName =
	 * "call_classification_id")
	 * 
	 * @ManyToOne(fetch = FetchType.EAGER, optional = false) private
	 * CallClassification callClassificationId;
	 * 
	 * @JoinColumn(name = "citizen_cpf", referencedColumnName = "citizen_cpf")
	 * 
	 * @ManyToOne(fetch = FetchType.EAGER) private Citizen citizenCpf;
	 * 
	 * @JoinColumns({ @JoinColumn(name = "entity_id", referencedColumnName =
	 * "entity_id"),
	 * 
	 * @JoinColumn(name = "entity_category_id", referencedColumnName =
	 * "entity_category_id") })
	 * 
	 * @ManyToOne private EntityEntityCategoryMaps entityEntityCategoryMaps;
	 * 
	 * @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy =
	 * "parentCallId") private Collection<SolvedCall> solvedCallCollection;
	 * 
	 * @JoinColumn(name = "parent_call_id", referencedColumnName =
	 * "solved_call_id")
	 * 
	 * @ManyToOne(fetch = FetchType.EAGER, optional = false) private SolvedCall
	 * parentCallId;
	 * 
	 * @JoinColumn(name = "updated_or_moderated_by", referencedColumnName =
	 * "system_user_username")
	 * 
	 * @ManyToOne(fetch = FetchType.EAGER, optional = false) private SystemUser
	 * updatedOrModeratedBy;
	 * 
	 * @OneToOne(mappedBy = "solvedCallId", fetch = FetchType.EAGER) private
	 * WebUser webUserS;
	 */

	public SolvedCall() {
	}

	public SolvedCall(Long solvedCallId) {
		this.solvedCallId = solvedCallId;
	}

	/*public SolvedCall(Long solvedCallId, String priority) {
		super();
		this.solvedCallId = solvedCallId;
	}*/
	
	public SolvedCall(Long solvedCallId, int callStatus, int callProgress, int callSource,
			Date creationOrUpdateDate, Secret secret, Anonymity anonymity, int priority) {
		this.solvedCallId = solvedCallId;
		this.callStatus = CallStatus.fromValue(callStatus);
		this.callProgress = CallProgress.fromValue(callProgress);
		this.callSource = CallSource.fromValue(callSource);
		this.creationOrUpdateDate = creationOrUpdateDate;
		this.secret = secret;
		this.anonymity = anonymity;
		this.priority = Priority.fromValue(priority);
	}

	public SolvedCall(Long solvedCallId, int callStatus, String callProgress, String callSource,
			Date creationOrUpdateDate, Secret secret, Anonymity anonymity, String priority, Collection<byte[]> medias,
			String targetGeograpicalCoordinates, Collection<Media> mediaCollection,
			CallClassification callClassificationId, Citizen citizenCpf, EntityCategory entityCategoryTarget,
			Collection<SolvedCall> solvedCallCollection, SolvedCall parentCallId, SystemUser updatedOrModeratedBy) {
		super();
		this.solvedCallId = solvedCallId;
		this.callStatus = CallStatus.fromValue(callStatus);
		this.creationOrUpdateDate = creationOrUpdateDate;
		
		this.secret = secret;
		this.anonymity = anonymity;
		
		this.medias = medias;
		
		this.callClassificationId = callClassificationId;
		this.citizenCpf = citizenCpf;		

		this.updatedOrModeratedBy = updatedOrModeratedBy;
		this.solvedCallCollection = solvedCallCollection;
		this.parentCallId = parentCallId;
		this.mediaCollection = mediaCollection;
	}
	
	public SolvedCall(CallStatus callStatus, Secret secret, Anonymity anonymity,
			CallClassification callClassificationId) {
		super();
		this.callStatus = callStatus;

		this.secret = secret;
		this.anonymity = anonymity;

		this.callClassificationId = callClassificationId;
	}

	public Long getSolvedCallId() {
		return solvedCallId;
	}

	public void setSolvedCallId(Long solvedCallId) {
		this.solvedCallId = solvedCallId;
	}

	public WebUser getWebUser() {
		return webUserSN;
	}

	public void setWebUser(WebUser webUser) {
		this.webUserSN = webUser;
	}
	
	public ArrayList<String> getMediasPath() {
		return mediasPath;
	}
	
	public void setMediasPath(ArrayList<String> mediasPath) {
		this.mediasPath = mediasPath;
	}
	
	public String getDelay() {
		return delay;
	}

	public void setDelay(String delay) {
		this.delay = delay;
	}

	public Collection<String> getImages() {
		return images;
	}

	public void setImages(Collection<String> images) {
		this.images = images;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}
	

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Collection<byte[]> getMedias() {
		return medias;
	}

	public void setMedias(Collection<byte[]> medias) {
		this.medias = medias;
	}

	public CallStatus getCallStatus() {
		return this.callStatus;
	}

	public void setCallStatus(CallStatus callStatus) {
		this.callStatus = callStatus;
	}

	public void changeCallStatus() {
		if (this.callStatus == CallStatus.ATIVO){
			this.callProgress = CallProgress.REJEITADO;
			this.callStatus = CallStatus.REJEITADO;
		}
		else if (this.callStatus == CallStatus.REJEITADO)
			this.callProgress = CallProgress.NOVO;
			this.callStatus = CallStatus.ATIVO;
	}

	public Boolean getRemove() {
		return remove;
	}

	public void setRemove(Boolean remove) {
		this.remove = remove;
	}

	public CallProgress getCallProgress() {
		return callProgress;
	}

	public void setCallProgress(CallProgress callProgress) {
		this.callProgress = callProgress;
	}

	public CallSource getCallSource() {
		return callSource;
	}

	public void setCallSource(CallSource callSource) {
		this.callSource = callSource;
	}

	public Date getCreationOrUpdateDate() {
		return creationOrUpdateDate;
	}

	public void setCreationOrUpdateDate(Date creationOrUpdateDate) {
		this.creationOrUpdateDate = creationOrUpdateDate;
	}

	public boolean isNoMidia() {
		return noMidia;
	}

	public void setNoMidia(boolean noMidia) {
		this.noMidia = noMidia;
	}

	public String getCallReply() {
		return callReply;
	}

	public void setCallReply(String callReply) {
		this.callReply = callReply;
	}

	public Secret getSecret() {
		return secret;
	}

	public void setSecret(Secret secret) {
		this.secret = secret;
	}

	public Anonymity getAnonymity() {
		return anonymity;
	}

	public void setAnonymity(Anonymity anonymity) {
		this.anonymity = anonymity;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public AdditionalInformation getObservation() {
		return observation;
	}

	public void setObservation(AdditionalInformation observation) {
		this.observation = observation;
	}

	public AdditionalInformation getDescription() {
		return description;
	}

	public void setDescription(AdditionalInformation description) {
		this.description = description;
	}

	public Address getAddressId() {
		return addressId;
	}

	public void setAddressId(Address addressId) {
		this.addressId = addressId;
	}

	public CallClassification getCallClassificationId() {
		return callClassificationId;
	}

	public void setCallClassificationId(CallClassification callClassificationId) {
		this.callClassificationId = callClassificationId;
	}

	public Citizen getCitizenCpf() {
		return citizenCpf;
	}

	public void setCitizenCpf(Citizen citizenCpf) {
		this.citizenCpf = citizenCpf;
	}

	public SystemUser getUpdatedOrModeratedBy() {
		return updatedOrModeratedBy;
	}

	public void setUpdatedOrModeratedBy(SystemUser updatedOrModeratedBy) {
		this.updatedOrModeratedBy = updatedOrModeratedBy;
	}
	
	@XmlTransient
	public Collection<SolvedCall> getSolvedCallCollection() {
		return solvedCallCollection;
	}

	public void setSolvedCallCollection(Collection<SolvedCall> solvedCallCollection) {
		this.solvedCallCollection = solvedCallCollection;
	}

	public SolvedCall getParentCallId() {
		return parentCallId;
	}

	public void setParentCallId(SolvedCall parentCallId) {
		this.parentCallId = parentCallId;
	}
	
	@XmlTransient
	public Collection<Media> getMediaCollection() {
		return mediaCollection;
	}

	public void setMediaCollection(Collection<Media> mediaCollection) {
		this.mediaCollection = mediaCollection;
	}

	public boolean hasWebUser() {
		return this.webUserSN != null;
	}

	public EntityEntityCategoryMaps getEntityEntityCategoryMaps() {
		return entityEntityCategoryMaps;
	}

	public void setEntityEntityCategoryMaps(EntityEntityCategoryMaps entityEntityCategoryMaps) {
		this.entityEntityCategoryMaps = entityEntityCategoryMaps;
	}
	
	public void addEntityEntityCategoryMaps(EntityCategory entityCategory, zup.bean.Entity entity) {
		this.entityEntityCategoryMaps.setEntity(entity);
		this.entityEntityCategoryMaps.setEntityCategory(entityCategory);
	}
	

	public Short getQualification() {
		return qualification;
	}

	public void setQualification(Short qulification) {
		this.qualification = qulification;
	} 	
	
	@XmlElement(name="firstPhoto")
	public String getFirstPhoto() {
		return firstPhoto;
	}

	public void setFirstPhoto(String firstPhoto) {
		this.firstPhoto = firstPhoto;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (solvedCallId != null ? solvedCallId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof SolvedCall)) {
			return false;
		}
		SolvedCall other = (SolvedCall) object;
		if ((this.solvedCallId == null && other.solvedCallId != null)
				|| (this.solvedCallId != null && !this.solvedCallId.equals(other.solvedCallId))) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "banco.SolvedCall[ solvedCallId=" + solvedCallId + " ]";
	}

	/*public SolvedCall toSolvedCall() {
		SolvedCall sc = new SolvedCall();
		sc.setSolvedCallId(this.solvedCallId);

		sc.setCallStatus(this.callStatus);
		sc.setCallProgress(this.callProgress);
		sc.setCallSource(this.callSource);
		sc.setCreationOrUpdateDate(this.creationOrUpdateDate);
		sc.setDescription(this.description);
		sc.setObservation(this.observation);
		sc.setSecret(this.secret);
		sc.setAnonymity(this.anonymity);
		sc.setPriority(this.priority);
		sc.setAddressId(this.getAddressId());
		sc.setMedias(this.medias);
		sc.setEntityEntityCategoryMaps(this.getEntityEntityCategoryMaps());
		sc.setCallClassificationId(this.callClassificationId);
		sc.setCitizenCpf(this.citizenCpf);
		sc.setUpdatedOrModeratedBy(this.updatedOrModeratedBy);
		sc.setMediaCollection(this.mediaCollection);
		return sc;
	}*/
	

	/*public CallProgress getCallProgress() {
		return callProgress;
	}

	public void setCallProgress(CallProgress callProgress) {
		this.callProgress = callProgress;
	}

	public CallSource getCallSource() {
		return callSource;
	}

	public void setCallSource(CallSource callSource) {
		this.callSource = callSource;
	}

	public Date getCreationOrUpdateDate() {
		return creationOrUpdateDate;
	}

	public void setCreationOrUpdateDate(Date creationOrUpdateDate) {
		this.creationOrUpdateDate = creationOrUpdateDate;
	}

	public boolean isNoMidia() {
		return noMidia;
	}

	public void setNoMidia(boolean noMidia) {
		this.noMidia = noMidia;
	}

	public Secret getSecret() {
		return secret;
	}

	public void setSecret(Secret secret) {
		this.secret = secret;
	}

	public Anonymity getAnonymity() {
		return anonymity;
	}

	public void setAnonymity(Anonymity anonymity) {
		this.anonymity = anonymity;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Collection<byte[]> getMedias() {
		return medias;
	}

	public void setMedias(Collection<byte[]> cm) {
		this.medias = cm;
	}

	@XmlTransient
	public Collection<Media> getMediaCollection() {
		return mediaCollection;
	}

	public void setMediaCollection(Collection<Media> mediaCollection) {
		this.mediaCollection = mediaCollection;
	}

	public CallClassification getCallClassificationId() {
		return callClassificationId;
	}

	public void setCallClassificationId(CallClassification callClassificationId) {
		this.callClassificationId = callClassificationId;
	}

	public Citizen getCitizenCpf() {
		return citizenCpf;
	}

	public void setCitizenCpf(Citizen citizenCpf) {
		this.citizenCpf = citizenCpf;
	}

	public AdditionalInformation getDescription() {
		return description;
	}

	public EntityEntityCategoryMaps getEntityEntityCategoryMaps() {
		return entityEntityCategoryMaps;
	}

	public void setEntityEntityCategoryMaps(EntityEntityCategoryMaps entityEntityCategoryMaps) {
		this.entityEntityCategoryMaps = entityEntityCategoryMaps;
	}

	public void setDescription(AdditionalInformation description) {
		this.description = description;
	}

	public AdditionalInformation getObservation() {
		return observation;
	}

	public void setObservation(AdditionalInformation observation) {
		this.observation = observation;
	}

	public Address getAddressId() {
		return addressId;
	}

	public void setAddressId(Address addressId) {
		this.addressId = addressId;
	}

	@XmlTransient
	public Collection<SolvedCall> getSolvedCallCollection() {
		return solvedCallCollection;
	}

	public void setSolvedCallCollection(Collection<SolvedCall> solvedCallCollection) {
		this.solvedCallCollection = solvedCallCollection;
	}

	public SolvedCall getParentCallId() {
		return parentCallId;
	}

	public void setParentCallId(SolvedCall parentCallId) {
		this.parentCallId = parentCallId;
	}

	public SystemUser getUpdatedOrModeratedBy() {
		return updatedOrModeratedBy;
	}

	public void setUpdatedOrModeratedBy(SystemUser updatedOrModeratedBy) {
		this.updatedOrModeratedBy = updatedOrModeratedBy;
	}

	public CallStatus getCallStatus() {
		return this.callStatus;
	}

	public void setCallStatus(CallStatus callStatus) {
		this.callStatus = callStatus;
	}

	public void changeCallStatus() {
		if (this.callStatus == CallStatus.ATIVO)
			this.callStatus = CallStatus.REJEITADO;
		else if (this.callStatus == CallStatus.REJEITADO)
			this.callStatus = CallStatus.ATIVO;
	}

	public Short getQualification() {
		return qualification;
	}

	public void setQualification(Short qualification) {
		this.qualification = qualification;
	}	

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (solvedCallId != null ? solvedCallId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof SolvedCall)) {
			return false;
		}
		SolvedCall other = (SolvedCall) object;
		if ((this.solvedCallId == null && other.solvedCallId != null)
				|| (this.solvedCallId != null && !this.solvedCallId.equals(other.solvedCallId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "banco.SolvedCall[ solvedCallId=" + solvedCallId + " ]";
	} */

}

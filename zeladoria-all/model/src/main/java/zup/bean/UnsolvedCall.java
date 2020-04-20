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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import zup.enums.Anonymity;
import zup.enums.CallProgress;
import zup.enums.CallSource;
import zup.enums.CallStatus;
import zup.enums.Priority;
import zup.enums.Secret;
import zup.serializer.UnsolvedCallSerializer;




@Entity
@Table(name = "unsolved_call") 
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "UnsolvedCall.findAll", query = "SELECT u FROM UnsolvedCall u"),
		@NamedQuery(name = "UnsolvedCall.findByUnsolvedCallId", query = "SELECT u FROM UnsolvedCall u WHERE u.unsolvedCallId = :unsolvedCallId"),
		@NamedQuery(name = "UnsolvedCall.findByCallStatus", query = "SELECT u FROM UnsolvedCall u WHERE u.callStatus = :callStatus"),
		@NamedQuery(name = "UnsolvedCall.findByCallProgress", query = "SELECT u FROM UnsolvedCall u WHERE u.callProgress = :callProgress"),
		@NamedQuery(name = "UnsolvedCall.findByCallSource", query = "SELECT u FROM UnsolvedCall u WHERE u.callSource = :callSource"),
		@NamedQuery(name = "UnsolvedCall.findByCreationOrUpdateDate", query = "SELECT u FROM UnsolvedCall u WHERE u.creationOrUpdateDate = :creationOrUpdateDate"),
		@NamedQuery(name = "UnsolvedCall.findByDescription", query = "SELECT u FROM UnsolvedCall u WHERE u.description = :description"),
		@NamedQuery(name = "UnsolvedCall.findByObservation", query = "SELECT u FROM UnsolvedCall u WHERE u.observation = :observation"),
		@NamedQuery(name = "UnsolvedCall.findBySecret", query = "SELECT u FROM UnsolvedCall u WHERE u.secret = :secret"),
		@NamedQuery(name = "UnsolvedCall.findByAnonymity", query = "SELECT u FROM UnsolvedCall u WHERE u.anonymity = :anonymity"),
		@NamedQuery(name = "UnsolvedCall.findByPriority", query = "SELECT u FROM UnsolvedCall u WHERE u.priority = :priority"),
		@NamedQuery(name = "UnsolvedCall.findByParent", query = "SELECT u FROM UnsolvedCall u inner join u.parentCallId parent WHERE parent.unsolvedCallId = :unsolvedCallId ORDER BY u.unsolvedCallId"),
		@NamedQuery(name = "UnsolvedCall.findAllLastChild", query = "SELECT u.unsolvedCallId FROM UnsolvedCall u WHERE u.unsolvedCallId IN (SELECT MAX(u2.unsolvedCallId) FROM UnsolvedCall u2 GROUP BY COALESCE(parent_call_id,unsolved_call_id))"),
		@NamedQuery(name = "UnsolvedCall.findAllLastChildId", query = "SELECT u.unsolvedCallId FROM UnsolvedCall u WHERE u.unsolvedCallId IN (SELECT MAX(u2.unsolvedCallId) FROM UnsolvedCall u2 WHERE u2.parentCallId = :parentCallId GROUP BY COALESCE(parent_call_id,unsolved_call_id))"),
		@NamedQuery(name = "UnsolvedCall.findAllCall", query = "SELECT u.unsolvedCallId FROM UnsolvedCall u WHERE u.callProgress IN (0,2,3,6) AND u.unsolvedCallId IN (SELECT MAX(u2.unsolvedCallId) FROM UnsolvedCall u2 GROUP BY COALESCE(parent_call_id,unsolved_call_id))"),
		@NamedQuery(name = "UnsolvedCall.findAllCall3", query = "SELECT u.unsolvedCallId FROM UnsolvedCall u WHERE u.callProgress != 0 AND u.unsolvedCallId IN (SELECT MAX(u2.unsolvedCallId) FROM UnsolvedCall u2 GROUP BY COALESCE(parent_call_id,unsolved_call_id))"),
		@NamedQuery(name = "UnsolvedCall.findAllCallFollow", query = "SELECT u.unsolvedCallId FROM UnsolvedCall u WHERE u.callProgress != 0 AND u.callProgress != 2 AND u.callProgress != 3 AND u.callProgress != 6 AND u.unsolvedCallId IN (SELECT MAX(u2.unsolvedCallId) FROM UnsolvedCall u2 GROUP BY COALESCE(parent_call_id,unsolved_call_id))"),
		
		//Counts da Dashboard
		@NamedQuery(name = "UnsolvedCall.countInProgress", query = "SELECT count(*) AS total FROM UnsolvedCall un WHERE un.callProgress != 6 AND un.callProgress != 3 AND un.unsolvedCallId IN (SELECT MAX(u2.unsolvedCallId) FROM UnsolvedCall u2 GROUP BY COALESCE(parent_call_id,unsolved_call_id))"),
		@NamedQuery(name = "UnsolvedCall.countCallDelay", query = "SELECT upa.creationOrUpdateDate, CASE WHEN un.priority=0 THEN at.lowPriorityTime WHEN un.priority = 1 THEN at.mediumPriorityTime ELSE at.highPriorityTime END AS time FROM UnsolvedCall un,UnsolvedCall upa, AttendanceTime at WHERE un.parentCallId.unsolvedCallId = upa.unsolvedCallId AND un.entityEntityCategoryMaps.entityEntityCategoryMapsPK.entityId = at.entityId AND un.unsolvedCallId IN (SELECT MAX(u2.unsolvedCallId) FROM UnsolvedCall u2 GROUP BY COALESCE(parent_call_id,unsolved_call_id))"),
		@NamedQuery(name = "UnsolvedCall.countAllCall", query = "SELECT count(*) AS total FROM UnsolvedCall un WHERE un.unsolvedCallId IN (SELECT MAX(u2.unsolvedCallId) FROM UnsolvedCall u2 GROUP BY COALESCE(parent_call_id,unsolved_call_id))"),
		@NamedQuery(name = "UnsolvedCall.countNewCall", query = "SELECT count(*) AS total FROM UnsolvedCall un WHERE un.callProgress = 0 AND un.unsolvedCallId IN (SELECT MAX(u2.unsolvedCallId) FROM UnsolvedCall u2 GROUP BY COALESCE(parent_call_id,unsolved_call_id))"),
		@NamedQuery(name = "UnsolvedCall.countForwardedCall", query = "SELECT count(*) AS total FROM UnsolvedCall un WHERE un.callProgress = 1 AND un.unsolvedCallId IN (SELECT MAX(u2.unsolvedCallId) FROM UnsolvedCall u2 GROUP BY COALESCE(parent_call_id,unsolved_call_id))"),
		@NamedQuery(name = "UnsolvedCall.countVisualizedCall", query = "SELECT count(*) AS total FROM UnsolvedCall un WHERE un.callProgress = 4 AND un.unsolvedCallId IN (SELECT MAX(u2.unsolvedCallId) FROM UnsolvedCall u2 GROUP BY COALESCE(parent_call_id,unsolved_call_id))"),
		@NamedQuery(name = "UnsolvedCall.countInProgressCall", query = "SELECT count(*) AS total FROM UnsolvedCall un WHERE un.callProgress = 5 AND un.unsolvedCallId IN (SELECT MAX(u2.unsolvedCallId) FROM UnsolvedCall u2 GROUP BY COALESCE(parent_call_id,unsolved_call_id))"),
		@NamedQuery(name = "UnsolvedCall.countProccessedCall", query = "SELECT count(*) AS total FROM UnsolvedCall un WHERE un.callProgress = 2 AND un.unsolvedCallId IN (SELECT MAX(u2.unsolvedCallId) FROM UnsolvedCall u2 GROUP BY COALESCE(parent_call_id,unsolved_call_id))"),
		@NamedQuery(name = "UnsolvedCall.countRejectedCall", query = "SELECT count(*) AS total FROM UnsolvedCall un WHERE un.callProgress = 6 AND un.unsolvedCallId IN (SELECT MAX(u2.unsolvedCallId) FROM UnsolvedCall u2 GROUP BY COALESCE(parent_call_id,unsolved_call_id))"),

//		@NamedQuery(name = "UnsolvedCall.countCallProgress", query = "SELECT call.cp, COUNT(*) AS count FROM SELECT s.callProgress AS cp, s.solvedCallId AS callid FROM SolvedCall s WHERE s.solvedCallId IN (SELECT MAX(s2.solvedCallId) FROM solved_call s2 GROUP BY COALESCE(parent_call_id,solved_call_id)) GROUP BY cp, solvedCallId ORDER By cp ASC UNION SELECT u.callProgress AS cp, u.unsolvedCallId AS callid FROM UnsolvedCall u WHERE u.unsolvedCallId IN (SELECT MAX(u2.unsolvedCallId) FROM unsolved_call u2 GROUP BY COALESCE(parent_call_id,unsolved_call_id))  GROUP BY cp, u.unsolvedCallId ORDER By cp ASC AS call GROUP BY call.cp ORDER By call.cp ASC"),
		
		//Queries do Monitor de Chamados
		@NamedQuery(name = "UnsolvedCall.findAllMon", query = "SELECT u.unsolvedCallId FROM UnsolvedCall u WHERE u.unsolvedCallId IN (SELECT MAX(u2.unsolvedCallId) FROM UnsolvedCall u2 GROUP BY COALESCE(parent_call_id,unsolved_call_id))"),
		@NamedQuery(name = "UnsolvedCall.countCPMonitor", query = "SELECT u.callProgress, COUNT(*) as total FROM UnsolvedCall u WHERE u.unsolvedCallId IN (SELECT MAX(u2.unsolvedCallId) FROM UnsolvedCall u2 GROUP BY COALESCE(parent_call_id,unsolved_call_id)) GROUP BY u.callProgress ORDER BY total DESC"),
		@NamedQuery(name = "UnsolvedCall.countCSMonitor", query = "SELECT u.callSource, COUNT(*) as total FROM UnsolvedCall u WHERE u.unsolvedCallId IN (SELECT MAX(u2.unsolvedCallId) FROM UnsolvedCall u2 GROUP BY COALESCE(parent_call_id,unsolved_call_id)) GROUP BY u.callSource ORDER BY total DESC"),
		@NamedQuery(name = "UnsolvedCall.countPRMonitor", query = "SELECT u.priority, COUNT(*) as total FROM UnsolvedCall u WHERE u.unsolvedCallId IN (SELECT MAX(u2.unsolvedCallId) FROM UnsolvedCall u2 GROUP BY COALESCE(parent_call_id,unsolved_call_id)) GROUP BY u.priority ORDER BY total DESC"),
		@NamedQuery(name = "UnsolvedCall.countCCMonitor", query = "SELECT u.callClassificationId.name, COUNT(*) as total FROM UnsolvedCall u WHERE u.unsolvedCallId IN (SELECT MAX(u2.unsolvedCallId) FROM UnsolvedCall u2 GROUP BY COALESCE(parent_call_id,unsolved_call_id)) GROUP BY u.callClassificationId.name ORDER BY total DESC"),
		@NamedQuery(name = "UnsolvedCall.countENMonitor", query = "SELECT en.name, COUNT(*) as total FROM UnsolvedCall u, Entity en WHERE u.entityEntityCategoryMaps.entityEntityCategoryMapsPK.entityId = en.entityId AND u.unsolvedCallId IN (SELECT MAX(u2.unsolvedCallId) FROM UnsolvedCall u2 GROUP BY COALESCE(parent_call_id,unsolved_call_id)) GROUP BY en.name ORDER BY total DESC"),
		@NamedQuery(name = "UnsolvedCall.countECMonitor", query = "SELECT ec.name, COUNT(*) FROM UnsolvedCall u, EntityCategory ec WHERE u.entityEntityCategoryMaps.entityEntityCategoryMapsPK.entityCategoryId =  ec.entityCategoryId AND u.unsolvedCallId IN (SELECT MAX(u2.unsolvedCallId) FROM UnsolvedCall u2 GROUP BY COALESCE(parent_call_id,unsolved_call_id)) GROUP BY ec.name ORDER BY total DESC"),
		@NamedQuery(name = "UnsolvedCall.findAllCall4", query = "SELECT u.unsolvedCallId FROM UnsolvedCall u WHERE u.callProgress != 0 AND u.callProgress != 2 AND u.callProgress != 3  AND u.unsolvedCallId IN (SELECT MAX(u2.unsolvedCallId) FROM UnsolvedCall u2 GROUP BY COALESCE(parent_call_id,unsolved_call_id))"),
		@NamedQuery(name = "UnsolvedCall.countNeighborhoodChart", query = "SELECT COUNT(u.unsolvedCallId) as sumUnsolvedCall, n.name FROM UnsolvedCall u, Neighborhood n, Address a WHERE u.addressId = a.addressId AND a.neighborhoodId = n.neighborhoodId AND u.callProgress != 6 AND u.unsolvedCallId IN (SELECT MAX(u2.unsolvedCallId) FROM UnsolvedCall u2 GROUP BY COALESCE(parent_call_id,unsolved_call_id)) GROUP BY n.name ORDER BY sumUnsolvedCall DESC"),
	//	@NamedQuery(name = "UnsolvedCall.findAllCall2", query = "SELECT u.unsolvedCallId FROM UnsolvedCall u WHERE u.unsolvedCallId IN (SELECT MAX(u2.unsolvedCallId) FROM UnsolvedCall u2 WHERE u2.unsolvedCallId IN (SELECT u3.unsolvedCallId FROM UnsolvedCall u3 WHERE u3.entityEntityCategoryMaps.entityEntityCategoryMapsPK.entityCategoryId IN (SELECT ec FROM EntityCategory ec WHERE ec.entityCollection IN (SELECT ent FROM Entity ent WHERE ent.entityId=1))) GROUP BY COALESCE(parent_call_id,unsolved_call_id))") 
})

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@JsonSerialize(using = UnsolvedCallSerializer.class)

public class UnsolvedCall implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "unsolved_call_id")
	private Long unsolvedCallId;
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
	@NotNull
	@Column(name = "remove")
	private Boolean remove;
	@Transient
	private Collection<byte[]> medias;
	@Transient
	private Collection<String> images;
	@Transient
	@JsonDeserialize(as=ArrayList.class, contentAs=String.class)
	private ArrayList<String> mediasPath;
	@Transient
	private String delay;
	@Transient
	private String callReply;
	@Transient
	private String firstPhoto;
	@Transient
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm", timezone="GMT-3")
	private Date expirationDate;
    @Column(name = "qualification")
    private Short qualification;
	@JoinColumn(name = "observation", referencedColumnName = "additional_information_id")
	@ManyToOne//(cascade = CascadeType.ALL)
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
	private Collection<UnsolvedCall> unsolvedCallCollection;	
	@JoinColumn(name = "parent_call_id", referencedColumnName = "unsolved_call_id")
	@ManyToOne(fetch = FetchType.EAGER)
	private UnsolvedCall parentCallId;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "unsolvedCallId")
	private Collection<Media> mediaCollection;
	@OneToOne(mappedBy = "unsolvedCallId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private WebUser webUserUN;
	@JoinColumns({ @JoinColumn(name = "entity_id", referencedColumnName = "entity_id"),
			@JoinColumn(name = "entity_category_id", referencedColumnName = "entity_category_id") })
	@ManyToOne
	private EntityEntityCategoryMaps entityEntityCategoryMaps;

	public UnsolvedCall() {
	}

	public UnsolvedCall(Long unsolvedCallId) {
		this.unsolvedCallId = unsolvedCallId;
	}

	public UnsolvedCall(Long unsolvedCallId, int callStatus, int callProgress, int callSource,
			Date creationOrUpdateDate, Secret secret, Anonymity anonymity, int priority) {
		this.unsolvedCallId = unsolvedCallId;
		this.callStatus = CallStatus.fromValue(callStatus);
		this.callProgress = CallProgress.fromValue(callProgress);
		this.callSource = CallSource.fromValue(callSource);
		this.creationOrUpdateDate = creationOrUpdateDate;
		this.secret = secret;
		this.anonymity = anonymity;
		this.priority = Priority.fromValue(priority);
	}

	public UnsolvedCall(Long unsolvedCallId, int callStatus, String callProgress, String callSource,
			Date creationOrUpdateDate, Secret secret, Anonymity anonymity, String priority, Collection<byte[]> medias,
			CallClassification callClassificationId, Citizen citizenCpf, 
			SystemUser updatedOrModeratedBy, Collection<UnsolvedCall> unsolvedCallCollection, UnsolvedCall parentCallId,
			Collection<Media> mediaCollection) {
		super();
		this.unsolvedCallId = unsolvedCallId;
		this.callStatus = CallStatus.fromValue(callStatus);
		this.creationOrUpdateDate = creationOrUpdateDate;

		this.secret = secret;
		this.anonymity = anonymity;

		this.medias = medias;
		this.callClassificationId = callClassificationId;
		this.citizenCpf = citizenCpf;

		this.updatedOrModeratedBy = updatedOrModeratedBy;
		this.unsolvedCallCollection = unsolvedCallCollection;
		this.parentCallId = parentCallId;
		this.mediaCollection = mediaCollection;
	}

	public UnsolvedCall(CallStatus callStatus, Secret secret, Anonymity anonymity,
			CallClassification callClassificationId) {
		super();
		this.callStatus = callStatus;

		this.secret = secret;
		this.anonymity = anonymity;

		this.callClassificationId = callClassificationId;

	}

	public Long getUnsolvedCallId() {
		return unsolvedCallId;
	}

	public void setUnsolvedCallId(Long unsolvedCallId) {
		this.unsolvedCallId = unsolvedCallId;
	}

	public WebUser getWebUser() {
		return webUserUN;
	}

	public void setWebUser(WebUser webUser) {
		this.webUserUN = webUser;
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

	public Boolean getRemove() {
		return remove;
	}

	public void setRemove(Boolean remove) {
		this.remove = remove;
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
		else {
			if (this.callStatus == CallStatus.REJEITADO){
				this.callProgress = CallProgress.NOVO;
				this.callStatus = CallStatus.ATIVO;
			}
		}			
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

	public WebUser getWebUserUN() {
		return webUserUN;
	}

	public void setWebUserUN(WebUser webUserUN) {
		this.webUserUN = webUserUN;
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
	public Collection<UnsolvedCall> getUnsolvedCallCollection() {
		return unsolvedCallCollection;
	}

	public void setUnsolvedCallCollection(Collection<UnsolvedCall> unsolvedCallCollection) {
		this.unsolvedCallCollection = unsolvedCallCollection;
	}

	public UnsolvedCall getParentCallId() {
		return parentCallId;
	}

	public void setParentCallId(UnsolvedCall parentCallId) {
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
		return this.webUserUN != null;
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

	public void setQualification(Short qualification) {
		this.qualification = qualification;
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
		hash += (unsolvedCallId != null ? unsolvedCallId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof UnsolvedCall)) {
			return false;
		}
		UnsolvedCall other = (UnsolvedCall) object;
		if ((this.unsolvedCallId == null && other.unsolvedCallId != null)
				|| (this.unsolvedCallId != null && !this.unsolvedCallId.equals(other.unsolvedCallId))) {
			return false;
		}
		return true;
	}

	public SolvedCall toSolvedCall() {
		SolvedCall sc = new SolvedCall();
		sc.setSolvedCallId(this.unsolvedCallId);

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
		sc.setRemove(this.getRemove());
		return sc;
	}

	@Override
	public String toString() {
		return "banco.UnsolvedCall[ unsolvedCallId=" + unsolvedCallId + " ]";
	}

}

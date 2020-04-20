package zup.bean;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonFormat;

import zup.enums.InformationType;
import zup.enums.OperationType;
import zup.model.utils.JSONObjectUserType;

@Entity
@TypeDefs({ @TypeDef(name = "CustomJsonObject", typeClass = JSONObjectUserType.class) })
@Table(name = "log")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Log.findAll", query = "SELECT l FROM Log l"),
    @NamedQuery(name = "Log.findByLogId", query = "SELECT l FROM Log l WHERE l.logId = :logId"),
    @NamedQuery(name = "Log.findByChangeDate", query = "SELECT l FROM Log l WHERE l.changeDate = :changeDate"),
    @NamedQuery(name = "Log.findByOperationType", query = "SELECT l FROM Log l WHERE l.operationType = :operationType"),
    @NamedQuery(name = "Log.findByInformationType", query = "SELECT l FROM Log l WHERE l.informationType = :informationType")})

public class Log implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "log_id")
    private Long logId;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy HH:mm", timezone="GMT-3")
    @Basic(optional = false)
    @Column(name = "change_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date changeDate;
    @Basic(optional = false)
    @Column(name = "operation_type")
    @Enumerated(EnumType.ORDINAL)
    private OperationType operationType;
    @Basic(optional = false)
    @Column(name = "information_type")
    @Enumerated(EnumType.ORDINAL)    
    private InformationType informationType;
    //@Basic(optional = false)
   // @Column(name = "content")
   // @Type(type = "CustomJsonObject")
   // private JSONObject content;
    //@Basic(optional = false)
    @Column(name = "content2")
    private String content2;
    @JoinColumn(name = "system_user_username", referencedColumnName = "system_user_username")
    @ManyToOne(optional = false)
    private SystemUser systemUserUsername;

    public Log() {
    }

    public Log(Long logId) {
        this.logId = logId;
    }

    public Log(Long logId, Date changeDate, OperationType operationType, InformationType informationType, String content2) {
        this.logId = logId;
        this.changeDate = changeDate;
        this.operationType = operationType;
        this.informationType = informationType;
        this.content2 = content2;
    }
    
    public Log( InformationType informationType, String content2) {
        this.changeDate = new Date();
        this.informationType = informationType;
        this.content2 = content2;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public InformationType getInformationType() {
        return informationType;
    }

    public void setInformationType(InformationType informationType) {
        this.informationType = informationType;
    }
/*
    public JSONObject getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = new JSONObject(content);
    }
*/
    public SystemUser getSystemUserUsername() {
        return systemUserUsername;
    }

    public void setSystemUserUsername(SystemUser systemUserUsername) {
        this.systemUserUsername = systemUserUsername;
    }

    
    
    public String getContent2() {
		return content2;
	}

	public void setContent2(String content2) {
		this.content2 = content2;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (logId != null ? logId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Log)) {
            return false;
        }
        Log other = (Log) object;
        if ((this.logId == null && other.logId != null) || (this.logId != null && !this.logId.equals(other.logId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "zup.Log[ logId=" + logId + " ]";
    }
    
}

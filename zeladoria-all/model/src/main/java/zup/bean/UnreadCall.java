package zup.bean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import zup.serializer.UnreadCallSerializer;

/**
 *
 * @author Labitec01
 */
@Entity
@Table(name = "unread_call")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnreadCall.findAll", query = "SELECT u FROM UnreadCall u"),
    @NamedQuery(name = "UnreadCall.findByUnreadCallId", query = "SELECT u FROM UnreadCall u WHERE u.unreadCallId = :unreadCallId")})
@JsonSerialize(using = UnreadCallSerializer.class)
public class UnreadCall implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
	@Basic(optional = false)
    @Column(name = "unread_call_id")
    private Long unreadCallId;
    @Column(name = "read")
    private Boolean read;
    @JoinColumn(name = "solved_call_id", referencedColumnName = "solved_call_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private SolvedCall solvedCallId;
    @JoinColumn(name = "unsolved_call_id", referencedColumnName = "unsolved_call_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UnsolvedCall unsolvedCallId;

    public UnreadCall() {
    }

    public UnreadCall(Long unreadCallId) {
        this.unreadCallId = unreadCallId;
    }

    public Long getUnreadCallId() {
        return unreadCallId;
    }

    public void setUnreadCallId(Long unreadCallId) {
        this.unreadCallId = unreadCallId;
    }

    public Boolean getRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}

	public SolvedCall getSolvedCallId() {
        return solvedCallId;
    }

    public void setSolvedCallId(SolvedCall solvedCallId) {
        this.solvedCallId = solvedCallId;
    }

    public UnsolvedCall getUnsolvedCallId() {
        return unsolvedCallId;
    }

    public void setUnsolvedCallId(UnsolvedCall unsolvedCallId) {
        this.unsolvedCallId = unsolvedCallId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (unreadCallId != null ? unreadCallId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UnreadCall)) {
            return false;
        }
        UnreadCall other = (UnreadCall) object;
        if ((this.unreadCallId == null && other.unreadCallId != null) || (this.unreadCallId != null && !this.unreadCallId.equals(other.unreadCallId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.UnreadCall[ unreadCallId=" + unreadCallId + " ]";
    }
    
}

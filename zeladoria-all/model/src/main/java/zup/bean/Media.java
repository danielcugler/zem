package zup.bean;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "media")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Media.findAll", query = "SELECT m FROM Media m"),
		@NamedQuery(name = "Media.findByMediaId", query = "SELECT m FROM Media m WHERE m.mediaId = :mediaId") })

public class Media implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "media_id")
	private Long mediaId;
	@Basic(optional = false)
	@NotNull

	@Column(name = "media")
	private byte[] media;
	@JoinColumn(name = "solved_call_id", referencedColumnName = "solved_call_id")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private SolvedCall solvedCallId;
	@JoinColumn(name = "unsolved_call_id", referencedColumnName = "unsolved_call_id")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private UnsolvedCall unsolvedCallId;

	public Media() {
	}

	public Media(Long mediaId) {
		this.mediaId = mediaId;
	}

	public Media(Long mediaId, byte[] media) {
		this.mediaId = mediaId;
		this.media = media;
	}

	public Long getMediaId() {
		return mediaId;
	}

	public void setMediaId(Long mediaId) {
		this.mediaId = mediaId;
	}

	public byte[] getMedia() {
		return media;
	}

	public void setMedia(byte[] media) {
		this.media = media;
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
		hash += (mediaId != null ? mediaId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Media)) {
			return false;
		}
		Media other = (Media) object;
		if ((this.mediaId == null && other.mediaId != null)
				|| (this.mediaId != null && !this.mediaId.equals(other.mediaId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "banco.Media[ mediaId=" + mediaId + " ]";
	}

}

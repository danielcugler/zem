package zup.bean;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "web_user")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "WebUser.findAll", query = "SELECT w FROM WebUser w"),
		@NamedQuery(name = "WebUser.findByPublicIdentificationKey", query = "SELECT w FROM WebUser w WHERE w.publicIdentificationKey = :publicIdentificationKey"),
		@NamedQuery(name = "WebUser.findByName", query = "SELECT w FROM WebUser w WHERE w.name = :name"),
		@NamedQuery(name = "WebUser.findByWebUserCpf", query = "SELECT w FROM WebUser w WHERE w.webUserCpf = :webUserCpf"),
		@NamedQuery(name = "WebUser.findByEmail", query = "SELECT w FROM WebUser w WHERE w.email = :email") })

public class WebUser implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "public_identification_key")
	@Size(min = 32, max = 32)
	private String publicIdentificationKey;
	@Basic(optional = false)
	@Column(name = "name")
	private String name;
	@Basic(optional = false)
	@Column(name = "web_user_cpf")
	private String webUserCpf;
	@Basic(optional = false)
	@Column(name = "email")
	private String email;
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "solved_call_id")
	private SolvedCall solvedCallId;
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "unsolved_call_id")
	private UnsolvedCall unsolvedCallId;

	public WebUser() {
	}

	public WebUser(String publicIdentificationKey) {
		this.publicIdentificationKey = publicIdentificationKey;
	}

	public WebUser(String publicIdentificationKey, String name, String webUserCpf, String email) {
		this.publicIdentificationKey = publicIdentificationKey;
		this.name = name;
		this.webUserCpf = webUserCpf;
		this.email = email;
	}

	public String getPublicIdentificationKey() {
		return publicIdentificationKey;
	}

	public void setPublicIdentificationKey(String publicIdentificationKey) {
		this.publicIdentificationKey = publicIdentificationKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWebUserCpf() {
		return webUserCpf;
	}

	public void setWebUserCpf(String webUserCpf) {
		this.webUserCpf = webUserCpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
		hash += (publicIdentificationKey != null ? publicIdentificationKey.hashCode() : 0);
		return hash;
	}

	public boolean getCallType() {
		return unsolvedCallId == null;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof WebUser)) {
			return false;
		}
		WebUser other = (WebUser) object;
		if ((this.publicIdentificationKey == null && other.publicIdentificationKey != null)
				|| (this.publicIdentificationKey != null
						&& !this.publicIdentificationKey.equals(other.publicIdentificationKey))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "javaapplication1.WebUser[ publicIdentificationKey=" + publicIdentificationKey + " ]";
	}

}

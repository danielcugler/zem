/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zup.bean;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author labitec
 */
@Entity
@Table(name = "firebase")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Firebase.findAll", query = "SELECT f FROM Firebase f")
    , @NamedQuery(name = "Firebase.findByFirebaseId", query = "SELECT f FROM Firebase f WHERE f.firebaseId = :firebaseId")
    , @NamedQuery(name = "Firebase.findByToken", query = "SELECT f FROM Firebase f WHERE f.token = :token")})
public class Firebase implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "firebase_id")
    private Integer firebaseId;
    @Basic(optional = false)
    @Column(name = "token")
    private String token;
    @Basic(optional = false)
    @Column(name = "platform")
    private String platform;

    public Firebase() {
    }

    public Firebase(Integer firebaseId) {
        this.firebaseId = firebaseId;
    }

    public Firebase(Integer firebaseId, String token) {
        this.firebaseId = firebaseId;
        this.token = token;
    }

    public Integer getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(Integer firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (firebaseId != null ? firebaseId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Firebase)) {
            return false;
        }
        Firebase other = (Firebase) object;
        if ((this.firebaseId == null && other.firebaseId != null) || (this.firebaseId != null && !this.firebaseId.equals(other.firebaseId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.Firebase[ firebaseId=" + firebaseId + " ]";
    }
    
}

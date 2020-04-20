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
 * @author rodrigo
 */
@Entity
@Table(name = "citizen_login")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CitizenLogin.findAll", query = "SELECT c FROM CitizenLogin c"),
    @NamedQuery(name = "CitizenLogin.findByCitizenLoginId", query = "SELECT c FROM CitizenLogin c WHERE c.citizenLoginId = :citizenLoginId"),
    @NamedQuery(name = "CitizenLogin.findByCitizenId", query = "SELECT c FROM CitizenLogin c WHERE c.citizenId = :citizenId"),
    @NamedQuery(name = "CitizenLogin.findByToken", query = "SELECT c FROM CitizenLogin c WHERE c.token = :token")})
public class CitizenLogin implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "citizen_login_id")
    private Integer citizenLoginId;
    @Basic(optional = false)
    @Column(name = "citizen_id")
    private String citizenId;
    @Basic(optional = false)
    @Column(name = "token")
    private String token;

    public CitizenLogin() {
    }

    public CitizenLogin(Integer citizenLoginId) {
        this.citizenLoginId = citizenLoginId;
    }

    public CitizenLogin(Integer citizenLoginId, String citizenId, String token) {
        this.citizenLoginId = citizenLoginId;
        this.citizenId = citizenId;
        this.token = token;
    }

    public Integer getCitizenLoginId() {
        return citizenLoginId;
    }

    public void setCitizenLoginId(Integer citizenLoginId) {
        this.citizenLoginId = citizenLoginId;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (citizenLoginId != null ? citizenLoginId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CitizenLogin)) {
            return false;
        }
        CitizenLogin other = (CitizenLogin) object;
        if ((this.citizenLoginId == null && other.citizenLoginId != null) || (this.citizenLoginId != null && !this.citizenLoginId.equals(other.citizenLoginId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.zup.entity.CitizenLogin[ citizenLoginId=" + citizenLoginId + " ]";
    }
    
}

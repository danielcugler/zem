package zup.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

//@Entity
//@Table(name = "read_broadcast_message")
//@XmlRootElement
public class ReadBroadcastMessage {
//	@Column(name = "citizen_cpf")
	private String citizenCpf;
//	@Column(name = "broadcast_message_ids")
	private Integer broadcastMessageId;

	public String getCitizenCpf() {
		return citizenCpf;
	}

	public void setCitizenCpf(String citizenCpf) {
		this.citizenCpf = citizenCpf;
	}

	public Integer getBroadcastMessageId() {
		return broadcastMessageId;
	}

	public void setBroadcastMessageId(Integer broadcastMessageId) {
		this.broadcastMessageId = broadcastMessageId;
	}

}

package filters;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

public class BroadcastMessageFilterBean {
	private @QueryParam("page") int page;	
	private @QueryParam("subject") String subject;
	private @QueryParam("bmc") String bmc;
	private @QueryParam("creationDate") String creationDate; 
	private @QueryParam("publicationDate") String publicationDate;
	private @QueryParam("enabled") String enabled;
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBmc() {
		return bmc;
	}
	public void setBmc(String bmc) {
		this.bmc = bmc;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getPublicationDate() {
		return publicationDate;
	}
	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	
}

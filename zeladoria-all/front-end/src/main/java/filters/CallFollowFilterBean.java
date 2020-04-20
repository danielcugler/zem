package filters;

import javax.ws.rs.QueryParam;

public class CallFollowFilterBean {
	@QueryParam("page") int page;
	@QueryParam("iniDate") String iniDate;
	@QueryParam("endDate") String endDate;
	@QueryParam("callSource") String callSource;
	@QueryParam("callClassificationId") String callClassificationId;
	@QueryParam("priority") String priority;
	@QueryParam("callDelay") String callDelay;
	@QueryParam("unsolvedCallId") String unsolvedCallId;
	@QueryParam("citizenCpf") String citizenCpf;
	@QueryParam("entityId") String entityId;
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public String getIniDate() {
		return iniDate;
	}
	public void setIniDate(String iniDate) {
		this.iniDate = iniDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCallSource() {
		return callSource;
	}
	public void setCallSource(String callSource) {
		this.callSource = callSource;
	}
	public String getCallClassificationId() {
		return callClassificationId;
	}
	public void setCallclassificationid(String callClassificationId) {
		this.callClassificationId = callClassificationId;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getCallDelay() {
		return callDelay;
	}
	public void setCallDelay(String callDelay) {
		this.callDelay = callDelay;
	}
	public String getUnsolvedCallId() {
		return unsolvedCallId;
	}
	public void setUnsolvedCallId(String unsolvedCallId) {
		this.unsolvedCallId = unsolvedCallId;
	}
	public String getCitizenCpf() {
		return citizenCpf;
	}
	public void setCitizenCpf(String citizenCpf) {
		this.citizenCpf = citizenCpf;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public void setCallClassificationId(String callClassificationId) {
		this.callClassificationId = callClassificationId;
	}

}

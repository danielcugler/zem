package filters;

import javax.ws.rs.QueryParam;

public class CallMonitoringFilterBean {
	@QueryParam("page") int page;
	@QueryParam("iniDate") String iniDate;
	@QueryParam("endDate") String endDate;
	@QueryParam("callSource") String callSource;
	@QueryParam("callClassificationId") String callClassificationId;
	@QueryParam("entityCategoryId") String entityCategoryId;
	@QueryParam("priority") String priority;
	@QueryParam("username") String username;
	@QueryParam("callStatus") String callStatus;
	@QueryParam("unsolvedCallId") String unsolvedCallId;
	@QueryParam("citizenCpf") String citizenCpf;
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
	public String getEntityCategoryId() {
		return entityCategoryId;
	}
	public void setCategoryEntityId(String entityCategoryId) {
		this.entityCategoryId = entityCategoryId;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCallStatus() {
		return callStatus;
	}
	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
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

}

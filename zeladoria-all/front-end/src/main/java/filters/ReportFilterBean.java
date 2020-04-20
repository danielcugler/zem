package filters;

import java.util.List;

import javax.ws.rs.QueryParam;

public class ReportFilterBean {
	@QueryParam("user")
	String user;
	@QueryParam("type")
	String type;
	@QueryParam("format")
	String format;
	@QueryParam("iniDate")
	String initDate;
	@QueryParam("endDate")
	String endDate;
	@QueryParam("callClassification")
	String callClassification;
	@QueryParam("callSource")
	String callSource;
	@QueryParam("callProgress")
	String callProgress;
	@QueryParam("entity")
	String entity;
	@QueryParam("category")
	String category;
	@QueryParam("priority")
	String priority;
	@QueryParam("chart")
	String chart;
	@QueryParam("fields")
	List<Integer> fields;
		public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getInitDate() {
		return initDate;
	}
	public void setInitDate(String initDate) {
		this.initDate = initDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCallClassification() {
		return callClassification;
	}
	public void setCallClassification(String callClassification) {
		this.callClassification = callClassification;
	}
	public String getCallSource() {
		return callSource;
	}
	public void setCallSource(String callSource) {
		this.callSource = callSource;
	}
	public String getCallProgress() {
		return callProgress;
	}
	public void setCallProgress(String callProgress) {
		this.callProgress = callProgress;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getChart() {
		return chart;
	}
	public void setChart(String chart) {
		this.chart = chart;
	}
	public List<Integer> getFields() {
		return fields;
	}
	public void setFields(List<Integer> fields) {
		this.fields = fields;
	}

	
}

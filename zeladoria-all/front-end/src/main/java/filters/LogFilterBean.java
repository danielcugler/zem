package filters;
import javax.ws.rs.QueryParam;
import zup.enums.InformationType;
import zup.enums.OperationType;

public class LogFilterBean {
private @QueryParam("page") int page;	
private @QueryParam("username") String username;
private @QueryParam("informationType") String informationType;
private @QueryParam("operationType") String operationType;
private @QueryParam("fromDate") String fromDate;
private @QueryParam("toDate") String toDate;
public int getPage() {
	return page;
}
public void setPage(int page) {
	this.page = page;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}

public String getInformationType() {
	return informationType;
}
public void setInformationType(String informationType) {
	this.informationType = informationType;
}
public String getOperationType() {
	return operationType;
}
public void setOperationType(String operationType) {
	this.operationType = operationType;
}
public String getFromDate() {
	return fromDate;
}
public void setFromDate(String fromDate) {
	this.fromDate = fromDate;
}
public String getToDate() {
	return toDate;
}
public void setToDate(String toDate) {
	this.toDate = toDate;
}
}


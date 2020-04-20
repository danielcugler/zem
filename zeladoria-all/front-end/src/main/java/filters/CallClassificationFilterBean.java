package filters;

import javax.ws.rs.QueryParam;

public class CallClassificationFilterBean {
private @QueryParam("page") int page;	
private @QueryParam("name") String name;
private @QueryParam("addressRequired") String addressRequired;
//private @QueryParam("enabled") String enabled;
public int getPage() {
	return page;
}
public void setPage(int page) {
	this.page = page;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}

public String getAddressRequired() {
	return addressRequired;
}
public void setAddressRequired(String addressRequired) {
	this.addressRequired = addressRequired;
}
//public String getEnabled() {
//	return enabled;
//}
//public void setEnabled(String enabled) {
//	this.enabled = enabled;
//}

}

package filters;

import javax.ws.rs.QueryParam;

public class EntityCategoryFilterBean {
private @QueryParam("page") int page;	
private @QueryParam("name") String name;
private @QueryParam("enabled") String enabled;
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
public String getEnabled() {
	return enabled;
}
public void setEnabled(String enabled) {
	this.enabled = enabled;
}

}

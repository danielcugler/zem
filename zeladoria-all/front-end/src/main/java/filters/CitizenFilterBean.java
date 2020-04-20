package filters;

import javax.ws.rs.QueryParam;

public class CitizenFilterBean {

	private @QueryParam("page") int page;
	private @QueryParam("name") String name;
	private @QueryParam("email") String email;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

}

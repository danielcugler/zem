package zup.dto;

import java.io.Serializable;
import java.util.Date;

public class DelayCountDTO implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private Integer time;
private Date creationDate;
public Integer getTime() {
	return time;
}
public void setTime(Integer time) {
	this.time = time;
}
public Date getCreationDate() {
	return creationDate;
}
public void setCreationDate(Date creationDate) {
	this.creationDate = creationDate;
}

}

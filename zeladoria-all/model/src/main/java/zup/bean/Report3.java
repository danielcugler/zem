package zup.bean;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonFormat;

import flexjson.JSONSerializer;
import zup.enums.CallProgress;
import zup.enums.CallSource;
import zup.enums.Priority;

public class Report3 implements Comparable<Report3>{
	private String description;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "pt-BR", timezone = "Brazil/East")
	private Date creationOrUpdateDate;
	private String callClassification;
	private CallSource callSource;
	private String entity;
	private Integer entityId;
	private String entityCategory;
	private Integer EntityCategoryId;
	private CallProgress callProgress;
	private Priority priority;
	private String neighborhood;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreationOrUpdateDate() {
		return creationOrUpdateDate;
	}
	public void setCreationOrUpdateDate(Date creationOrUpdateDate) {
		this.creationOrUpdateDate = creationOrUpdateDate;
	}
	public String getCallClassification() {
		return callClassification;
	}
	public void setCallClassification(String callClassification) {
		this.callClassification = callClassification;
	}
	public String getCallSource() {
		return callSource.getStr();
	}
	public void setCallSource(CallSource callSource) {
		this.callSource = callSource;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public String getEntityCategory() {
		return entityCategory;
	}
	public void setEntityCategory(String entityCategory) {
		this.entityCategory = entityCategory;
	}
	public String getCallProgress() {
		return callProgress.getStr();
	}
	public void setCallProgress(CallProgress callProgress) {
		this.callProgress = callProgress;
	}
	public String getPriority() {
		return priority.getStr();
	}
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	public String getNeighborhood() {
		return neighborhood;
	}
	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}
	
	public Integer getEntityId() {
		return entityId;
	}
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}
	public Integer getEntityCategoryId() {
		return EntityCategoryId;
	}
	public void setEntityCategoryId(Integer entityCategoryId) {
		EntityCategoryId = entityCategoryId;
	}
	@Override
	public String toString() {
		return "Report3 [description=" + description + ", creationOrUpdateDate=" + creationOrUpdateDate
				+ ", callClassification=" + callClassification + ", callSource=" + callSource + ", entity=" + entity
				+ ", entityCategory=" + entityCategory + ", callProgress=" + callProgress + ", priority=" + priority
				+ ", neighborhood=" + neighborhood + "]";
	}
	@Override
	public int compareTo(Report3 o) {
		
		if(this.creationOrUpdateDate.before(o.creationOrUpdateDate)){
			return -1;
		}else if(this.creationOrUpdateDate.after(o.creationOrUpdateDate)){
			return 1;
		}		
		return 0;
	}
}

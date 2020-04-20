package filters;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.QueryParam;

import zup.enums.CallProgress;
import zup.enums.CallSource;
import zup.enums.Priority;

public class CallMonitorFilterBean {
	private @QueryParam("page") int page;
	private @QueryParam("callSource") List<Integer> callSource;
	private @QueryParam("entity") List<Integer> entity;
	private @QueryParam("priority") List<Integer> priority;
	private @QueryParam("callClassification") List<Integer> callClassification;
	private @QueryParam("callProgress") List<Integer> callProgress;

	private int orderParam;
	private int order;

	public int getOrderParam() {
		return orderParam;
	}

	public void setOrderParam(int orderParam) {
		this.orderParam = orderParam;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public List<Integer> getCallSource() {
		return callSource;
	}
	public List<CallSource> getCallSourceEnum() {
		return CallSource.getList(callSource);
	}
	
	public void setCallSource(List<Integer> callSource) {
		this.callSource = callSource;
	}



	public List<Integer> getEntity() {
		return entity;
	}

	public void setEntity(List<Integer> entity) {
		this.entity = entity;
	}

	public List<Integer> getPriority() {
		return priority;
	}
	public List<Priority> getPriorityEnum() {
		return Priority.getList(callSource);
	}

	
	public void setPriority(List<Integer> priority) {
		this.priority = priority;
	}

	public List<Integer> getCallClassification() {
		return callClassification;
	}

	public void setCallClassification(List<Integer> callClassification) {
		this.callClassification = callClassification;
	}

	public List<Integer> getCallProgress() {
		return callProgress;
	}
	public List<CallProgress> getCallProgressEnum() {
		return CallProgress.getList(callSource);
	}

	public void setCallProgress(List<Integer> callProgress) {
		this.callProgress = callProgress;
	}

	public List<CallSource> getEnumCallSource() {
		List<CallSource> list = new ArrayList<CallSource>();
		for (Integer ii : this.callSource)
			list.add(CallSource.fromValue(ii));
		return list;
	}

	public List<Priority> getEnumPriority() {
		List<Priority> list = new ArrayList<Priority>();
		for (Integer ii : this.priority)
			list.add(Priority.fromValue(ii));
		return list;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public List<CallProgress> getEnumCallProgress() {
		List<CallProgress> list = new ArrayList<CallProgress>();
		for (Integer ii : this.callProgress)
			list.add(CallProgress.fromValue(ii));
		return list;
	}

}

package zup.webservices.adm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Charts {
	private LinkedHashMap<String, Long> entityCounter;
	private LinkedHashMap<String, Long> entityCategoryCounter;
	private LinkedHashMap<String, Long> callProgressCounter;
	private LinkedHashMap<String, Long> callClassificationCounter;
	private LinkedHashMap<String, Integer> callDelayCounter;
	public LinkedHashMap<String, Long> getEntityCounter() {
		return entityCounter;
	}
	public void setEntityCounter(LinkedHashMap<String, Long> entityCounter) {
		this.entityCounter = entityCounter;
	}
	public LinkedHashMap<String, Long> getEntityCategoryCounter() {
		return entityCategoryCounter;
	}
	public void setEntityCategoryCounter(LinkedHashMap<String, Long> entityCategoryCounter) {
		this.entityCategoryCounter = entityCategoryCounter;
	}
	public LinkedHashMap<String, Long> getCallProgressCounter() {
		return callProgressCounter;
	}
	public void setCallProgressCounter(LinkedHashMap<String, Long> callProgressCounter) {
		this.callProgressCounter = callProgressCounter;
	}
	public LinkedHashMap<String, Long> getCallClassificationCounter() {
		return callClassificationCounter;
	}
	public void setCallClassificationCounter(LinkedHashMap<String, Long> map) {
		this.callClassificationCounter = map;
	}
	public LinkedHashMap<String, Integer> getCallDelayCounter() {
		return callDelayCounter;
	}
	public void setCallDelayCounter(LinkedHashMap<String, Integer> callDelayCounter) {
		this.callDelayCounter = callDelayCounter;
	}

}

package zup.dto;

import zup.enums.CallProgress;

public class CallProgressDTO {
private CallProgress callProgress;
private Integer count;
public CallProgress getCallProgress() {
	return callProgress;
}
public void setCallProgress(CallProgress callProgress) {
	this.callProgress = callProgress;
}
public Integer getCount() {
	return count;
}
public void setCount(Integer count) {
	this.count = count;
}

}

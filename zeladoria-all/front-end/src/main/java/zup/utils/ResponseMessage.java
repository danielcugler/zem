package zup.utils;

public class ResponseMessage {
private String message;
private String code;
public ResponseMessage(String message, String code) {
	super();
	this.message = message;
	this.code = code;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public String getCode() {
	return code;
}
public void setCode(String code) {
	this.code = code;
}

}

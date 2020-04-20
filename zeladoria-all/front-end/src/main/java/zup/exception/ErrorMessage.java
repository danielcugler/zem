package zup.exception;

import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONObject;

@XmlRootElement
public class ErrorMessage {

	private String errorMessage;
	private String errorCode;
	private String documentation;

	public ErrorMessage() {

	}

	public ErrorMessage(String errorMessage, String errorCode, String documentation) {
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
		this.documentation = documentation;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	public String toJSON() {
		JSONObject json = new JSONObject();
		json.append("errorMessage", this.errorMessage);
		json.append("errorCode", this.errorCode);
		json.append("documentation", this.documentation);
		return json.toString();
	}

}
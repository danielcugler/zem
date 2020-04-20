package dto;

import javax.ws.rs.QueryParam;

public class EvalDTO {
	Long solvedCallId;
	Short evaluation;
	String token;
	public Long getSolvedCallId() {
		return solvedCallId;
	}
	public void setSolvedCallId(Long solvedCallId) {
		this.solvedCallId = solvedCallId;
	}
	public Short getEvaluation() {
		return evaluation;
	}
	public void setEvaluation(Short evaluation) {
		this.evaluation = evaluation;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
}

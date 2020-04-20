package zup.bean;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;


import flexjson.JSONSerializer;
import zup.enums.CallProgress;
import zup.enums.CallSource;
import zup.enums.Priority;

public class Report {
	private String cdescription;
	private Date creation_or_update_date;
	private String cname;
	private String call_source;
	private String ename;
	private String ecname;
	private String call_progress;
	private String priority;
	private String neighborhood_name;
	private BigInteger count;

	public String getCdescription() {
		return cdescription;
	}
	public void setCdescription(String cdescription) {
		this.cdescription = cdescription;
	}
	public Date getCreation_or_update_date() {
		return creation_or_update_date;
	}
	public void setCreation_or_update_date(Date creation_or_update_date) {
		this.creation_or_update_date = creation_or_update_date;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getEcname() {
		return ecname;
	}
	public void setEcname(String ecname) {
		this.ecname = ecname;
	}

	public String getNeighborhood_name() {
		return neighborhood_name;
	}
	public void setNeighborhood_name(String neighborhood_name) {
		this.neighborhood_name = neighborhood_name;
	}

	
	public String getCall_source() {
		return call_source;
	}
	public void setCall_source(Integer call_source) {
		this.call_source = CallSource.fromValue(call_source).getStr();;
	}
	public String getCall_progress() {
		return call_progress;
	}
	public void setCall_progress(Integer call_progress) {
		this.call_progress =  CallProgress.fromValue(call_progress).getStr(); ;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = Priority.fromValue(priority).getStr();;
	}
	

	public BigInteger getCount() {
		return count;
	}
	public void setCount(BigInteger count) {
		this.count = count;
	}
	@Override
	public String toString() {
		// return "Relatorio : "+"Data : "+this.getCreation_or_update_date()+",Classificacao : "+this.getCname()+ ",Origem : "+this.getCall_source()+",Entidade: "+this.getEname()+",EntidadeCategoria: "+this.getEcname()+",Progresso: "+this.getCall_progress()+",Prioridade: "+this.getPriority()+",bairro: "+this.getNeighborhood_name();
		 return "Relatorio : "+"Data : "+this.getCreation_or_update_date()+"Descricao : " + this.getCdescription() +",Classificacao : "+this.getCname()+ ",Origem : "+this.getCall_source()+",Entidade: "+this.getEname()+",EntidadeCategoria: "+this.getEcname()+",Progresso: "+this.getCall_progress()+",Prioridade: "+this.getPriority()+",bairro: "+this.getNeighborhood_name();
	}
}

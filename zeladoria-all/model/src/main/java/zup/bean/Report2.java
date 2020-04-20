package zup.bean;

import java.util.Date;

import zup.enums.CallProgress;
import zup.enums.CallSource;
import zup.enums.Priority;

public class Report2 {
	private String cdescription;
	private Date creation_or_update_date;
	private String cname;
	private int call_source;
	private String ename;
	private String ecname;
	private int call_progress;
	private int priority;
	private String neighborhood_name;

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
	public int getCall_source() {
		return call_source;
	}
	public void setCall_source(int call_source) {
		this.call_source = call_source;
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
	public int getCall_progress() {
		return call_progress;
	}
	public void setCall_progress(int call_progress) {
		this.call_progress = call_progress;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getNeighborhood_name() {
		return neighborhood_name;
	}
	public void setNeighborhood_name(String neighborhood_name) {
		this.neighborhood_name = neighborhood_name;
	}

	@Override
	public String toString() {
		// return "Relatorio : "+"Data : "+this.getCreation_or_update_date()+",Classificacao : "+this.getCname()+ ",Origem : "+this.getCall_source()+",Entidade: "+this.getEname()+",EntidadeCategoria: "+this.getEcname()+",Progresso: "+this.getCall_progress()+",Prioridade: "+this.getPriority()+",bairro: "+this.getNeighborhood_name();
		 return "Relatorio : "+"Data : "+this.getCreation_or_update_date()+"Descricao : " + this.getCdescription() +",Classificacao : "+this.getCname()+ ",Origem : "+this.getCall_source()+",Entidade: "+this.getEname()+",EntidadeCategoria: "+this.getEcname()+",Progresso: "+this.getCall_progress()+",Prioridade: "+this.getPriority()+",bairro: "+this.getNeighborhood_name();
	}
}

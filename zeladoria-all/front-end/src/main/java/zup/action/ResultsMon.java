package zup.action;

import java.util.List;

import zup.bean.UnsolvedCall;
import zup.webservices.adm.Charts;

public class ResultsMon {
private List<UnsolvedCall> list;
private Charts charts;

public List<UnsolvedCall> getList() {
	return list;
}
public void setList(List<UnsolvedCall> list) {
	this.list = list;
}
public Charts getCharts() {
	return charts;
}
public void setCharts(Charts charts) {
	this.charts = charts;
}
}

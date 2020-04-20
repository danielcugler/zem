package zup.bean;

import java.util.List;

public class Result<T> {
private int count;
private List<T> list;


public Result(int count, List<T> list) {
	super();
	this.count = count;
	this.list = list;
}
public int getCount() {
	return count;
}
public void setCount(int count) {
	this.count = count;
}
public List<T> getList() {
	return list;
}
public void setResult(List<T> list) {
	this.list = list;
}

}

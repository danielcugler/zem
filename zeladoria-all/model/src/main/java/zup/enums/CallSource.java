package zup.enums;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CallSource {

	WEB(0, "Web"), MOVEL(1, "Móvel");
	private int value;
	private String str;

	private CallSource(int value, String str) {
		this.value = value;
		this.str = str;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
@JsonValue
	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}
public static List<CallSource> getList(List<Integer> list){
	List<CallSource> l=new ArrayList<CallSource>();
	for(Integer i:list)
		l.add(fromValue(i));
	return l;
}
	
	public static CallSource fromValue(int v) {
		for (CallSource c : CallSource.values())
			if (c.value == v)
				return c;
		return null;
	}
}

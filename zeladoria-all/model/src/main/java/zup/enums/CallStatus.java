package zup.enums;

import java.util.ArrayList;
import java.util.List;

public enum CallStatus {
	ATIVO(0, "Ativo"), REJEITADO(1, "Rejeitado");
	private int value;
	private String str;

	private CallStatus(int value, String str) {
		this.value = value;
		this.str = str;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}
	public static List<CallStatus> getList(List<Integer> list){
		List<CallStatus> l=new ArrayList<CallStatus>();
		for(Integer i:list)
			l.add(fromValue(i));
		return l;
	}
	public static CallStatus fromValue(int v) {
		for (CallStatus c : CallStatus.values())
			if (c.value == v)
				return c;
		return null;
	}
}

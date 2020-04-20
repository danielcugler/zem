package zup.enums;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Priority {
	BAIXA(0, "Baixa"), MEDIA(1, "Média"), ALTA(2, "Alta");
	private int value;
	private String str;

	private Priority(int value, String str) {
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

	public static List<Priority> getList(List<Integer> list){
		List<Priority> l=new ArrayList<Priority>();
		for(Integer i:list)
			l.add(fromValue(i));
		return l;
	}
	 @JsonValue
	public static Priority fromValue(int v) {
		for (Priority c : Priority.values())
			if (c.value == v)
				return c;
		return null;
	}
}

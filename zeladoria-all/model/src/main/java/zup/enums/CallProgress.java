package zup.enums;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CallProgress {

	NOVO(0, "Novo"), ENCAMINHADO(1, "Encaminhado"), PROCESSADO(2, "Processado"), FINALIZADO(3,
			"Finalizado"), VISUALIZADO(4, "Visualizado"), ANDAMENTO(5, "Em Andamento"), REJEITADO(6, "Rejeitado");
	private int value;
	private String str;

	private CallProgress(int value, String str) {
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
	public static List<CallProgress> getList(List<Integer> list){
		List<CallProgress> l=new ArrayList<CallProgress>();
		for(Integer i:list)
			l.add(fromValue(i));
		return l;
	}
	
	
	public void setStr(String str) {
		this.str = str;
	}

	public static CallProgress fromValue(int v) {
		for (CallProgress c : CallProgress.values())
			if (c.value == v)
				return c;
		return null;
	}

}

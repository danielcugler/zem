package zup.enums;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CallClassificationName {
	RECLAMACAO(0,"Reclamação"), CRITICA(1,"Crítica"), DENUNCIA(2,"Denúncia"), SUGESTAO(3,"Sugestão"), ELOGIO(4,"Elogio"), SOLICITACOES(5,"Solicitações"), PEDIDOSDEINFORMACAO(6,"Pedidos de informação");
	private int value;
private String str;
	private CallClassificationName(int value, String str) {
		this.value = value;
		this.str = str;
	}


	public int getValue() {
		return this.value;
	}

	
	@JsonValue
	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@JsonCreator
	public static CallClassificationName fromValue(int v) {
		for (CallClassificationName c : CallClassificationName.values())
			if (c.value == v)
				return c;
		return null;
	}
}

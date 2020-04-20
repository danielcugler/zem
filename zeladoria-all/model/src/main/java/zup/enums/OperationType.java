package zup.enums;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import zup.serializer.OperationTypeSerializer;

@JsonSerialize(using = OperationTypeSerializer.class)
public enum OperationType{
	INCLUSAO(0, "Inclusão"), EDICAO(1, "Edição"), ATIVACAO(2, "Ativação"), INATIVACAO(3, "Inativação"), PUBLICACAO(4,
			"Publicação"), ALTERARSENHA(5, "Alteração de senha");
	private int value;
	private String str;

	private OperationType(int value, String str) {
		this.value = value;
		this.str = str;
	}

	
	public int getValue() {
		return this.value;
	}
	public String getStr() {
		return str;
	}


	public void setStr(String str) {
		this.str = str;
	}

	public static OperationType fromValue(int v) {
		for (OperationType c : OperationType.values())
			if (c.value == v)
				return c;
		return null;
	}
}

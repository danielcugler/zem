package zup.enums;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import zup.serializer.InformationTypeSerializer;
@JsonSerialize(using = InformationTypeSerializer.class)
public enum InformationType {
	ATTENDANCETIME(0, "Tempo de atendimento"), BROADCASTMESSAGE(1, "Comunicado em massa"), CALLCLASSIFICATION(2,
			"Classificação do chamado"), CITZEN(3, "Cidadão"), ENTITY(4, "Entidade"), ENTITYCATEGORY(5,
					"Categoria de entidade"), MESSAGEMODEL(6, "Modelo de mensagem"), SOLVEDCALL(7,
							"Chamado resolvido"), SYSTEMUSER(8, "Usuário"), SYSTEMUSERPROFILE(9,
									"Perfil de usuário"), UNSOLVEDCALL(10, "Chamado não resolvido"), WEBUSER(11,
											"Usuário Web"),NEIGHBORHOOD(12,	"Bairro");
	private int value;
	private String str;

	private InformationType(int value, String str) {
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

	public static InformationType fromValue(int v) {
		for (InformationType c : InformationType.values())
			if (c.value == v)
				return c;
		return null;
	}
}

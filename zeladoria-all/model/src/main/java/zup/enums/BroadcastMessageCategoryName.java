package zup.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BroadcastMessageCategoryName {
	NORMAL(0, "Normal"), EMERGENCIAL(1, "Emergencial");
	private int value;
	private String str;

	private BroadcastMessageCategoryName(int value, String str) {
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

	public static BroadcastMessageCategoryName fromValue(int v) {
		for (BroadcastMessageCategoryName c : BroadcastMessageCategoryName.values())
			if (c.value == v)
				return c;
		return null;
	}
}

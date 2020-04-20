package zup.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Enabled {
	ENABLED(0, "Enabled"), DISABLED(1, "Disabled");
	private int value;
	private String str;

	private Enabled(int value, String str) {
		this.value = value;
		this.str = str;
	}

	@JsonValue
	public int getValue() {
		return this.value;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public static Enabled fromValue(int v) {
		for (Enabled c : Enabled.values())
			if (c.value == v)
				return c;
		return null;
	}
}

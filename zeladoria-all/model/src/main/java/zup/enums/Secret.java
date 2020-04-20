package zup.enums;

public enum Secret {
	ENABLED(0, "Enabled"), DISABLED(1, "Disabled");
	private int value;
	private String str;

	private Secret(int value, String str) {
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

	public static Secret fromValue(int v) {
		for (Secret c : Secret.values())
			if (c.value == v)
				return c;
		return null;
	}
}

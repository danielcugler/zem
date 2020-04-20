package zup.enums;

public enum Anonymity {
	ENABLED(0, "Enabled"), DISABLED(1, "Disabled");
	private int value;
	private String str;

	private Anonymity(int value, String str) {
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

	public static Anonymity fromValue(int v) {
		for (Anonymity c : Anonymity.values())
			if (c.value == v)
				return c;
		return null;
	}
}

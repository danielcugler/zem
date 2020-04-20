package zup.report;

public class Column {
	private String title;
	private String field;
	private String dataType;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public Column(String title, String field, String dataType) {
		
		this.title = title;
		this.field = field;
		this.dataType = dataType;
	}
	
	

}
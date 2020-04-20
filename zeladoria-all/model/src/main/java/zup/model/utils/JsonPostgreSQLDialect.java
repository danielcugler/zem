package zup.model.utils;

import java.sql.Types;

import org.hibernate.dialect.PostgreSQL9Dialect;

public class JsonPostgreSQLDialect extends PostgreSQL9Dialect {

	public JsonPostgreSQLDialect() {
		super();
		this.registerColumnType(Types.JAVA_OBJECT, "json");
	}

}

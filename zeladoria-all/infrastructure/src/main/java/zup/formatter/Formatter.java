package zup.formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Formatter {

	private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

	private SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public String dateToString(Date date) {

		if (date != null) {

			return DATE_FORMAT.format(date);
		}

		return null;
	}

	public Date stringToDate(String string) throws ParseException {

		return DATE_FORMAT.parse(string);
	}

	public String timestampToString(Date date) {

		if (date != null) {

			return TIMESTAMP_FORMAT.format(date);
		}

		return null;
	}

	public Date stringToTimestamp(String string) throws ParseException {

		return TIMESTAMP_FORMAT.parse(string);
	}

}

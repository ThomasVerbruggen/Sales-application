package janvdl.sales.gui.utils;

import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.time.LocalDate.parse;
import static java.time.format.DateTimeFormatter.ofPattern;

public class DateConverter extends StringConverter<LocalDate> {

	private DateTimeFormatter dateFormatter;

	public DateConverter(String pattern) {
		dateFormatter = ofPattern(pattern);
	}

	@Override
	public LocalDate fromString(String string) {
		return string == null || string.isEmpty() ? null : parse(string, dateFormatter);
	}

	@Override
	public String toString(LocalDate date) {
		return date == null ? "" : dateFormatter.format(date);
	}

}

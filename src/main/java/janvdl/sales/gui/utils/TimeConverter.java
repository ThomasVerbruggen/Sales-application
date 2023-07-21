package janvdl.sales.gui.utils;

import javafx.util.StringConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static java.time.LocalTime.parse;
import static java.time.format.DateTimeFormatter.ofPattern;

public class TimeConverter extends StringConverter<LocalTime> {

	private DateTimeFormatter timeFormatter;

	public TimeConverter(String pattern) {
		timeFormatter = ofPattern(pattern);
	}

	@Override
	public LocalTime fromString(String string) {
		return string == null || string.isEmpty() ? null : parse(string, timeFormatter);
	}

	@Override
	public String toString(LocalTime time) {
		return time == null ? "" : timeFormatter.format(time);
	}

}

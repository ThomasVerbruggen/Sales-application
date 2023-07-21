package janvdl.sales.report;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static java.time.Instant.ofEpochSecond;
import static java.time.ZoneId.systemDefault;

public final class ReportUtility {

	private ReportUtility() {

	}

	public static long periodKey(LocalDateTime date, long period) {

		long seconds = ZonedDateTime.of(date, systemDefault()).toEpochSecond();

		long periods = (seconds) / period;

		return periods * period;
	}

	public static ZonedDateTime toZonedDateTime(long periodKey) {
		return ZonedDateTime.ofInstant(ofEpochSecond(periodKey), systemDefault());
	}

}

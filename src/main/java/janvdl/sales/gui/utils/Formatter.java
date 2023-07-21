package janvdl.sales.gui.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

@Component
public class Formatter {

	@Value("${format.amount}")
	private String amountFormat;

	@Value("${format.quantity}")
	private String quantityFormat;

	@Value("${format.date}")
	private String dateFormat;

	private DecimalFormat amountFormatter;
	private DecimalFormat quantityFormatter;
	private DateTimeFormatter dateFormatter;

	public DecimalFormat getAmountFormatter() {
		if (amountFormatter == null)
			amountFormatter = new DecimalFormat(amountFormat);

		return amountFormatter;
	}

	public DecimalFormat getQuantityFormatter() {
		if (quantityFormatter == null)
			quantityFormatter = new DecimalFormat(quantityFormat);

		return quantityFormatter;
	}

	public DateTimeFormatter getDateFormatter() {
		if (dateFormatter == null)
			dateFormatter = DateTimeFormatter.ofPattern(dateFormat);

		return dateFormatter;
	}

}

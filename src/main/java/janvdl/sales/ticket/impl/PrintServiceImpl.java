package janvdl.sales.ticket.impl;

import janvdl.sales.domain.Sale;
import janvdl.sales.domain.SaleDetail;
import janvdl.sales.ticket.PrintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Service
@Slf4j
public class PrintServiceImpl implements PrintService {

	@Value("${printer.name}")
	private String printerName;

	@Override
	public void print(Sale sale, List<SaleDetail> details) throws IOException {
    log.info("Printing ticket for " + sale.getId());

    try (PrintWriter printWriter = new PrintWriter(printerName)) {
			printWriter.printf("%1$5d %2$10td-%2$tb-%2$tY %2$14tR%n%n%n", sale.getId(), sale.getDate());

			details.stream()
					.filter(d -> d.getQuantity() > 0)
					.sorted((o1, o2) -> o1.getDisplayOrder().compareTo(o2.getDisplayOrder()))
					.forEach(d -> printDetail(printWriter, d));

			printWriter.printf("%1$16s %2$23.2f%n%n%n", "Totaal", sale.getAmount());
			
			// Print the comment if it exists
            if (sale.getComment() != null && !sale.getComment().isEmpty()) {
                printWriter.println("    Naam: " + sale.getComment() + "\n\n");
            }

			printWriter.println("    Deze bon afgeven aan de helpers aub");

			for (int i = 0; i < 10; i++)
				printWriter.println();

		}

	}

	private static void printDetail(PrintWriter printWriter, SaleDetail detail) {

		printWriter.printf("%1$3.0f %2$-5s %3$-23s %4$6.2f%n%n", detail.getQuantity(), detail.getProduct().getName(),
				detail.getProduct().getDescription(), detail.getAmount());

	}

}

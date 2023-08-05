package janvdl.sales.ticket.impl;

import janvdl.sales.domain.Sale;
import janvdl.sales.domain.SaleDetail;
import janvdl.sales.ticket.PrintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

        try (OutputStream outputStream = new FileOutputStream(printerName)) {
            // Initialize the PrintWriter with the OutputStream
            PrintWriter printWriter = new PrintWriter(outputStream);

            // Send the font selection command to change to Font B
            // Assuming Font B is available on your printer
            byte[] fontSelectionCommand = new byte[]{27, 77, 0};
            outputStream.write(fontSelectionCommand);

            // Print the header
            printWriter.printf("%1$5d %2$10td-%2$tb-%2$tY %2$14tR%n%n%n", sale.getId(), sale.getDate());

            // Print sale details
            details.stream()
                    .filter(d -> d.getQuantity() > 0)
                    .sorted((o1, o2) -> o1.getDisplayOrder().compareTo(o2.getDisplayOrder()))
                    .forEach(d -> printDetail(printWriter, d));

            // Print total amount
            printWriter.printf("%1$16s %2$23.2f%n%n%n", "Totaal", sale.getAmount());

            // Print the comment if it exists
            if (sale.getComment() != null && !sale.getComment().isEmpty()) {
                printWriter.println("    Naam: " + sale.getComment() + "\n\n");
            }

            // Print footer
            printWriter.println("    Deze bon afgeven aan de helpers aub");

            for (int i = 0; i < 10; i++)
                printWriter.println();

            // Flush and close the PrintWriter
            printWriter.flush();
            printWriter.close();
        }
    }

    private static void printDetail(PrintWriter printWriter, SaleDetail detail) {
        printWriter.printf("%1$3.0f %2$-5s %3$-23s %4$6.2f%n%n", detail.getQuantity(), detail.getProduct().getName(),
                detail.getProduct().getDescription(), detail.getAmount());
    }
}

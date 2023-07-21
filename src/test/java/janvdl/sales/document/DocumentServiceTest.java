package janvdl.sales.document;

import janvdl.sales.domain.Product;
import janvdl.sales.report.ReportProductTotal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.awt.Desktop.getDesktop;
import static java.awt.Desktop.isDesktopSupported;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class DocumentServiceTest {

    @Autowired
    private DocumentService service;

    @Test
    void documentServiceTest() throws IOException {

        Product product1 = new Product(1L, "PR1", "Product 1", 18.0, true, 1);
        Product product2 = new Product(2L, "PR2", "Product 2", 25.0, true, 2);

        ReportProductTotal total1 = ReportProductTotal.builder()
                .product(product1)
                .quantity(5)
                .amount(90.0)
                .build();
        ReportProductTotal total2 = ReportProductTotal.builder()
                .product(product2)
                .quantity(12)
                .amount(250.0)
                .build();

        ReportProductTotal sum = ReportProductTotal.builder()
                .product(null)
                .quantity(total1.getQuantity() + total2.getQuantity())
                .amount(total1.getAmount() + total2.getAmount())
                .build();

        Path path = service.create("Test Document Service ", asList(total1, total2, sum));
        assertTrue(Files.exists(path));
        System.out.println("Generated file " + path);
        if (isDesktopSupported())
            getDesktop().open(path.toFile());

    }

}

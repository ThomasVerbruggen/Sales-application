package janvdl.sales.ticket;

import janvdl.sales.domain.Product;
import janvdl.sales.domain.Sale;
import janvdl.sales.domain.SaleDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static java.time.LocalDateTime.now;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@SpringBootTest
@ActiveProfiles("test")

class PrintServiceTest {

	@Autowired
	private PrintService service;

	@Test
	void printTest() throws IOException {

		Sale sale = new Sale();
		sale.setAmount(20.0D);
		sale.setId(1L);
		sale.setDate(now());

		Product product1 = new Product();
		product1.setActive(true);
		product1.setDescription("Product 1");
		product1.setDisplayOrder(1);
		product1.setId(1L);
		product1.setName("PR1");
		product1.setPrice(10.0D);

		Product product2 = new Product();
		product2.setActive(true);
		product2.setDescription("Product 2");
		product2.setDisplayOrder(2);
		product2.setId(2L);
		product2.setName("PR2");
		product2.setPrice(5.0D);

		SaleDetail detail1 = new SaleDetail();
		detail1.setAmount(10.0D);
		detail1.setId(1L);
		detail1.setProduct(product1);
		detail1.setQuantity(1.0D);

		SaleDetail detail2 = new SaleDetail();
		detail2.setAmount(10.0D);
		detail2.setId(12L);
		detail2.setProduct(product2);
		detail2.setQuantity(2.0D);

		assertDoesNotThrow(() -> service.print(sale, asList(detail1, detail2)));
	}
}

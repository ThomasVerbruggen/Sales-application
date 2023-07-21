package janvdl.sales.database.repository;

import janvdl.sales.domain.Product;
import janvdl.sales.domain.Sale;
import janvdl.sales.domain.SaleDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SaleDetailRepositoryTest {

	@Autowired
	private SaleDetailRepository saleDetailRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private SaleRepository saleRepository;

	private final Random random = new Random();

	private void createProducts() {

		for (int i = 1; i < 20; i++)
			productRepository
					.save(new Product(null, "Prod " + i, "Product " + i, 10.0 + i, i % 5 != 4, random.nextInt(200)));

	}

	@Test
	void saleTest() {

		createProducts();
		Long productId = productRepository.findAll().get(3).getId();

		Sale sale = new Sale();
		sale.setComment("Sale 1");

		Product product = productRepository.findById(productId).get();

		SaleDetail saleDetail = new SaleDetail(null, sale, product, 5.0, product.getPrice(), 5 * product.getPrice(),
				product.getDisplayOrder());

		sale.setAmount(saleDetail.getAmount());
		sale.setDate(now());

		saleRepository.save(sale);
		saleDetailRepository.save(saleDetail);

		List<SaleDetail> details = saleDetailRepository.findAll();
		assertEquals(1, details.size());
		assertEquals(product, details.get(0).getProduct());

	}

}

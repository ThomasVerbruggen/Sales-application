package janvdl.sales.database.repository;

import janvdl.sales.domain.Sale;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SaleRepositoryTest {

    @Autowired
    private SaleRepository repository;

    @Test
    void saleTest() {

        Sale sale = new Sale();
        sale.setComment("Sale 1");
        sale.setAmount(10.0);
        sale.setDate(now());

        Long firstId = repository.save(sale).getId();

        System.out.println("Sale " + sale);

        sale = new Sale();
        sale.setComment("Sale 2");
        sale.setAmount(20.0);
        sale.setDate(now());


        Long topId = repository.save(sale).getId();

        System.out.println("Sale " + sale);

        assertEquals(firstId, repository.findFirstByOrderById().getId(), "First not returned");
        assertEquals(topId, repository.findFirstByOrderByIdDesc().getId(), "Last not found ");

    }

}

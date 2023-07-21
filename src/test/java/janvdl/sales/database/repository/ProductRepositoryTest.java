package janvdl.sales.database.repository;

import janvdl.sales.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Test
    void productTest() {

        Product product = new Product();
        product.setName("Frit");
        product.setDescription("Een grote portie frit");
        product.setPrice(2.50);

        System.out.println("Product " + product);

        repository.save(product);

        assertEquals(1, repository.count(), "Repository count differs from 1");

        repository.findAll().forEach(System.out::println);

        Long id = product.getId();

        Product copy = repository.findById(id).get();

        assertEquals(product, copy, "Wrong product found " + copy);

        String newName = "Friet";
        product.setName(newName);

        repository.save(product);

        copy = repository.findById(id).get();

        assertEquals(newName, copy.getName(), "Wrong product found " + copy);

        repository.deleteById(id);

        assertEquals(0, repository.count(), "Repository is not empty after delete");


    }
}

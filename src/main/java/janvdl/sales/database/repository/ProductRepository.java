package janvdl.sales.database.repository;

import janvdl.sales.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findAllByOrderByDisplayOrderAsc();

	Optional<Product> findById(Long id);

}

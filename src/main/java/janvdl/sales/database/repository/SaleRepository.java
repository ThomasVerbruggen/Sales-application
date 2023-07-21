package janvdl.sales.database.repository;

import janvdl.sales.domain.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
public interface SaleRepository extends JpaRepository<Sale, Long> {

	Optional<Sale> findById(Long id);

	List<Sale> findByDateBetween(LocalDateTime from, LocalDateTime to);

	Sale findFirstByOrderById();

	Sale findFirstByOrderByIdDesc();
}

package janvdl.sales.database.repository;

import janvdl.sales.domain.Product;
import janvdl.sales.domain.Sale;
import janvdl.sales.domain.SaleDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SaleDetailRepository extends JpaRepository<SaleDetail, Long> {

	Optional<SaleDetail> findById(Long id);

	List<SaleDetail> findBySale(Sale sale);

	List<SaleDetail> findBySaleOrderByDisplayOrderAsc(Sale sale);

	List<SaleDetail> findByProduct(Product product);

}

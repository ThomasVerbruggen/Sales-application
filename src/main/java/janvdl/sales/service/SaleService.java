package janvdl.sales.service;

import janvdl.sales.domain.Sale;
import janvdl.sales.domain.SaleDetail;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface SaleService {

	void save(Sale sale);

	void save(Sale sale, List<SaleDetail> saleDetails);

	void delete(Sale sale);

	List<Sale> list();

	Sale findFirst();

	Sale findLast();

}

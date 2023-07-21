package janvdl.sales.service;

import janvdl.sales.domain.Sale;
import janvdl.sales.domain.SaleDetail;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface SaleDetailService {

	void save(SaleDetail saleDetails);

	void delete(SaleDetail saleDetail);

	List<SaleDetail> list(Sale sale);

}

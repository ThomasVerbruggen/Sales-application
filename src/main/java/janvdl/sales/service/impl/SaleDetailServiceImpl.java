package janvdl.sales.service.impl;

import janvdl.sales.database.repository.SaleDetailRepository;
import janvdl.sales.domain.Sale;
import janvdl.sales.domain.SaleDetail;
import janvdl.sales.service.SaleDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class SaleDetailServiceImpl implements SaleDetailService {

	@Autowired
	private SaleDetailRepository repository;

	@Override
	public void save(SaleDetail saleDetail) {
		repository.save(saleDetail);
	}

	@Override
	public void delete(SaleDetail saleDetail) {
		repository.delete(saleDetail);

	}

	@Override
	public List<SaleDetail> list(Sale sale) {
		return repository.findBySaleOrderByDisplayOrderAsc(sale);
	}

}

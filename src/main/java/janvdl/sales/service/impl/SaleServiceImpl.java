package janvdl.sales.service.impl;

import janvdl.sales.database.repository.SaleDetailRepository;
import janvdl.sales.database.repository.SaleRepository;
import janvdl.sales.domain.Sale;
import janvdl.sales.domain.SaleDetail;
import janvdl.sales.service.SaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static java.lang.Math.abs;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@Transactional
public class SaleServiceImpl implements SaleService {

	private static final double ZERO_THRESHOLD = 0.0001;

	@Autowired
	private SaleRepository saleRepository;

	@Autowired
	private SaleDetailRepository saleDetailRepository;

	@Override
	public void save(Sale sale) {
		saleRepository.save(sale);
	}

	private static boolean nonZeroQuantity(SaleDetail saleDetail) {

		return abs(saleDetail.getQuantity()) > ZERO_THRESHOLD;

	}

	private static SaleDetail clean(SaleDetail detail) {

		detail.setAmount(detail.getQuantity() * detail.getUnitPrice());

		return detail;
	}

	private static SaleDetail setSale(Sale sale, SaleDetail detail) {

		detail.setSale(sale);
		return detail;
	}

	@Override
	public void save(Sale sale, List<SaleDetail> saleDetails) {

		List<SaleDetail> cleanedDetails = saleDetails.stream()
				.filter(SaleServiceImpl::nonZeroQuantity)
				.map(SaleServiceImpl::clean)
				.map(d -> setSale(sale, d))
				.collect(toList());

		sale.setAmount(cleanedDetails.stream().mapToDouble(SaleDetail::getAmount).sum());

		log.trace("Sale = {}",sale);

		saleRepository.save(sale);

		cleanedDetails.forEach(d -> log.trace("Detail = {}",d));

		saleDetailRepository.saveAll(cleanedDetails);

	}

	@Override
	public void delete(Sale sale) {
		saleDetailRepository.deleteAll(saleDetailRepository.findBySale(sale));
		saleRepository.delete(sale);
	}

	@Override
	public List<Sale> list() {
		return saleRepository.findAll();
	}

	@Override
	public Sale findFirst() {
		return saleRepository.findFirstByOrderById();
	}

	@Override
	public Sale findLast() {
		return saleRepository.findFirstByOrderByIdDesc();
	}

}

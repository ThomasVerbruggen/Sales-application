package janvdl.sales.report;

import janvdl.sales.database.repository.SaleDetailRepository;
import janvdl.sales.database.repository.SaleRepository;
import janvdl.sales.domain.Sale;
import janvdl.sales.domain.SaleDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static janvdl.sales.report.ReportUtility.periodKey;
import static java.util.Objects.requireNonNull;

@Component
@Slf4j
public class Generator {

	@Autowired
	private SaleRepository saleRepository;

	@Autowired
	private SaleDetailRepository saleDetailRepository;

	private Map<ReportKey, ReportDetail> report = new HashMap<>();

	private long period;

	/**
	 *
	 * @param fromDate
	 * @param toDate
	 */
	public void create(long period, LocalDateTime fromDate, LocalDateTime toDate) {
		this.period = period;
		report.clear();

		saleRepository.findByDateBetween(fromDate, toDate).forEach(this::process);

	}

	private void process(Sale sale) {
		log.trace("Generating for sale {}",sale.getId());

		saleDetailRepository.findBySale(sale).forEach(d -> process(sale.getDate(), d));
	}

	private void process(LocalDateTime date, SaleDetail saleDetail) {
		log.trace("Generating for sale detail {}",saleDetail.getId());

		Long periodKey = periodKey(date, period);
		Long productId = saleDetail.getProduct().getId();

		add(new ReportKey(productId, periodKey), reportDetail(saleDetail));
		add(new ReportKey(null, periodKey), reportDetail(saleDetail));
		add(new ReportKey(productId, null), reportDetail(saleDetail));
		add(new ReportKey(null, null), reportDetail(saleDetail));

	}

	private void add(ReportKey key, ReportDetail detail) {
		ReportDetail existing = report.get(key);

		if (existing == null) {
			report.put(key, detail);
		} else
			existing.add(detail);
	}

	private static ReportDetail reportDetail(SaleDetail saleDetail) {
		return new ReportDetail(saleDetail.getAmount(), saleDetail.getQuantity());
	}

	public ReportDetail totalSum() {
		return get(null, null);
	}

	public ReportDetail totalProduct(Long productId) {
		return get(requireNonNull(productId, "Product must not be null"), null);
	}

	public ReportDetail totalPeriod(LocalDateTime date) {
		return get(null, requireNonNull(date, "Date must not be null"));
	}

	public ReportDetail totalProductPeriod(Long productId, LocalDateTime date) {
		return get(requireNonNull(productId, "Product must not be null"),
				requireNonNull(date, "Date must not be null"));
	}

	private ReportDetail get(Long productId, LocalDateTime date) {
		return report.get(new ReportKey(productId, date == null ? null : periodKey(date, period)));
	}

	public Stream<Entry<ReportKey, ReportDetail>> stream() {
		return report.entrySet().stream();
	}
}

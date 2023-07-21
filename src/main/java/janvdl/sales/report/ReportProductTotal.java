package janvdl.sales.report;

import janvdl.sales.domain.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportProductTotal {

	private Product product;

	private double amount;

	private double quantity;

}

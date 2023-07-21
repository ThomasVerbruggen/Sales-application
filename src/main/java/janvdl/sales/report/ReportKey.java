package janvdl.sales.report;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportKey {

	private Long product;

	private Long offset;

}

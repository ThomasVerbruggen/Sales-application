package janvdl.sales.report;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class ReportDetail {

	private double amount;

	private double quantity;

	public void add(ReportDetail detail) {
		setAmount(getAmount() + detail.getAmount());
		setQuantity(getQuantity() + detail.getQuantity());
	}

	public void add(double amount, double quantity) {
		setAmount(getAmount() + amount);
		setQuantity(getQuantity() + quantity);
	}

}

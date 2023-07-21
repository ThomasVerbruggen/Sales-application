package janvdl.sales.ticket;

import janvdl.sales.domain.Sale;
import janvdl.sales.domain.SaleDetail;

import java.io.IOException;
import java.util.List;

public interface PrintService {

	void print(Sale sale, List<SaleDetail> details) throws IOException;

}

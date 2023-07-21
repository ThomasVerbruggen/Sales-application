package janvdl.sales.document;

import janvdl.sales.report.ReportProductTotal;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface DocumentService {

	Path create(String title, List<ReportProductTotal> totals) throws IOException;
}

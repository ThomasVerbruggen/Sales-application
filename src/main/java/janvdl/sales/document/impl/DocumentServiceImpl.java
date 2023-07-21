package janvdl.sales.document.impl;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import janvdl.sales.document.DocumentService;
import janvdl.sales.gui.utils.Formatter;
import janvdl.sales.report.ReportProductTotal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;

import static com.itextpdf.io.font.FontConstants.COURIER;
import static com.itextpdf.io.font.FontConstants.COURIER_BOLD;
import static com.itextpdf.io.font.FontConstants.COURIER_OBLIQUE;
import static com.itextpdf.kernel.font.PdfFontFactory.createFont;
import static com.itextpdf.layout.property.TextAlignment.CENTER;
import static com.itextpdf.layout.property.TextAlignment.LEFT;
import static com.itextpdf.layout.property.TextAlignment.RIGHT;
import static com.itextpdf.layout.property.UnitValue.createPercentArray;
import static java.nio.file.Files.createTempFile;
import static java.nio.file.Files.newOutputStream;

@Service
@Slf4j
public class DocumentServiceImpl implements DocumentService {

	@Autowired
	private Formatter formatter;

	@Override
	public Path create(String title, List<ReportProductTotal> totals) throws IOException {

		Path path = createTempFile("report", ".pdf");

		log.info("Generating the file " + path);

		try (OutputStream out = new BufferedOutputStream(newOutputStream(path));
				Document document = new Document(
						new PdfDocument(new PdfWriter(out)))) {

			document.add(new Paragraph(title).setTextAlignment(CENTER));

			Table table = new Table(createPercentArray(new float[] { 50F, 25F, 25F }));
			table.setWidthPercent(70);

			table.addCell(createTitleCell("Artikel"));
			table.addCell(createTitleCell("Aantal"));
			table.addCell(createTitleCell("Bedrag"));

			totals.forEach(t -> add(table, t));

			document.add(table);
		}

		log.info("File generated!");
		return path;
	}

	private void add(Table table, ReportProductTotal total) {

		try {

			boolean isTotal = total.getProduct() == null;
			table.addCell(createTextCell(
					isTotal ? "Totaal" : (total.getProduct().getName() + ": " + total.getProduct().getDescription()),
					isTotal));

			table.addCell(createNumericCell(formatter.getQuantityFormatter().format(total.getQuantity()), isTotal));
			table.addCell(createNumericCell(formatter.getAmountFormatter().format(total.getAmount()), isTotal));
		} catch (IOException e) {
			log.error("Problem with cell generation: " + e.getMessage(), e);
		}
	}

	private static Cell createTextCell(String content, boolean total) throws IOException {
		return createCellImpl(content, LEFT, total);
	}

	private static Cell createNumericCell(String content, boolean total) throws IOException {
		return createCellImpl(content, RIGHT, total);
	}

	private static Cell createCellImpl(String content, TextAlignment textAlignment, boolean total) throws IOException {
		Paragraph paragraph = new Paragraph(content);
		paragraph.setTextAlignment(textAlignment);

		return new Cell()
				.add(paragraph)
				.setFont(createFont(total ? COURIER_OBLIQUE : COURIER))
				.setFontSize(total ? 11 : 9);

	}

	private static Cell createTitleCell(String content) throws IOException {
		Paragraph paragraph = new Paragraph(content);
		paragraph.setTextAlignment(CENTER);

		return new Cell()
				.add(paragraph)
				.setFont(createFont(COURIER_BOLD))
				.setFontSize(11);

	}

}

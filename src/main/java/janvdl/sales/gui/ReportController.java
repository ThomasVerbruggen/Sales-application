package janvdl.sales.gui;

import de.felixroske.jfxsupport.FXMLController;
import janvdl.sales.document.DocumentService;
import janvdl.sales.domain.Product;
import janvdl.sales.domain.Sale;
import janvdl.sales.gui.utils.DateConverter;
import janvdl.sales.gui.utils.TableCellFactory;
import janvdl.sales.gui.utils.TimeConverter;
import janvdl.sales.report.Generator;
import janvdl.sales.report.ReportDetail;
import janvdl.sales.report.ReportKey;
import janvdl.sales.report.ReportProductTotal;
import janvdl.sales.service.ProductService;
import janvdl.sales.service.SaleService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map.Entry;

import static janvdl.sales.report.ReportUtility.toZonedDateTime;
import static java.awt.Desktop.getDesktop;
import static java.lang.Integer.parseInt;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.util.stream.Collectors.toList;
import static javafx.scene.control.Alert.AlertType.ERROR;

@Slf4j
@FXMLController
public class ReportController {

	private static final DateTimeFormatter tickFormatter = DateTimeFormatter.ofPattern("dd-MM HH:mm");
	private static final String DATE_FORMAT = "dd-MM-YYYY";
	private static final String TIME_FORMAT = "HH:mm";

	@FXML
	private DatePicker fromDatePicker;

	@FXML
	private TextField fromTimeField;

	@FXML
	private DatePicker toDatePicker;

	@FXML
	private TextField toTimeField;

	@FXML
	private TextField textFieldLength;

	@FXML
	private Button pdfButton;

	@FXML
	private PieChart productPieChart;

	@FXML
	private PieChart periodPieChart;

	@FXML
	private BarChart<String, Double> salesProductAmountPerPeriodChart;

	@FXML
	private TableView<ReportProductTotal> totalsTable;

	@FXML
	private TableColumn<ReportProductTotal, Product> productColumn;

	@FXML
	private TableColumn<ReportProductTotal, Double> quantityColumn;

	@FXML
	private TableColumn<ReportProductTotal, Double> amountColumn;

	@Autowired
	private Generator generator;

	@Autowired
	private TableCellFactory tableCellFactory;

	private XYChart.Series<String, Double> seriesProductAmountByPeriod;
	private Long product = null;

	@Autowired
	private ProductService productService;

	@Autowired
	private SaleService saleService;

	@Autowired
	private DocumentService documentservice;

	private Path pdfReport;

	private TimeConverter timeConverter = new TimeConverter(TIME_FORMAT);

	@FXML
	public void initialize() {

		productColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
		productColumn.setCellFactory(c -> tableCellFactory.productCell());

		amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
		amountColumn.setCellFactory(c -> tableCellFactory.amountCell());

		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		quantityColumn.setCellFactory(c -> tableCellFactory.quantityCell());

		DateConverter dateConverter = new DateConverter(DATE_FORMAT);
		fromDatePicker.setConverter(dateConverter);
		toDatePicker.setConverter(dateConverter);

		fromTimeField.setTextFormatter(new TextFormatter<>(timeConverter));
		toTimeField.setTextFormatter(new TextFormatter<>(timeConverter));

		textFieldLength.setText("30");

		initializeDates();

		pdfButton.setDisable(true);

		// too crazy to be true, but it works to open the desktop
		System.setProperty("java.awt.headless", "false");

	}

	private void initializeDates() {
		Sale first = saleService.findFirst();

		if (first == null)
			return;

		fromDatePicker.setValue(first.getDate().toLocalDate());
		fromTimeField.setText(timeConverter.toString(first.getDate().toLocalTime().truncatedTo(HOURS)));

		Sale last = saleService.findLast();
		toDatePicker.setValue(last.getDate().toLocalDate());
		toTimeField.setText(timeConverter.toString(last.getDate().toLocalTime().truncatedTo(HOURS).plusHours(1)));
	}

	@FXML
	void go(ActionEvent event) {
		LocalDateTime fromDate = LocalDateTime.of(fromDatePicker.getValue(),
				timeConverter.fromString(fromTimeField.getText()));
		LocalDateTime toDate = LocalDateTime.of(toDatePicker.getValue(),
				timeConverter.fromString(toTimeField.getText()));

		if (fromDate.isAfter(toDate)) {
			new Alert(ERROR, "Einde van de periode komt voor begin").showAndWait();
			return;
		}

		int periodLength = parseInt(textFieldLength.getText());

		if (periodLength < 1) {
			new Alert(ERROR, "Lengte van periode moet positief zijn").showAndWait();
			return;
		}

		populate(fromDate, toDate, periodLength);

		createPdfReport();

	}

	private void createPdfReport() {
		String title = "Verkoop van " + valueAsString(fromDatePicker, fromTimeField) + " tot "
				+ valueAsString(toDatePicker, toTimeField);
		try {
			pdfButton.setDisable(true);
			pdfReport = documentservice.create(title, totalsTable.getItems());
			pdfButton.setDisable(false);
		} catch (IOException e) {
			log.error("Problemn generating the pdf report: " + e.getMessage(), e);
		}
	}

	private String valueAsString(DatePicker datePicker, TextField time) {
		return datePicker.getConverter().toString(datePicker.getValue()) + "-" + time.getText();
	}

	@FXML
	void pdf(ActionEvent event) {
		try {
			getDesktop().open(pdfReport.toFile());
		} catch (IOException e) {
			log.error("Problem opening the pdf report: " + e.getMessage(), e);
		}
	}

	private void populate(LocalDateTime fromDate, LocalDateTime toDate, int periodLength) {

		generator.create(periodLength * 60, fromDate, toDate);

		if (log.isDebugEnabled())
			generator.stream().forEach(ReportController::logEntry);

		// fill product pie chart

		productPieChart.getData().clear();

		productPieChart.getData().addAll(generator.stream()
				.filter(ReportController::isProductTotal)
				.map(this::productAmount)
				.collect(toList()));

		// fill period pie chart
		periodPieChart.getData().clear();
		periodPieChart.getData().addAll(generator.stream()
				.filter(ReportController::isPeriodTotal)
				.map(this::periodAmount)
				.collect(toList()));

		// fill bar char

		salesProductAmountPerPeriodChart.getData().clear();

		generator.stream()
				.filter(ReportController::isProductPeriodTotal)
				.sorted(ReportController::orderByProduct)
				.forEach(this::amountByTime);

		totalsTable.getItems().clear();

		// fill table

		totalsTable.getItems().addAll(generator.stream()
				.filter(ReportController::isProductTotal)
				.sorted(ReportController::orderByProduct)
				.map(this::productTotal)
				.collect(toList()));

		addTotalToTable();

	}

	private static void logEntry(Entry<ReportKey, ReportDetail> entry) {
		log.debug("Period "
				+ (entry.getKey().getOffset() == null ? "NA" : format(toZonedDateTime(entry.getKey().getOffset())))
				+ " product = " + entry.getKey().getProduct()
				+ " amount = " + entry.getValue().getAmount() + " quantity = " + entry.getValue().getQuantity());
	}

	private static boolean isProductTotal(Entry<ReportKey, ReportDetail> entry) {
		return entry.getKey().getProduct() != null && entry.getKey().getOffset() == null;
	}

	private static boolean isPeriodTotal(Entry<ReportKey, ReportDetail> entry) {
		return entry.getKey().getProduct() == null && entry.getKey().getOffset() != null;
	}

	private static boolean isProductPeriodTotal(Entry<ReportKey, ReportDetail> entry) {
		return entry.getKey().getProduct() != null && entry.getKey().getOffset() != null;
	}

	private void amountByTime(Entry<ReportKey, ReportDetail> entry) {

		if (!entry.getKey().getProduct().equals(product)) {
			product = entry.getKey().getProduct();

			seriesProductAmountByPeriod = new XYChart.Series<>();
			seriesProductAmountByPeriod.setName(productService.get(product).get().getName());
			salesProductAmountPerPeriodChart.getData().add(seriesProductAmountByPeriod);
		}
		seriesProductAmountByPeriod.getData()
				.add(new XYChart.Data<>(format(toZonedDateTime(entry.getKey().getOffset())),
						entry.getValue().getAmount()));
	}

	private PieChart.Data productAmount(Entry<ReportKey, ReportDetail> entry) {

		Product product = productService.get(entry.getKey().getProduct()).get();

		return new PieChart.Data(product.getName(), entry.getValue().getAmount());

	}

	private PieChart.Data periodAmount(Entry<ReportKey, ReportDetail> entry) {

		String period = tickFormatter.format(toZonedDateTime(entry.getKey().getOffset()));

		return new PieChart.Data(period, entry.getValue().getAmount());
	}

	private ReportProductTotal productTotal(Entry<ReportKey, ReportDetail> entry) {

		Product product = productService.get(entry.getKey().getProduct()).get();

		return ReportProductTotal.builder()
				.product(product)
				.amount(entry.getValue().getAmount())
				.quantity(entry.getValue().getQuantity())
				.build();

	}

	private void addTotalToTable() {

		ReportDetail total = generator.totalSum();

		ReportProductTotal grandTotal = ReportProductTotal.builder()
				.product(null)
				.amount(total.getAmount())
				.quantity(total.getQuantity())
				.build();

		totalsTable.getItems().add(grandTotal);
	}

	private static int orderByProduct(Entry<ReportKey, ReportDetail> e1, Entry<ReportKey, ReportDetail> e2) {

		ReportKey k1 = e1.getKey();
		ReportKey k2 = e2.getKey();

		if (k1.getProduct() < k2.getProduct())
			return -1;

		if (k1.getProduct() > k2.getProduct())
			return 1;

		if (k1.getOffset() < k2.getOffset())
			return -1;

		if (k1.getOffset() > k2.getOffset())
			return 1;

		return 0;
	}

	private static String format(ZonedDateTime time) {
		return tickFormatter.format(time);
	}

}

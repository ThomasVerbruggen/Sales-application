package janvdl.sales.gui;

import de.felixroske.jfxsupport.FXMLController;
import janvdl.sales.domain.Product;
import janvdl.sales.domain.Sale;
import janvdl.sales.domain.SaleDetail;
import janvdl.sales.gui.utils.TableCellFactory;
import janvdl.sales.service.ProductService;
import janvdl.sales.service.SaleDetailService;
import janvdl.sales.service.SaleService;
import janvdl.sales.ticket.PrintService;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static janvdl.sales.gui.DialogController.showViewAsDialog;
import static java.util.stream.Collectors.toList;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.scene.control.TableColumn.SortType.DESCENDING;

@Slf4j
@FXMLController
public class SaleListController {

	@Autowired
	private ConfigurableApplicationContext applicationContext;

	@Autowired
	private TableCellFactory tableCellFactory;

	@FXML
	private TableView<Sale> saleTable;

	@FXML
	private TableColumn<Sale, Long> saleIdColumn;

	@FXML
	private TableColumn<Sale, Double> saleAmountColumn;

	@FXML
	private TableColumn<Sale, LocalDateTime> dateColumn;

	@FXML
	private TableColumn<Sale, String> commentColumn;

	@FXML
	private TableView<SaleDetail> saleDetailTable;

	@FXML
	private TableColumn<SaleDetail, Product> productColumn;

	@FXML
	private TableColumn<SaleDetail, Double> quantityColumn;

	@FXML
	private TableColumn<SaleDetail, Double> unitPriceColumn;

	@FXML
	private TableColumn<SaleDetail, Double> saleDetailAmountColumn;

	@Autowired
	private SaleService saleService;

	@Autowired
	private SaleDetailService saleDetailService;

	@Autowired
	private ProductService productService;

	@Autowired
	private PrintService printService;

	@FXML
	public void initialize() {

		// sale table
		// by default sorted with newest sales on top of the list

		saleIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		saleIdColumn.setSortType(DESCENDING);

		saleAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
		saleAmountColumn.setCellFactory(c -> tableCellFactory.amountCell());

		commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		dateColumn.setCellFactory(c -> tableCellFactory.dateCell());

		saleTable.setItems(observableArrayList(saleService.list()));
		saleTable.getSelectionModel().selectedItemProperty().addListener((s, o, n) -> selectedSale(n));
		saleTable.getSelectionModel().selectLast();
		saleTable.getSortOrder().add(saleIdColumn);

		// sale detail table

		productColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
		productColumn.setCellFactory(c -> tableCellFactory.productCell());

		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		quantityColumn.setCellFactory(c -> tableCellFactory.quantityCell());

		unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
		unitPriceColumn.setCellFactory(c -> tableCellFactory.amountCell());

		saleDetailAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
		saleDetailAmountColumn.setCellFactory(c -> tableCellFactory.amountCell());

	}

	private void selectedSale(Sale selected) {

		if (selected == null)
			saleDetailTable.setItems(null);
		else {
			saleDetailTable.setItems(observableArrayList(saleDetailService.list(selected)));
		}
	}

	private List<SaleDetail> createSaleDetails() {

		return productService.list().stream()
				.filter(Product::getActive)
				.map(SaleListController::saleDetail)
				.collect(toList());

	}

	private static SaleDetail saleDetail(Product product) {
		return new SaleDetail(null, null, product, 0.0, product.getPrice(), 0.0, product.getDisplayOrder());
	}

	public void insert(Event event) {

		log.trace("Insert button !");

		SaleController controller = (SaleController) showViewAsDialog(applicationContext.getBean(SaleView.class));

		Sale sale = new Sale();
		List<SaleDetail> saleDetails = createSaleDetails();
		controller.setSale(sale, saleDetails);

		controller.showAndWait();

		if (controller.isOkClicked()) {
			sale = controller.getSale();
			sale.setDate(LocalDateTime.now());
			List<SaleDetail> details = controller.getSaleDetails();

			log.trace("Sale {}",sale);
			details.forEach(d -> log.trace("Detail {}",d));

			saleService.save(sale, details);
			// keep the newest sales at the top of the list
			saleTable.getItems().add(0, sale);
			saleTable.refresh();

			try {
				printService.print(sale, details);
			} catch (IOException e) {
				log.error("Problem printing the ticket " + e.getMessage(), e);
			}
		}
	}

	public void print(Event event) {
		Sale sale = saleTable.getSelectionModel().getSelectedItem();
		if (sale == null)
			return;

		try {
			printService.print(sale, saleDetailTable.getItems());
		} catch (IOException e) {
			log.error("Problem printing ticket for sale " + sale.getId() + ": " + e.getMessage(), e);
		}

	}
}

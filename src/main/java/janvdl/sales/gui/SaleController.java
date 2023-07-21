package janvdl.sales.gui;

import de.felixroske.jfxsupport.FXMLController;
import janvdl.sales.domain.Product;
import janvdl.sales.domain.Sale;
import janvdl.sales.domain.SaleDetail;
import janvdl.sales.gui.utils.Formatter;
import janvdl.sales.gui.utils.TableCellFactory;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.lang.Math.max;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static javafx.collections.FXCollections.observableArrayList;

@Slf4j
@FXMLController
public class SaleController extends DialogController {

	@Autowired
	private Formatter formatter;

	@Autowired
	private TableCellFactory tableCellFactory;

	private Sale sale;
	private List<SaleDetail> saleDetails;

	@FXML
	private Label totalAmount;
	private double amountToPay;

	@FXML
	private Label quantity;

	@FXML
	private TextField comment;

	@FXML
	private TableView<SaleDetail> table;

	@FXML
	private TableColumn<SaleDetail, Double> quantityColumn;

	@FXML
	private TableColumn<SaleDetail, Product> productColumn;

	@FXML
	private TableColumn<SaleDetail, Double> unitPriceColumn;

	@FXML
	private TableColumn<SaleDetail, Double> amountColumn;

	@FXML
	private Button button0;

	@FXML
	private Button button1;

	@FXML
	private Button button2;

	@FXML
	private Button button3;

	@FXML
	private Button button4;

	@FXML
	private Button button5;

	@FXML
	private Button button6;

	@FXML
	private Button button7;

	@FXML
	private Button button8;

	@FXML
	private Button button9;

	@FXML
	private Button button10;

	@FXML
	private Button button15;

	@FXML
	private Button button20;

	@FXML
	private Button buttonMinus;

	@FXML
	private Button buttonPlus;

	private List<Button> quantityButtons;

	@FXML
	private TextField receivedAmount;

	@FXML
	private Label returnAmount;
	private DoubleStringConverter amountConverter;

	@FXML
	private TextField customerNameField;

	@FXML
	public void initialize() {

		productColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
		productColumn.setCellFactory(c -> tableCellFactory.productCell());

		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		quantityColumn.setOnEditCommit(this::quantityChanged);

		unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
		unitPriceColumn.setCellFactory(c -> tableCellFactory.amountCell());

		amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
		amountColumn.setCellFactory(c -> tableCellFactory.amountCell());

		totalAmount.setText(formatAmount(0.0));
		quantity.setText(formatQuantity(0.0));

		quantityButtons = asList(button0, button1, button2, button3, button4, button5, button6, button7,
				button8, button9, button10);

		amountConverter = new DoubleStringConverter();

		receivedAmount.setTextFormatter(new TextFormatter<>(amountConverter));
		receivedAmount.textProperty().addListener((c, o, n) -> calculateReturnAmount());
	}

	private String formatAmount(double amount) {
		return formatter.getAmountFormatter().format(amount);
	}

	private String formatQuantity(double quantity) {
		return formatter.getQuantityFormatter().format(quantity);
	}

	private void calculateTotal() {
		amountToPay = table.getItems().stream()
				.mapToDouble(SaleDetail::getAmount)
				.sum();

		totalAmount.setText(formatAmount(amountToPay));

		Double sum = table.getItems().stream()
				.mapToDouble(SaleDetail::getQuantity)
				.sum();

		sale.setAmount(sum);
		quantity.setText(formatQuantity(sum));
	}

	private void resync() {
		table.refresh();
		calculateTotal();
		calculateReturnAmount();
	}

	private void changeQuantity(SaleDetail detail, Double quantity) {
		detail.setQuantity(quantity);
		detail.setAmount(detail.getUnitPrice() * quantity);
		resync();

	}

	@FXML
	public void quantityChanged(CellEditEvent<SaleDetail, Double> event) {
		SaleDetail detail = event.getRowValue();
		log.info("Changing " + detail + " value: " + event.getOldValue() + " -> " + event.getNewValue());
		changeQuantity(detail, event.getNewValue());

	}

	@FXML
	public void handleQuantityButton(Event event) {
		int value = quantityButtons.indexOf(event.getSource());
		SaleDetail detail = table.getSelectionModel().getSelectedItem();

		if (detail == null)
			return;

		if (event.getSource() == button15)
			value = 15;
		else if (event.getSource() == button20)
			value = 20;

		if (value >= 0) {
			changeQuantity(detail, (double) value);
		} else {
			boolean plus = event.getSource() == buttonPlus;
			changeQuantity(detail, max(0, detail.getQuantity() + (plus ? 1 : -1)));
		}
	}

	public void setSale(Sale sale, List<SaleDetail> saleDetails) {
		this.sale = sale;
		this.saleDetails = saleDetails;
	
		table.setItems(observableArrayList(saleDetails));
		table.getSelectionModel().selectFirst();
	}
	

	public Sale getSale() {
		// Set the customer name from the TextField
		sale.setComment(comment.getText());
		return sale;
	}
	

	public List<SaleDetail> getSaleDetails() {
		return unmodifiableList(table.getItems());
	}

	@FXML
	public void handleFree(Event event) {

		Product product = table.getSelectionModel().getSelectedItem().getProduct();
		SaleDetail detail = new SaleDetail(null, sale, product, 1.0,
				0.0, 0.0, product.getDisplayOrder());
		saleDetails.add(detail);
		table.getItems().add(detail);

		resync();
	}

	@FXML
	public void handleUp(Event event) {

		table.getSelectionModel().selectPrevious();

	}

	public void handleDown(Event event) {
		table.getSelectionModel().selectNext();
	}

	@FXML
	@Override
	public void handleOk(Event event) {
		super.handleOk(event);
	}

	private void calculateReturnAmount() {

		String string = receivedAmount.getText();

		if (string == null || string.isEmpty())
			return;

		double received = amountConverter.fromString(string);
		double toReturn = received - amountToPay;
		returnAmount.setText(toReturn >= 0.0 ? formatAmount(toReturn) : "");

	}
}

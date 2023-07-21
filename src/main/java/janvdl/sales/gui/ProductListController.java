package janvdl.sales.gui;

import de.felixroske.jfxsupport.FXMLController;
import janvdl.sales.domain.Product;
import janvdl.sales.gui.utils.TableCellFactory;
import janvdl.sales.service.ProductService;
import javafx.beans.binding.BooleanBinding;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Optional;

import static janvdl.sales.gui.DialogController.showViewAsDialog;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.scene.control.Alert.AlertType.CONFIRMATION;
import static javafx.scene.control.ButtonType.OK;

@FXMLController
public class ProductListController {

	@Autowired
	private ConfigurableApplicationContext applicationContext;

	@Autowired
	private ProductService service;

	@Autowired
	private TableCellFactory tableCellFactory;

	@FXML
	private TableView<Product> tableView;

	@FXML
	private TableColumn<Product, String> nameColumn;

	@FXML
	private TableColumn<Product, String> descriptionColumn;

	@FXML
	private TableColumn<Product, Double> priceColumn;

	@FXML
	private TableColumn<Product, Integer> orderColumn;

	@FXML
	private TableColumn<Product, Boolean> activeColumn;

	@FXML
	private Button insertButton;

	@FXML
	private Button updateButton;

	@FXML
	private Button deleteButton;

	@FXML
	private void initialize() {
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

		priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
		priceColumn.setCellFactory(c -> tableCellFactory.amountCell());

		orderColumn.setCellValueFactory(new PropertyValueFactory<>("displayOrder"));

		activeColumn.setCellValueFactory(new PropertyValueFactory<>("active"));
		activeColumn.setCellFactory(c -> tableCellFactory.booleanCell());

		tableView.setItems(observableArrayList(service.list()));

		tableView.getSelectionModel().selectFirst();

		BooleanBinding emptySelection = tableView.getSelectionModel().selectedItemProperty().isNull();

		updateButton.disableProperty().bind(emptySelection);
		deleteButton.disableProperty().bind(emptySelection);
	}

	private Product getSelectedProduct() {
		return tableView.getSelectionModel().getSelectedItem();
	}

	private String format(Product product) {
		return product.getName() + " - " + product.getDescription();
	}

	@FXML
	private void insert(Event event) {

		Product newProduct = new Product();

		if (showProductView(newProduct)) {
			service.save(newProduct);
			tableView.getItems().add(newProduct);
		}
	}

	@FXML
	private void update(Event event) {

		Product product = getSelectedProduct();

		if (showProductView(product)) {
			service.save(product);
			tableView.refresh();
		}

	}

	@FXML
	private void delete(Event event) {

		Product product = getSelectedProduct();

		Alert alert = new Alert(CONFIRMATION);
		alert.getDialogPane().getStylesheets().add(getClass().getResource("sales.css").toExternalForm());
		alert.setTitle("Wissen"); // TODO translate
		alert.setContentText("Artikel wissen"); // TODO translate
		alert.setContentText(format(product) + " wissen ?"); // TODO translate

		Optional<ButtonType> confirmation = alert.showAndWait();

		if (confirmation.isPresent()
				&& confirmation.get() == OK) {
			service.delete(product);
			tableView.getItems().remove(product);
		}

	}

	private boolean showProductView(Product product) {

		ProductController controller = (ProductController) showViewAsDialog(
				applicationContext.getBean(ProductView.class));
		controller.setProduct(product);

		controller.showAndWait();

		return controller.isOkClicked();

	}
}

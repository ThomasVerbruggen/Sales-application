package janvdl.sales.gui;

import de.felixroske.jfxsupport.FXMLController;
import janvdl.sales.domain.Product;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.DoubleStringConverter;

@FXMLController
public class ProductController extends DialogController {

	@FXML
	private TextField name;

	@FXML
	private TextField description;

	@FXML
	private TextField price;
	@FXML
	private TextField order;

	@FXML
	private CheckBox active;

	@FXML
	private Button okButton;

	private Product product;

	@FXML
	public void initialize() {

		price.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));

	}

	public void setProduct(Product product) {
		this.product = product;

		name.setText(product.getName());
		description.setText(product.getDescription());
		price.setText(product.getPrice().toString()); // TODO kan dit niet met die converter ?
		order.setText(product.getDisplayOrder().toString()); // TODO kan dit niet met die converter ?
		active.setSelected(product.getActive());

	}

	@Override
	@FXML
	public void handleOk(Event event) {

		product.setName(name.getText());
		product.setDescription(description.getText());
		product.setPrice(Double.parseDouble(price.getText())); // TODO kan dit niet met die converter ?
		product.setDisplayOrder(Integer.parseInt(order.getText())); // TODO kan dit niet met die converter ?
		product.setActive(active.isSelected());

		super.handleOk(event);

	}

}

package janvdl.sales.gui.utils;

import janvdl.sales.domain.Product;
import javafx.scene.control.TableCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static javafx.geometry.Pos.CENTER;
import static javafx.geometry.Pos.CENTER_RIGHT;

@Component
public class TableCellFactory {

	@Autowired
	private Formatter formatter;

	public <T> TableCell<T, Boolean> booleanCell() {

		return new TableCell<T, Boolean>() {

			@Override
			protected void updateItem(Boolean item, boolean empty) {

				String text = "";
				if (item != null && !empty)
					text = item ? "\u2714" : "\u2718";
				setText(text);
				setAlignment(CENTER);
			}

		};

	}

	public <T> TableCell<T, Product> productCell() {

		return new TableCell<T, Product>() {

			@Override
			protected void updateItem(Product item, boolean empty) {

				String text = "";

				if (!empty && item != null)
					text = item.getName() + ": " + item.getDescription();

				setText(text);
			}

		};
	}

	public <T> TableCell<T, Double> amountCell() {

		return new TableCell<T, Double>() {

			@Override
			protected void updateItem(Double item, boolean empty) {

				String text = "";

				if (!empty && item != null) {
					text = formatter.getAmountFormatter().format(item);
				}

				setText(text);
				setAlignment(CENTER_RIGHT);
			}

		};
	}

	public <T> TableCell<T, Double> quantityCell() {
		// make more generic !

		return new TableCell<T, Double>() {

			@Override
			protected void updateItem(Double item, boolean empty) {

				String text = "";

				if (!empty && item != null) {
					text = formatter.getQuantityFormatter().format(item);
				}

				setText(text);
				setAlignment(CENTER_RIGHT);
			}

		};
	}

	public <T> TableCell<T, LocalDateTime> dateCell() {

		return new TableCell<T, LocalDateTime>() {

			@Override
			protected void updateItem(LocalDateTime item, boolean empty) {

				String text = "";

				if (!empty && item != null) {
					text = formatter.getDateFormatter().format(item);
				}

				setText(text);
				setAlignment(CENTER);

			}

		};
	}

}

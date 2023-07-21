package janvdl.sales.gui;

import de.felixroske.jfxsupport.AbstractFxmlView;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.stage.Modality.APPLICATION_MODAL;

public class DialogController {

	private Stage dialogStage;
	private boolean okClicked;

	public boolean isOkClicked() {
		return okClicked;
	}

	private void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public final Stage getDialogStage() {
		return dialogStage;
	}

	private void closeDialog() {
		getDialogStage().close();
	}

	protected void handleOk(Event event) {
		okClicked = true;
		closeDialog();
	}

	@FXML
	public void handleCancel(Event event) {
		closeDialog();
	}

	public void showAndWait() {
		getDialogStage().showAndWait();
	}

	public static DialogController showViewAsDialog(AbstractFxmlView view) {
		Stage dialogStage = new Stage();
		dialogStage.setScene(new Scene(view.getView()));
		dialogStage.initModality(APPLICATION_MODAL);

		DialogController controller = (DialogController) view.getPresenter();
		controller.setDialogStage(dialogStage);

		return controller;

	}
}

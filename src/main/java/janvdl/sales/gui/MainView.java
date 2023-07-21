package janvdl.sales.gui;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import javafx.scene.Parent;
import javafx.scene.control.TabPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

@FXMLView(bundle = "janvdl.sales.gui.main", css = "/janvdl/sales/gui/sales.css")
public class MainView extends AbstractFxmlView {

	@Autowired
	private ConfigurableApplicationContext applicationContext;

	@Override
	public Parent getView() {
		TabPane view = (TabPane) super.getView();

		view.getTabs().get(0).setContent(applicationContext.getBean(ProductListView.class).getView());
		view.getTabs().get(1).setContent(applicationContext.getBean(SaleListView.class).getView());
		view.getTabs().get(2).setContent(applicationContext.getBean(ReportView.class).getView());

		return view;
	}

}

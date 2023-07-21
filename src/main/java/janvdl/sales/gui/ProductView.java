package janvdl.sales.gui;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import org.springframework.context.annotation.Scope;

@FXMLView(bundle = "janvdl.sales.gui.product", css = "/janvdl/sales/gui/sales.css")
@Scope("prototype")
public final class ProductView extends AbstractFxmlView {

}

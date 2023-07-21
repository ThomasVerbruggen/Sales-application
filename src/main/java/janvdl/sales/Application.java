package janvdl.sales;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.SplashScreen;
import janvdl.sales.gui.MainView;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@SpringBootApplication
@EntityScan(basePackageClasses = { Jsr310JpaConverters.class }, basePackages = { "janvdl.sales.domain" })
public class Application extends AbstractJavaFxApplicationSupport {

	public static void main(String[] args) {
		launch(Application.class, MainView.class, new SplashScreen(),args);
	}

}

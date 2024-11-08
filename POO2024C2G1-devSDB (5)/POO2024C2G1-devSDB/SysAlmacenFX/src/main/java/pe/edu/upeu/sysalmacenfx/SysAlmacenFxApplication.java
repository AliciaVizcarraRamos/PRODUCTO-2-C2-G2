package pe.edu.upeu.sysalmacenfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import pe.edu.upeu.sysalmacenfx.pruebas.serviceCAT;
import pe.edu.upeu.sysalmacenfx.pruebas.MainX;

//ALICIA VIZCARRA RAMOS-C2-G2
@SpringBootApplication
public class SysAlmacenFxApplication extends Application {

	private static ConfigurableApplicationContext exteconfigurableApplicationContext;
	private Parent parent;



	public static void main(String[] args) {

		//SpringApplication.run(SysAlmacenFxApplication.class, args);
		launch(args);
	}

	//@Bean
	//public CommandLineRunner run(ApplicationContext context) { return args -> {
		//MainX mx = context.getBean(MainX.class);
		//mx.menu();
		//serviceCAT mx =context.getBean(serviceCAT.class);
		//mx.menu();
		//};
	//}


	@Override
	public void init() throws Exception {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(SysAlmacenFxApplication.class);
		builder.application().setWebApplicationType(WebApplicationType.NONE);
		exteconfigurableApplicationContext = builder.run(getParameters().getRaw().toArray(new String[0]));
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
		fxmlLoader.setControllerFactory(exteconfigurableApplicationContext::getBean);
		parent= fxmlLoader.load();

	}
	@Override
	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(parent);
		scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("SysAlmacen Spring Java-FX");
		stage.setResizable(false);
		stage.show();
	}
	@Override
	public void stop() throws Exception {
		exteconfigurableApplicationContext.close();
	}
}

package application;
	
import java.io.PrintWriter;
import java.io.StringWriter;

import httpRequestHandler.RequestHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;


public class Main extends Application {	

	public static FXMLLoader loader;
	
	public static void main(String[] args) {
		launch(args);		
	}	
	
	public void start(Stage primaryStage) {
		try {	
			
			//Check if the Wi-5 controller is up
			Boolean isAlive = RequestHandler.pingURL(RequestHandler.URL + "/odin/params/json", 200);
			
			if(!isAlive) {
				AlertServerNotUp();
			}
			loader= new FXMLLoader(getClass().getResource("/view/Main.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
		    System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");			
			
			primaryStage.setTitle("Odin - Wi5 GUI");			
		
			primaryStage.getIcons().add(new Image("file:resources/icon.jpg"));
			primaryStage.setScene(scene);
			primaryStage.show();	
			
		} catch(Exception e) {
			ExceptionAlert(e);
			e.printStackTrace();
		}
	}
	
	private void ExceptionAlert(Exception e) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Exception Dialog");
		
		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("The exception stacktrace was:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}
	
	private void AlertServerNotUp() {
		Alert alert = new Alert(AlertType.INFORMATION);
		
		alert.setTitle("Wi-5 controller not up");
		alert.setHeaderText(null);
		alert.setContentText(	"Cannot ping the Wi-5 controller with the URL "+ 
								RequestHandler.URL +
								"/odin/params/json" +
								". Please check if it is compatible and started. Finishing program");
		alert.showAndWait();
		
		if (alert.getResult() == ButtonType.OK) {
			System.out.println("Finishing program");
		    System.exit(0);
		}
	}
}

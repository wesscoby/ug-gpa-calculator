package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("designer.fxml"));
        stage.setTitle("GPA Calculator Application");
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("../images/icon.png")));
//        stage.setMaximized(true);
        stage.setScene(new Scene(root));

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

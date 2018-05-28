package Controller;

import Model.GestorDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerIniciarSesion implements Initializable {

    @FXML
    public TextField user;
    @FXML
    public PasswordField pass;
    @FXML
    public Button iniciar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        iniciar.setOnAction(event -> {
            GestorDB.gestor = new GestorDB("jdbc:postgresql://localhost:5433/Progra2", user.getText(), pass.getText());
            if (GestorDB.gestor != null){
                try {
                    FXMLLoader loader = new FXMLLoader();
                    Parent root = loader.load(getClass().getResource("../View/mainView.fxml").openStream());
                    Stage escenario = new Stage();
                    escenario.setTitle("");
                    escenario.setScene(new Scene(root, 600, 557));
                    escenario.show();
                    Stage actual = (Stage) iniciar.getScene().getWindow();
                    actual.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

    }
}

package sample.Controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ErrorWindow {
    public Button errorAcceptBtn;

    public void close(ActionEvent actionEvent) {
        Stage stage = (Stage) errorAcceptBtn.getScene().getWindow();
        stage.close();
    }
}

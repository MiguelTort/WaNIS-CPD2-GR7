package sample.Controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LogInFailController {
    public Button OKBtn;

    public void close(ActionEvent actionEvent) {
        Stage stage = (Stage) OKBtn.getScene().getWindow();
        stage.close();
    }
}

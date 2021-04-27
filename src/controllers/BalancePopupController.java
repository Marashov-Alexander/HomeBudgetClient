package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Balance;
import utils.Utils;

import java.util.function.Consumer;

public class BalancePopupController {

    @FXML private ComboBox<String> monthCB;
    @FXML private TextField yearTB;

    @FXML
    private Button saveBtn;

    private Parent content;
    private Scene scene = null;

    public void setContent(final Parent content) {
        this.content = content;
    }

    public void show(final String title, final Consumer<Balance> onSave) {
        final Stage myDialog = new Stage();
        myDialog.initModality(Modality.APPLICATION_MODAL);
        myDialog.setTitle(title);

        monthCB.setItems(FXCollections.observableArrayList(Utils.MONTHS));
        monthCB.setValue(Utils.MONTHS[1]);
        yearTB.setText("");

        monthCB.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);");
        yearTB.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);");

        if (scene == null) {
            scene = new Scene(content);
        }
        myDialog.setScene(scene);

        saveBtn.setOnAction(arg0 -> {
            final int month = Utils.monthNumber(monthCB.getValue());
            final String yearStr = yearTB.getText();
            if (month == 0) {
                monthCB.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(240, 17, 99, 0.8), 10, 0, 0, 0);");
                return;
            }
            if (yearStr.isEmpty() || !Utils.isInteger(yearStr)) {
                yearTB.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(240, 17, 99, 0.8), 10, 0, 0, 0);");
                return;
            }
            int year = Integer.parseInt(yearStr);
            final Balance balance = new Balance(
                    null,
                    Utils.getCurrentTime(),
                    null,
                    null,
                    null,
                    month,
                    year
            );
            myDialog.close();
            scene.disposePeer();
            onSave.accept(balance);
        });
        myDialog.setResizable(false);
        myDialog.show();
    }
}

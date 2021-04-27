package controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmationPopupController {

    @FXML
    private Button confirmationYes;

    @FXML
    private Button confirmationCancel;

    @FXML
    private Label confirmationLabel;

    private Parent content;
    private Scene scene = null;

    public void setContent(final Parent content) {
        this.content = content;
    }

    public void show(final String title, final String text, Runnable onCancel, Runnable onConfirm) {
        final Stage myDialog = new Stage();
        myDialog.initModality(Modality.APPLICATION_MODAL);
        myDialog.setTitle(title);

        if (scene == null) {
            scene = new Scene(content);
        }
        myDialog.setScene(scene);
        confirmationLabel.setText(text);
        confirmationCancel.setOnAction(arg0 -> {
            myDialog.close();
            scene.disposePeer();
            onCancel.run();
        });
        confirmationYes.setOnAction(arg0 -> {
            myDialog.close();
            scene.disposePeer();
            onConfirm.run();
        });
        myDialog.setResizable(false);
        myDialog.show();
    }

}

package controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MessagePopupController {

    @FXML
    private Button popupOkBtn;

    @FXML
    private Label popupLabel;

    private Parent messageContent;
    private Scene messageScene = null;

    public void setContent(final Parent messageContent) {
        this.messageContent = messageContent;
    }

    public void show(final String title, final String text) {
        show(title, text, () -> {});
    }

    public void show(final String title, final String text, Runnable afterCloseAction) {
        final Stage myDialog = new Stage();
        myDialog.initModality(Modality.APPLICATION_MODAL);
        myDialog.setTitle(title);

        if (messageScene == null) {
            messageScene = new Scene(messageContent);
        }
        myDialog.setScene(messageScene);
        popupLabel.setText(text);
        popupOkBtn.setOnAction(arg0 -> {
            myDialog.close();
            messageScene.disposePeer();
            afterCloseAction.run();
        });
        myDialog.setResizable(false);
        myDialog.show();
    }

}

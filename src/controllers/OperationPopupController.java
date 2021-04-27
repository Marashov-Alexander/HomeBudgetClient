package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Article;
import models.Operation;
import utils.Utils;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public class OperationPopupController {

    @FXML private TextField opsDebitTB;
    @FXML private TextField opsCreditTB;
    @FXML private ComboBox<Article> opsArticleCB;

    @FXML
    private Button opsSaveBtn;

    private Parent content;
    private Scene scene = null;

    public void setContent(final Parent content) {
        this.content = content;
    }

    public void show(final String title, final Operation operation, final Consumer<Operation> onSave, final List<Article> articles) {
        final Stage myDialog = new Stage();
        myDialog.initModality(Modality.APPLICATION_MODAL);
        myDialog.setTitle(title);


        Callback<ListView<Article>, ListCell<Article>> cellFactory = new Callback<>() {

            @Override
            public ListCell<Article> call(ListView<Article> l) {
                return new ListCell<>() {

                    @Override
                    protected void updateItem(Article item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                };
            }
        };
        opsArticleCB.setButtonCell(cellFactory.call(null));
        opsArticleCB.setCellFactory(cellFactory);
        opsArticleCB.setItems(FXCollections.observableArrayList(articles));

        if (operation != null) {
            opsCreditTB.setText(String.valueOf(operation.getCredit()));
            opsDebitTB.setText(String.valueOf(operation.getDebit()));
            opsArticleCB.setValue(articles.stream().filter(a -> a.id.equals(operation.articleId)).findAny().orElse(null));
        } else {
            opsArticleCB.setValue(null);
            opsCreditTB.setText("0");
            opsDebitTB.setText("0");
        }

        opsCreditTB.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);");
        opsDebitTB.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);");
        opsArticleCB.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);");

        if (scene == null) {
            scene = new Scene(content);
        }
        myDialog.setScene(scene);

        opsSaveBtn.setOnAction(arg0 -> {
            final String opCredit = opsCreditTB.getText();
            final String opDebit = opsDebitTB.getText();
            final Article article = opsArticleCB.getValue();

            if (opCredit.isEmpty() || !Utils.isInteger(opCredit)) {
                opsCreditTB.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(240, 17, 99, 0.8), 10, 0, 0, 0);");
                return;
            }

            if (opDebit.isEmpty() || !Utils.isInteger(opDebit)) {
                opsDebitTB.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(240, 17, 99, 0.8), 10, 0, 0, 0);");
                return;
            }

            if (article == null) {
                opsArticleCB.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(240, 17, 99, 0.8), 10, 0, 0, 0);");
                return;
            }

            int debit = Integer.parseInt(opDebit);
            int credit = Integer.parseInt(opCredit);

            Operation updatedOperation = operation;
            if (updatedOperation == null) {
                updatedOperation = new Operation(
                        null,
                        article.id,
                        article.name,
                        debit,
                        credit,
                        Utils.getCurrentTime(),
                        null,
                        null
                );
            } else {
                updatedOperation.debit = debit;
                updatedOperation.credit = credit;
                updatedOperation.articleId = article.id;
                updatedOperation.articleName = article.name;
            }
            myDialog.close();
            scene.disposePeer();
            onSave.accept(updatedOperation);
        });
        myDialog.setResizable(false);
        myDialog.show();
    }
}
